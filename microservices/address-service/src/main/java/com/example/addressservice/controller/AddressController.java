package com.example.addressservice.controller;

import com.example.addressservice.dto.AddressRequest;
import com.example.addressservice.dto.AddressResponse;
import com.example.addressservice.dto.AddressSearchCriteria;
import com.example.addressservice.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressResponse> create(@Valid @RequestBody AddressRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getAll() {
        return ResponseEntity.ok(addressService.getAll());
    }

    /** e.g. GET /addresses/search?city=Varanasi&country=India  or  ?keyword=221001 */
    @GetMapping("/search")
    public ResponseEntity<List<AddressResponse>> search(@ModelAttribute AddressSearchCriteria criteria) {
        return ResponseEntity.ok(addressService.search(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.getById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AddressResponse>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(addressService.getByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody AddressRequest request) {
        return ResponseEntity.ok(addressService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteByUserId(@PathVariable Long userId) {
        addressService.deleteByUserId(userId);
        return ResponseEntity.noContent().build();
    }
}
