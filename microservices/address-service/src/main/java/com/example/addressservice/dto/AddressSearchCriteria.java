package com.example.addressservice.dto;

import lombok.*;

/** Bound from query params with @ModelAttribute, e.g. ?city=Varanasi&userId=1 */
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
    private Long userId;
}
