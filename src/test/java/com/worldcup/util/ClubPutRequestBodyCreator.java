package com.worldcup.util;

import com.worldcup.requests.ClubPutRequestBody;

public class ClubPutRequestBodyCreator {

    public static ClubPutRequestBody createClubPutRequestBody(){
        return ClubPutRequestBody.builder()
                .id(ClubCreator.createValidUpdateClub().getId())
                .name(ClubCreator.createValidUpdateClub().getName())
                .build();
    }
}
