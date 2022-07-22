package com.tutti.backend.dto.user.request;


import lombok.Getter;

@Getter
public class UserUpdateRequestDto {

    String profileText;

    String instagramUrl;

    String youtubeUrl;

    String[] genre;

    boolean[] genreSelected;


}
