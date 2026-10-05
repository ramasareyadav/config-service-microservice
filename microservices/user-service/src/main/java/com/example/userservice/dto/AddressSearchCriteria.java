package com.example.userservice.dto;

import lombok.*;

/**
 * Bound from query params with @ModelAttribute in the controller and
 * forwarded to address-service as query params with @SpringQueryMap in the Feign client.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressSearchCriteria {

    private String keyword;
    private String city;
    private String state;
    private String country;
    private String zipCode;
}
