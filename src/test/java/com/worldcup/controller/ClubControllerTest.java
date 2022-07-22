package com.worldcup.controller;

import com.worldcup.domain.Club;
import com.worldcup.requests.ClubPostRequestBody;
import com.worldcup.requests.ClubPutRequestBody;
import com.worldcup.service.ClubService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
class ClubControllerTest {
    @InjectMocks
    private ClubController clubController;

    @Mock
    private ClubService clubServiceMock;

    @BeforeEach
    void setup(){
        PageImpl<Club> clubPage = new PageImpl<>(List.of(ClubCreator.createValidClub()));
        BDDMockito.when(clubServiceMock.findAllPageable(ArgumentMatchers.any()))
                .thenReturn(clubPage);

        BDDMockito.when(clubServiceMock.findAll())
                .thenReturn(List.of(ClubCreator.createValidClub()));

        BDDMockito.when(clubServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(ClubCreator.createValidClub());

        BDDMockito.when(clubServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(ClubCreator.createValidClub()));

        BDDMockito.when(clubServiceMock.save(ArgumentMatchers.any(ClubPostRequestBody.class)))
                .thenReturn(ClubCreator.createValidClub());

        BDDMockito.doNothing().when(clubServiceMock).replace(ArgumentMatchers.any(ClubPutRequestBody.class));

        BDDMockito.doNothing().when(clubServiceMock).delete(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("List return list of clubs inside page object when successful")
    void list_ReturnsListOfClubsInsidePageObject_whenSuccessful(){
        String expectedName = ClubCreator.createValidClub().getName();

        Page<Club> clubPage = clubController.list(null).getBody();

        Assertions.assertThat(clubPage).isNotNull();

        Assertions.assertThat(clubPage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(clubPage.toList().get(0).getName()).isEqualTo(expectedName);
    }


    @Test
    @DisplayName("List return list of clubs when successful")
    void list_ReturnsListOfClubs_whenSuccessful(){
        String expectedName = ClubCreator.createValidClub().getName();

        List<Club> clubs = clubController.list().getBody();

        Assertions.assertThat(clubs)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(clubs.get(0).getName()).isEqualTo(expectedName);
    }


    @Test
    @DisplayName("findById Returns a club when successful")
    void findById_returnClub_whenSuccessful(){
        Long expectedId = ClubCreator.createValidClub().getId();

        Club club = clubController.findById(1).getBody();

        Assertions.assertThat(club).isNotNull();

        Assertions.assertThat(club.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName Returns list of club when successful")
    void findByName_returnsListOfClub_whenSuccessful(){

        BDDMockito.when(clubServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(ClubCreator.createValidClub()));

        String expectedName = ClubCreator.createValidClub().getName();

        List<Club> clubs = clubController.findByName(expectedName).getBody();

        Assertions.assertThat(clubs)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(clubs.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName an empty list of club when club is not found")
    void findByName_returnsEmptyListOfClub_whenClubIsNotFund(){
        BDDMockito.when(clubServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Club> clubs = clubController.findByName("club").getBody();

        Assertions.assertThat(clubs)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save Returns a club when successful")
    void save_returnClub_whenSuccessful(){

        Club club = clubController.save(ClubPostRequestBodyCreator.createClubPostRequestBody()).getBody();

        Assertions.assertThat(club).isNotNull().isEqualTo(ClubCreator.createValidClub());
    }

    @Test
    @DisplayName("replace updates club when successful")
    void replace_UpdatesClub_whenSuccessful(){

        Assertions.assertThatCode(() -> clubController.replace(ClubPutRequestBodyCreator.createClubPutRequestBody()))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = clubController.replace(ClubPutRequestBodyCreator.createClubPutRequestBody());

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete removes club when successful")
    void delete_RemovesClub_whenSuccessful(){
        Assertions.assertThatCode(() -> clubController.delete(1L))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = clubController.delete(1L);

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}