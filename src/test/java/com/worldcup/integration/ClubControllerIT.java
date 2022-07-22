package com.worldcup.integration;

import com.worldcup.domain.Club;
import com.worldcup.domain.WorldCupUser;
import com.worldcup.repository.ClubRepository;
import com.worldcup.repository.WorldCupUserRepository;
import com.worldcup.requests.ClubPostRequestBody;
import com.worldcup.util.ClubCreator;
import com.worldcup.util.ClubPostRequestBodyCreator;
import com.worldcup.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ClubControllerIT {
    @Autowired
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplateRoleUser;

    @Autowired
    @Qualifier(value = "testRestTemplateRoleAdmin")
    private TestRestTemplate testRestTemplateRoleAdmin;

    @Autowired
    private WorldCupUserRepository worldCupUserRepository;

    @Autowired
    private ClubRepository clubRepository;

    private static final WorldCupUser USER = WorldCupUser.builder()
            .name("Ana Carolina")
            .password("{bcrypt}$2a$10$lP2lvuITfEq0n9Mx8ZiDzuICgUpYdheVuraFLNWchCubSl1TIoWNy")
            .username("ana")
            .authorities("ROLE_USER")
            .build();

    private static final WorldCupUser ADMIN = WorldCupUser.builder()
            .name("Ricardo Ramos")
            .password("{bcrypt}$2a$10$lP2lvuITfEq0n9Mx8ZiDzuICgUpYdheVuraFLNWchCubSl1TIoWNy")
            .username("ricardo")
            .authorities("ROLE_USER,ROLE_ADMIN")
            .build();

    @TestConfiguration
    @Lazy
    static class Config{
        @Bean(name="testRestTemplateRoleUser")
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port){
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:"+port)
                    .basicAuthentication("ana", "123456");

            return new TestRestTemplate(restTemplateBuilder);
        }

        @Bean(name="testRestTemplateRoleAdmin")
        public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}") int port){
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:"+port)
                    .basicAuthentication("ricardo", "123456");

            return new TestRestTemplate(restTemplateBuilder);
        }

    }

    @Test
    @DisplayName("List return list of clubs inside page object when successful")
    void list_ReturnsListOfClubsInsidePageObject_whenSuccessful(){
        Club savedClub = clubRepository.save(ClubCreator.createClubToBeSaved());

        worldCupUserRepository.save(USER);

        String expectedName = savedClub.getName();

        PageableResponse<Club> clubPage = testRestTemplateRoleUser.exchange("/clubs/findAllPageable", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Club>>() {
                }).getBody();

        Assertions.assertThat(clubPage).isNotNull();

        Assertions.assertThat(clubPage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(clubPage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findAll return list of clubs when successful")
    void list_ReturnsListOfClubs_whenSuccessful(){
        Club savedClub = clubRepository.save(ClubCreator.createClubToBeSaved());

        worldCupUserRepository.save(USER);

        String expectedName = savedClub.getName();

        List<Club> clubs = testRestTemplateRoleUser.exchange("/clubs/findAll", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Club>>() {
                }).getBody();

        Assertions.assertThat(clubs)
                .isNotEmpty()
                .hasSize(1)
                .isNotNull();

        Assertions.assertThat(clubs.get(0).getName()).isEqualTo(expectedName);
    }


    @Test
    @DisplayName("findById Returns a club when successful")
    void findById_returnClub_whenSuccessful(){
        Club savedClub = clubRepository.save(ClubCreator.createClubToBeSaved());

        worldCupUserRepository.save(USER);

        Long expectedId = savedClub.getId();

        Club club = testRestTemplateRoleUser.getForObject("/clubs/{id}", Club.class, expectedId);

        Assertions.assertThat(club).isNotNull();

        Assertions.assertThat(club.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName Returns list of club when successful")
    void findByName_returnsListOfClub_whenSuccessful(){
        Club savedClub = clubRepository.save(ClubCreator.createClubToBeSaved());

        worldCupUserRepository.save(USER);

        String expectedName = savedClub.getName();

        String url = String.format("/clubs/find?name=%s", expectedName);

        List<Club> clubs = testRestTemplateRoleUser.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Club>>() {
                }).getBody();

        Assertions.assertThat(clubs)
                .isNotEmpty()
                .hasSize(1)
                .isNotNull();

        Assertions.assertThat(clubs.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName an empty list of club when club is not found")
    void findByName_returnsEmptyListOfClub_whenClubIsNotFund(){
        worldCupUserRepository.save(USER);

        List<Club> clubs = testRestTemplateRoleUser.exchange("/clubs/find?name=URSS", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Club>>() {
                }).getBody();

        Assertions.assertThat(clubs)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save Returns a club when successful")
    void save_returnClub_whenSuccessful(){
        ClubPostRequestBody clubPostRequestBody = ClubPostRequestBodyCreator.createClubPostRequestBody();

        worldCupUserRepository.save(USER);

        ResponseEntity<Club> clubResponseEntity = testRestTemplateRoleUser.postForEntity("/clubs/save", clubPostRequestBody, Club.class);

        Assertions.assertThat(clubResponseEntity).isNotNull();
        Assertions.assertThat(clubResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(clubResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(clubResponseEntity.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("replace updates club when successful")
    void replace_UpdatesClub_whenSuccessful(){
        Club savedClub = clubRepository.save(ClubCreator.createClubToBeSaved());

        worldCupUserRepository.save(USER);

        savedClub.setName("new name");

        ResponseEntity<Void> clubResponseEntity = testRestTemplateRoleUser.exchange("/clubs/replace", HttpMethod.PUT, new HttpEntity<>(savedClub), Void.class);

        Assertions.assertThat(clubResponseEntity).isNotNull();

        Assertions.assertThat(clubResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete removes club when successful")
    void delete_RemovesClub_whenSuccessful(){
        Club savedClub = clubRepository.save(ClubCreator.createClubToBeSaved());

        worldCupUserRepository.save(ADMIN);

        ResponseEntity<Void> clubResponseEntity = testRestTemplateRoleAdmin.exchange("/clubs/admin/delete/{id}",
                HttpMethod.DELETE, null, Void.class, savedClub.getId());

        Assertions.assertThat(clubResponseEntity).isNotNull();

        Assertions.assertThat(clubResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete returns 403 when user is not Admin")
    void delete_Returns403_whenUserIsNotAdmin(){
        Club savedClub = clubRepository.save(ClubCreator.createClubToBeSaved());

        worldCupUserRepository.save(USER);

        ResponseEntity<Void> clubResponseEntity = testRestTemplateRoleUser.exchange("/clubs/admin/delete/{id}",
                HttpMethod.DELETE, null, Void.class, savedClub.getId());

        Assertions.assertThat(clubResponseEntity).isNotNull();

        Assertions.assertThat(clubResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}
