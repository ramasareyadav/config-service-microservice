package com.example.addressservice.dto;

import lombok.*;

/** Response body returned by the address endpoints. */
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
