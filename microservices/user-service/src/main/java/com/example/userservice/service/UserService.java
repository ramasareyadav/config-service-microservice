package com.example.userservice.service;

import com.example.userservice.dto.AddressRequest;
import com.example.userservice.dto.AddressResponse;
import com.example.userservice.dto.AddressSearchCriteria;
import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.dto.UserSearchCriteria;
import com.example.userservice.dto.UserWithAddressResponse;

import java.util.List;

public interface UserService {

    UserResponse create(UserRequest request);

    List<UserResponse> getAll();

    UserWithAddressResponse getUserWithAddresses(Long id);

    /** Search users by their own fields. */
    List<UserResponse> search(UserSearchCriteria criteria);

    /** Search users by address fields (resolved through address-service via Feign). */
    List<UserWithAddressResponse> searchByAddress(AddressSearchCriteria criteria);

    AddressResponse addAddress(Long userId, AddressRequest request);

    UserResponse update(Long id, UserRequest request);

    void delete(Long id);
}
