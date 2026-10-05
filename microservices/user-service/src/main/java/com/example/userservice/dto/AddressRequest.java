package com.example.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Request body for POST /users/{id}/addresses.
 * userId is not sent by the client - user-service fills it in from the path
 * before forwarding the request to address-service through Feign.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressRequest {

    @NotBlank(message = "Street is required")
    private String street;

    @NotBlank(message = "City is required")
    private String city;

    private String state;

    private String zipCode;

    @NotBlank(message = "Country is required")
    private String country;

    private Long userId;
}
