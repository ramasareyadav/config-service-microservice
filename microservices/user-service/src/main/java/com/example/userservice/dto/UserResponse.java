package com.example.userservice.dto;

import lombok.*;

/** Response body returned by the user endpoints. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
}
