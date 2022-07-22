package com.worldcup.util;

import com.worldcup.domain.Club;

public class ClubCreator {

    public static Club createClubToBeSaved(){
        return Club.builder().name("Bulgaria").build();
    }

    public static Club createValidClub(){
        return Club.builder()
                .name("Bulgaria")
                .id(1L)
                .build();
    }

    public static Club createValidUpdateClub(){
        return Club.builder()
                .name("Germany")
                .id(1L)
                .build();
    }
}
