package com.worldcup.service;

import com.worldcup.domain.Club;
import com.worldcup.exception.BadRequestException;
import com.worldcup.mapper.ClubMapper;
import com.worldcup.repository.ClubRepository;
import com.worldcup.requests.ClubPostRequestBody;
import com.worldcup.requests.ClubPutRequestBody;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;


    public Page<Club> findAllPageable(Pageable pageable){
        return clubRepository.findAll(pageable);
    }

    public List<Club> findAll(){
        return clubRepository.findAll();
    }

    public List<Club> findByName(String name){
        return clubRepository.findByName(name);
    }

    public Club findByIdOrThrowBadRequestException(long id){
        return clubRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Club not found."));
    }

    @Transactional
    public Club save(ClubPostRequestBody clubPostRequestBody) {
        List<Club> clubsSaved = findByName(clubPostRequestBody.getName());

        //if (!clubsSaved.isEmpty()) throw new BadRequestException("Club already registered.");

        return clubRepository.save(ClubMapper.INSTANCE.toClub(clubPostRequestBody));
    }

    public void delete(long id) {
        clubRepository.delete(findByIdOrThrowBadRequestException(id));
    }

    public void replace(ClubPutRequestBody clubPutRequestBody) {
        Club savedClub = findByIdOrThrowBadRequestException(clubPutRequestBody.getId());
        Club club = ClubMapper.INSTANCE.toClub(clubPutRequestBody);
        club.setId(savedClub.getId());

        clubRepository.save(club);
    }
}
