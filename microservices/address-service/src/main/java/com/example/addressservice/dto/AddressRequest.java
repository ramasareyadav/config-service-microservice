package com.example.addressservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/** Request body for POST /addresses and PUT /addresses/{id}. */
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

    @NotNull(message = "userId is required")
    private Long userId;
}
