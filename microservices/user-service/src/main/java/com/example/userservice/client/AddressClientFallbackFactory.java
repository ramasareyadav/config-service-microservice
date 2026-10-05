package com.example.userservice.client;

import com.example.userservice.dto.AddressRequest;
import com.example.userservice.dto.AddressResponse;
import com.example.userservice.dto.AddressSearchCriteria;
import com.example.userservice.exception.ServiceUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class AddressClientFallbackFactory implements FallbackFactory<AddressClient> {

    private static final Logger log = LoggerFactory.getLogger(AddressClientFallbackFactory.class);

    @Override
    public AddressClient create(Throwable cause) {
        return new AddressClient() {

            @Override
            public AddressResponse createAddress(AddressRequest request) {
                log.error("address-service call failed (create): {}", cause.getMessage());
                throw new ServiceUnavailableException("Address service is currently unavailable");
            }

            // a user detail page can still be shown without addresses
            @Override
            public List<AddressResponse> getAddressesByUserId(Long userId) {
                log.warn("address-service call failed (get by user), returning empty list: {}", cause.getMessage());
                return Collections.emptyList();
            }

            // an empty result here would be misleading, so fail loudly
            @Override
            public List<AddressResponse> searchAddresses(AddressSearchCriteria criteria) {
                log.error("address-service call failed (search): {}", cause.getMessage());
                throw new ServiceUnavailableException("Address service is currently unavailable");
            }

            // deleting the user while addresses remain would leave orphan data
            @Override
            public void deleteAddressesByUserId(Long userId) {
                log.error("address-service call failed (delete): {}", cause.getMessage());
                throw new ServiceUnavailableException("Address service is currently unavailable, user was not deleted");
            }
        };
    }
}
