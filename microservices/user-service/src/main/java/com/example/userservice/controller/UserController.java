package com.example.userservice.controller;

import com.example.userservice.dto.AddressRequest;
import com.example.userservice.dto.AddressResponse;
import com.example.userservice.dto.AddressSearchCriteria;
import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.dto.UserSearchCriteria;
import com.example.userservice.dto.UserWithAddressResponse;
import com.example.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    /** e.g. GET /users/search?keyword=rahul  or  ?name=rah&email=rahul@example.com */
    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> search(@ModelAttribute UserSearchCriteria criteria) {
        return ResponseEntity.ok(userService.search(criteria));
    }

    /** e.g. GET /users/search/by-address?city=Varanasi&country=India */
    @GetMapping("/search/by-address")
    public ResponseEntity<List<UserWithAddressResponse>> searchByAddress(
            @ModelAttribute AddressSearchCriteria criteria) {
        return ResponseEntity.ok(userService.searchByAddress(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserWithAddressResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserWithAddresses(id));
    }

    @PostMapping("/{id}/addresses")
    public ResponseEntity<AddressResponse> addAddress(@PathVariable Long id,
                                                      @Valid @RequestBody AddressRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addAddress(id, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
