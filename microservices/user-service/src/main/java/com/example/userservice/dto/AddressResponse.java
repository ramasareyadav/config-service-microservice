package com.example.userservice.dto;

import lombok.*;

/** Mirror of the response returned by address-service. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponse {

    private Long id;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private Long userId;
}
