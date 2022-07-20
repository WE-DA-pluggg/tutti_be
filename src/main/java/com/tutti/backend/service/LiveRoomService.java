package com.tutti.backend.service;

import com.tutti.backend.domain.LiveRoom;
import com.tutti.backend.domain.LiveRoomMessage;
import com.tutti.backend.domain.User;
import com.tutti.backend.dto.chatDto.Message;
import com.tutti.backend.dto.chatDto.messageChannel;
import com.tutti.backend.dto.liveRoom.AddRoomRequestDto;
import com.tutti.backend.dto.liveRoom.LiveRoomSearchDetailDto;
import com.tutti.backend.dto.liveRoom.LiveRoomSearchDto;
import com.tutti.backend.dto.user.FileRequestDto;
import com.tutti.backend.exception.CustomException;
import com.tutti.backend.exception.ErrorCode;
import com.tutti.backend.repository.*;
import com.tutti.backend.security.UserDetailsImpl;
import com.tutti.backend.security.jwt.HeaderTokenExtractor;
import com.tutti.backend.security.jwt.JwtDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class LiveRoomService {
    private final UserRepository userRepository;
    private final S3Service service;
    private final HeaderTokenExtractor headerTokenExtractor;
    private final JwtDecoder jwtDecoder;
    private final LiveRoomRepository liveRoomRepository;

    private RedisTemplate<String, messageChannel> conversationTemplate;

    @Autowired
    public LiveRoomService(
            UserRepository userRepository,
            S3Service service,
            HeaderTokenExtractor headerTokenExtractor,
            JwtDecoder jwtDecoder,
            LiveRoomRepository liveRoomRepository,
            RedisTemplate<String, messageChannel> conversationTemplate
    ) {
        this.userRepository = userRepository;
        this.service = service;
        this.headerTokenExtractor = headerTokenExtractor;
        this.jwtDecoder = jwtDecoder;
        this.liveRoomRepository = liveRoomRepository;
        this.conversationTemplate = conversationTemplate;
    }

    public Object add(AddRoomRequestDto addRoomRequestDto, MultipartFile thumbNailImage, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        List<LiveRoom> liveRooms = liveRoomRepository.findAllByUserAndOnAirTrue(user);
        if (!liveRooms.isEmpty()) {
            throw new CustomException(ErrorCode.ENOUGH_LIVE_ROOM);
        }
        FileRequestDto albumDto = service.upload(thumbNailImage);
        String thumbNailImageUrl = albumDto.getImageUrl();


        LiveRoom liveRoom = new LiveRoom(addRoomRequestDto.getRoomTitle(),
                userDetails.getUser(),
                addRoomRequestDto.getDescription(),
                thumbNailImageUrl
        );

        liveRoomRepository.save(liveRoom);
        return ResponseEntity.ok().body("라이브 생성 완료");
    }


    public Object liveRoomSearch() {
        LiveRoomSearchDto liveRoomSearchDto = new LiveRoomSearchDto();

        liveRoomSearchDto.setResults(liveRoomRepository.searchAllLiveRooms());
        liveRoomSearchDto.setSuccess(200);
        liveRoomSearchDto.setMessage("성공");
        return liveRoomSearchDto;
    }

    public Object liveRoomDetail(String artist) {
        LiveRoomSearchDetailDto liveRoomSearchDetailDto = new LiveRoomSearchDetailDto();

        liveRoomSearchDetailDto.setLiveRoomListDto(liveRoomRepository.searchLiveRoom(artist));
        liveRoomSearchDetailDto.setSuccess(200);
        liveRoomSearchDetailDto.setMessage("성공");
        return liveRoomSearchDetailDto;
    }

    //방송 종료
    public void liveRoomDelete(String artist, User user) {

        LiveRoom liveRoom = liveRoomRepository.findByUser(user);

        if (artist.equals(liveRoom.getUser().getArtist())) {
            HashOperations<String, String, messageChannel> ho = conversationTemplate.opsForHash();

            messageChannel messageChannel = ho.get(artist, artist);

            List<Message> messageList = messageChannel.getMessageList();

            List<LiveRoomMessage> liveRoomMessages = new ArrayList<>();

            for (Message message : messageList) {
                LiveRoomMessage liveRoomMessage = new LiveRoomMessage(message, liveRoom);
                liveRoomMessages.add(liveRoomMessage);
            }

            liveRoom.setMessages(liveRoomMessages);
            liveRoom.setOnAir(false);
            Long num = ho.delete(artist, artist);
            log.info(num.toString());
        }
    }
}
