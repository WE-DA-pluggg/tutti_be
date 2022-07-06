package com.tutti.backend.dto.Feed;


import lombok.Getter;

import java.util.List;

// 비 로그인 유저의 메인 페이지(최신 순, 좋아요 순, 랜덤) middleDto
@Getter
public class MainPageListDto {

    private final List<SearchTitleDtoMapping> latestList;

    private final List<SearchTitleDtoMapping> likeList;

    private final List<SearchTitleDtoMapping> genreList; // 랜덤
    private final List<SearchTitleDtoMapping> videoList;

    public MainPageListDto(List<SearchTitleDtoMapping> latestList, List<SearchTitleDtoMapping> likeList, List<SearchTitleDtoMapping> randomList,List<SearchTitleDtoMapping> videoLIst) {
        this.latestList = latestList;
        this.likeList = likeList;
        this.genreList = randomList;
        this.videoList = videoLIst;

    }
}
