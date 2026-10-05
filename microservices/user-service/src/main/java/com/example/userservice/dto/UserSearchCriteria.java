package com.example.userservice.dto;

import lombok.*;

/** Bound from query params with @ModelAttribute, e.g. ?name=rah&email=rahul@example.com */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSearchCriteria {

    private String keyword;
    private String name;
    private String email;
    private String phone;
}
