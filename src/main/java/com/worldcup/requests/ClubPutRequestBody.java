package com.worldcup.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClubPutRequestBody {
    private long id;
    private String name;
}
