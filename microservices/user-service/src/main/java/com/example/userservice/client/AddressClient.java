package com.example.userservice.client;

import com.example.userservice.dto.AddressRequest;
import com.example.userservice.dto.AddressResponse;
import com.example.userservice.dto.AddressSearchCriteria;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Declarative HTTP client. "address-service" is resolved through Eureka
 * (and load-balanced) - no hard-coded host/port needed.
 */
@FeignClient(name = "address-service", fallbackFactory = AddressClientFallbackFactory.class)
public interface AddressClient {

    @PostMapping("/addresses")
    AddressResponse createAddress(@RequestBody AddressRequest request);

    @GetMapping("/addresses/user/{userId}")
    List<AddressResponse> getAddressesByUserId(@PathVariable("userId") Long userId);

    // @SpringQueryMap turns the criteria object into query params; null fields are not sent
    @GetMapping("/addresses/search")
    List<AddressResponse> searchAddresses(@SpringQueryMap AddressSearchCriteria criteria);

    @DeleteMapping("/addresses/user/{userId}")
    void deleteAddressesByUserId(@PathVariable("userId") Long userId);
}
