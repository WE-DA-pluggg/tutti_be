package com.tutti.backend.dto.Feed;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FeedAll4Dto {
    int success;

    String message;

    List<SearchTitleDtoMapping> data;
}
