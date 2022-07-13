package com.tutti.backend.service;

import com.tutti.backend.domain.LiveRoom;
import com.tutti.backend.domain.User;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LiveRoomService {
    private final UserRepository userRepository;
    private final S3Service service;
    private final HeaderTokenExtractor headerTokenExtractor;
    private final JwtDecoder jwtDecoder;
    private final LiveRoomRepository liveRoomRepository;

    @Autowired
    public LiveRoomService(
                       UserRepository userRepository,
                       S3Service service,
                       HeaderTokenExtractor headerTokenExtractor,
                       JwtDecoder jwtDecoder,
                       LiveRoomRepository liveRoomRepository
    ) {
        this.userRepository = userRepository;
        this.service = service;
        this.headerTokenExtractor =headerTokenExtractor;
        this.jwtDecoder=jwtDecoder;
        this.liveRoomRepository = liveRoomRepository;
    }

    public Object add(AddRoomRequestDto addRoomRequestDto, MultipartFile thumbNailImage, UserDetailsImpl userDetails) {
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

    public Object liveRoomDetail(Long chatRoomId) {
        LiveRoomSearchDetailDto liveRoomSearchDetailDto = new LiveRoomSearchDetailDto();

        liveRoomSearchDetailDto.setLiveRoomListDto(liveRoomRepository.searchLiveRoom(chatRoomId));
        liveRoomSearchDetailDto.setSuccess(200);
        liveRoomSearchDetailDto.setMessage("성공");
        return liveRoomSearchDetailDto;
    }

    public void liveRoomDelete(Long chatRoomId, User user) {
        LiveRoom liveRoom = liveRoomRepository.findById(chatRoomId).orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_LIVE_ROOM));

        if(user.getArtist().equals(liveRoom.getUser().getArtist())){
            liveRoom.setOnAir(false);
        }
    }
}
