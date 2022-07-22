package com.worldcup.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClubPostRequestBody {
    @NotEmpty(message = "The club name cannot be empty")
    @Schema(description = "This is the Club's name", example = "Brazil", required = true)
    private String name;
}
