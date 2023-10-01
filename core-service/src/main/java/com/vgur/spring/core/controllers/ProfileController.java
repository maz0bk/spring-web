package com.vgur.spring.core.controllers;

import com.vgur.spring.api.core.ProfileDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/profile")
@Tag(name = "Users profiles", description = "Methods of working with users profiles")
public class ProfileController {
    @Operation(summary = "Get user profile",
    responses = {
            @ApiResponse(description = "Successful response",responseCode = "200",
    content = @Content(schema = @Schema(implementation = ProfileDto.class)))
            }
    )
    @GetMapping
    public ProfileDto getCurrentUserInfo(Principal principal) {
        return new ProfileDto(principal.getName());
    }
}
