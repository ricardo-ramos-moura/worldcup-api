package com.worldcup.controller;

import com.worldcup.domain.Club;
import com.worldcup.requests.ClubPostRequestBody;
import com.worldcup.requests.ClubPutRequestBody;
import com.worldcup.service.ClubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("clubs")
@AllArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @GetMapping(path = "/findAllPageable")
    @Operation(summary = "List all clubs paginated", description = "The default size is 20, use the parameter size to change the default value",
    tags = {"club"})
    ResponseEntity<Page<Club>> list(@ParameterObject Pageable pageable){
        return ResponseEntity.ok(clubService.findAllPageable(pageable));

    }

    @GetMapping(path = "/findAll")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns list of Club when successful")
    })
    ResponseEntity<List<Club>> list(){
        return ResponseEntity.ok(clubService.findAll());
    }

    @GetMapping(path = "/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns Club when successful"),
            @ApiResponse(responseCode = "400", description = "When club does not found in the Database")
    })
    ResponseEntity<Club> findById(@PathVariable long id){
        return ResponseEntity.ok(clubService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(path = "by-id/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Find by name returns list of Club when successful")
    })
    ResponseEntity<Club> findByIdAutenticationPrincipal(@PathVariable long id,
                                                        @AuthenticationPrincipal UserDetails userDetails){

        System.out.println(userDetails);
        return ResponseEntity.ok(clubService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(path = "/find")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Find by name returns list of Club when successful")
    })
    ResponseEntity<List<Club>> findByName(@RequestParam String name){
        return ResponseEntity.ok(clubService.findByName(name));
    }

    @PostMapping(path = "/save")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Saved Club"),
            @ApiResponse(responseCode = "400", description = "When club does not exist in the Database")
    })
    ResponseEntity<Club> save(@RequestBody @Valid ClubPostRequestBody clubPostRequestBody){
        return new ResponseEntity<>(clubService.save(clubPostRequestBody), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/admin/delete/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "When club does not exist in the Database")
    })
    ResponseEntity<Void> delete(@PathVariable long id){
        clubService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "/replace")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Club not found in the Database")
    })
    ResponseEntity<Void> replace(@RequestBody ClubPutRequestBody clubPutRequestBody){
        clubService.replace(clubPutRequestBody);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
