package com.tutti.backend.dto.Feed;


import lombok.Getter;

import java.util.List;

@Getter
public class MainPageListDto {

    private List<SearchTitleDtoMapping> lastestList;

    private List<MainPageFeedDto> likeList;

    private List<MainPageFeedDto> randomList;

    public MainPageListDto(List<SearchTitleDtoMapping> lastestList, List<MainPageFeedDto> likeList, List<MainPageFeedDto> randomList) {
        this.lastestList = lastestList;
        this.likeList = likeList;
        this.randomList = randomList;
    }
}
