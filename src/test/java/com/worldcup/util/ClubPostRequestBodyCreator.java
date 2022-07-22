package com.worldcup.util;

import com.worldcup.requests.ClubPostRequestBody;

public class ClubPostRequestBodyCreator {

    public static ClubPostRequestBody createClubPostRequestBody(){
        return ClubPostRequestBody.builder()
                .name(ClubCreator.createClubToBeSaved().getName())
                .build();
    }
}
