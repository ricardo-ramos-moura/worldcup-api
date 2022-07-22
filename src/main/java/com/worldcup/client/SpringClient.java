package com.worldcup.client;

import com.worldcup.domain.Club;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
public class SpringClient {
    public static void main(String[] args){
        ResponseEntity<Club> entity = new RestTemplate().getForEntity("http://localhost:8080/clubs/{id}", Club.class,2);
        log.info(entity.toString());

        Club object = new RestTemplate().getForObject("http://localhost:8080/clubs/{id}", Club.class, 2);
        log.info(object.toString());

        ResponseEntity<List<Club>> exchange = new RestTemplate().exchange("http://localhost:8080/clubs/findAll", HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });
        log.info(exchange.getBody().toString());

//        Club france = Club.builder().name("France").build();
//        Club franceSaved = new RestTemplate().postForObject("http://localhost:8080/clubs/save/", france,Club.class);
//        log.info(franceSaved.toString());

        Club sweden = Club.builder().name("Sweden").build();
        ResponseEntity<Club> swedenSaved = new RestTemplate().exchange("http://localhost:8080/clubs/save/",
                HttpMethod.POST,
                new HttpEntity<>(sweden, crateJsonHeader()),
                Club.class);
        log.info(swedenSaved.toString());

        Club clupUpdated = swedenSaved.getBody();
        clupUpdated.setName("Sweden 2");
        ResponseEntity<Void> swedenUpdated = new RestTemplate().exchange("http://localhost:8080/clubs/replace/",
                HttpMethod.PUT,
                new HttpEntity<>(clupUpdated, crateJsonHeader()),
                Void.class);
        log.info(swedenUpdated.toString());


        ResponseEntity<Void> swedenDelete = new RestTemplate().exchange("http://localhost:8080/clubs/delete/{id}",
                HttpMethod.DELETE,
               null,
                Void.class,
                clupUpdated.getId());
        log.info(swedenDelete.toString());
    }

    private static HttpHeaders crateJsonHeader(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
