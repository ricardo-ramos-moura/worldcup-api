package com.worldcup.service;

import com.worldcup.domain.Club;
import com.worldcup.exception.BadRequestException;
import com.worldcup.repository.ClubRepository;
import com.worldcup.requests.ClubPostRequestBody;
import com.worldcup.util.ClubCreator;
import com.worldcup.util.ClubPostRequestBodyCreator;
import com.worldcup.util.ClubPutRequestBodyCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
class ClubServiceTest {
    @InjectMocks
    private ClubService clubService;

    @Mock
    private ClubRepository clubRepositoryMock;

    @BeforeEach
    void setup(){
        PageImpl<Club> clubPage = new PageImpl<>(List.of(ClubCreator.createValidClub()));
        BDDMockito.when(clubRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(clubPage);

        BDDMockito.when(clubRepositoryMock.findAll())
                .thenReturn(List.of(ClubCreator.createValidClub()));

        BDDMockito.when(clubRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(ClubCreator.createValidClub()));

        BDDMockito.when(clubRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(ClubCreator.createValidClub()));

        BDDMockito.when(clubRepositoryMock.save(ArgumentMatchers.any(Club.class)))
                .thenReturn(ClubCreator.createValidClub());


        BDDMockito.doNothing().when(clubRepositoryMock).delete(ArgumentMatchers.any(Club.class));
    }

    @Test
    @DisplayName("findAllPageable return list of clubs inside page object when successful")
    void findAllPageable_ReturnsListOfClubsInsidePageObject_whenSuccessful(){
        String expectedName = ClubCreator.createValidClub().getName();

        Page<Club> clubPage = clubService.findAllPageable(PageRequest.of(1, 1));

        Assertions.assertThat(clubPage).isNotNull();

        Assertions.assertThat(clubPage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(clubPage.toList().get(0).getName()).isEqualTo(expectedName);
    }


    @Test
    @DisplayName("findAll return list of clubs when successful")
    void findAll_ReturnsListOfClubs_whenSuccessful(){
        String expectedName = ClubCreator.createValidClub().getName();

        List<Club> clubs = clubService.findAll();

        Assertions.assertThat(clubs)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(clubs.get(0).getName()).isEqualTo(expectedName);
    }


    @Test
    @DisplayName("findByIdOrThrowBadRequestException Returns a club when successful")
    void findByIdOrThrowBadRequestException_returnClub_whenSuccessful(){
        Long expectedId = ClubCreator.createValidClub().getId();

        Club club = clubService.findByIdOrThrowBadRequestException(1);

        Assertions.assertThat(club).isNotNull();

        Assertions.assertThat(club.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException thows BadRequestExceptionw when club is not found.")
    void findByIdOrThrowBadRequestException_ThrowBadRequestException_WhenClubIsNotFound(){
        BDDMockito.when(clubRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> clubService.findByIdOrThrowBadRequestException(1));

    }

    @Test
    @DisplayName("findByName Returns list of club when successful")
    void findByName_returnsListOfClub_whenSuccessful(){

        BDDMockito.when(clubService.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(ClubCreator.createValidClub()));

        String expectedName = ClubCreator.createValidClub().getName();

        List<Club> clubs = clubService.findByName(expectedName);

        Assertions.assertThat(clubs)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(clubs.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName an empty list of club when club is not found")
    void findByName_returnsEmptyListOfClub_whenClubIsNotFund(){
        BDDMockito.when(clubRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Club> clubs = clubService.findByName("club");

        Assertions.assertThat(clubs)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save Returns a club when successful")
    void save_returnClub_whenSuccessful(){
        ClubPostRequestBody clubPostRequestBody = ClubPostRequestBodyCreator.createClubPostRequestBody();

        Club clubSaved = clubService.save(clubPostRequestBody);

        Assertions.assertThat(clubSaved).isNotNull().isEqualTo(ClubCreator.createValidClub());
    }

    @Test
    @DisplayName("replace updates club when successful")
    void replace_UpdatesClub_whenSuccessful(){
        Assertions.assertThatCode(() -> clubService.replace(ClubPutRequestBodyCreator.createClubPutRequestBody()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete removes club when successful")
    void delete_RemovesClub_whenSuccessful(){
        Assertions.assertThatCode(() -> clubService.delete(1L))
                .doesNotThrowAnyException();
    }
}