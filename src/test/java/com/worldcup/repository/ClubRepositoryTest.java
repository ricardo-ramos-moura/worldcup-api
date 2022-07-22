package com.worldcup.repository;

import com.worldcup.domain.Club;
import com.worldcup.util.ClubCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("Tests for Club Repository")
class ClubRepositoryTest {

    @Autowired
    private ClubRepository clubRepository;

    @Test
    @DisplayName("Save persist Club when successful")
    void save_PersistClub_WhenSuccessful() {
        Club clubToBeSaved = ClubCreator.createClubToBeSaved();
        Club clubSaved = this.clubRepository.save(clubToBeSaved);
        Assertions.assertThat(clubSaved).isNotNull();
        Assertions.assertThat(clubSaved.getId()).isNotNull();
        Assertions.assertThat(clubSaved.getName()).isEqualTo(clubToBeSaved.getName());
    }

    @Test
    @DisplayName("Save updates Club when successful")
    void save_UpdatesClub_WhenSuccessful() {
        Club clubToBeSaved = ClubCreator.createClubToBeSaved();
        Club clubSaved = this.clubRepository.save(clubToBeSaved);
        clubSaved.setName("Nigeria");

        Club clubUpdated = this.clubRepository.save(clubSaved);

        Assertions.assertThat(clubSaved).isNotNull();

        Assertions.assertThat(clubSaved.getId()).isNotNull();

        Assertions.assertThat(clubUpdated.getName()).isEqualTo(clubSaved.getName());
    }

    @Test
    @DisplayName("Delete removes Club when successful")
    void delete_RemovesClub_WhenSuccessful() {
        Club clubToBeSaved = ClubCreator.createClubToBeSaved();
        Club clubSaved = this.clubRepository.save(clubToBeSaved);

        this.clubRepository.delete(clubSaved);

        Optional<Club> clubOptional = this.clubRepository.findById(clubSaved.getId());

        Assertions.assertThat(clubOptional).isEmpty();
    }

    @Test
    @DisplayName("Find by name returns list of Club when successful")
    void findByName_ReturnsListOfClub_WhenSuccessful() {
        Club clubToBeSaved = ClubCreator.createClubToBeSaved();
        Club clubSaved = this.clubRepository.save(clubToBeSaved);

        List<Club> clubs = this.clubRepository.findByName(clubSaved.getName());

        Assertions.assertThat(clubs)
                .isNotEmpty()
                .contains(clubSaved);
    }

    @Test
    @DisplayName("Find by name returns list empty when club is not found")
    void findByName_ReturnsListEmpty_WhenClubIsNotFound() {
        List<Club> clubs = this.clubRepository.findByName("Namibia");

        Assertions.assertThat(clubs).isEmpty();
    }

    @Test
    @DisplayName("Save throw ConstraintViolationException then name is empty")
    void save_ThrowsConstraintViolationException_WhenNameIsEmpty() {
        Club club = new Club();
//        Assertions.assertThatThrownBy(() -> this.clubRepository.save(club))
//                .isInstanceOf(ConstraintViolationException.class);

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> this.clubRepository.save(club))
                .withMessageContaining("The club name cannot be empty");
    }

    @Test
    @DisplayName("Find by id returns Club when successful")
    void findById_ReturnsClub_WhenSuccessful() {
        Club clubToBeSaved = ClubCreator.createClubToBeSaved();
        Club clubSaved = this.clubRepository.save(clubToBeSaved);

        Optional<Club> club = this.clubRepository.findById(clubSaved.getId());

        Assertions.assertThat(club)
                .isNotEmpty()
                .contains(clubSaved);
    }
}