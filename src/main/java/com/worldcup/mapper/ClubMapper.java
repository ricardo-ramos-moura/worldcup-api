package com.worldcup.mapper;

import com.worldcup.domain.Club;
import com.worldcup.requests.ClubPostRequestBody;
import com.worldcup.requests.ClubPutRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class ClubMapper {
    public static final ClubMapper INSTANCE = Mappers.getMapper(ClubMapper.class);

    public abstract Club toClub(ClubPostRequestBody clubPostRequestBody);

    public abstract Club toClub(ClubPutRequestBody clubPutRequestBody);
}
