package com.example.userservice.dto;

import lombok.*;

import java.util.List;

/** A user together with their addresses (addresses are fetched from address-service). */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWithAddressResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private List<AddressResponse> addresses;
}
