package com.example.addressservice.service;

import com.example.addressservice.dto.AddressRequest;
import com.example.addressservice.dto.AddressResponse;
import com.example.addressservice.dto.AddressSearchCriteria;

import java.util.List;

public interface AddressService {

    AddressResponse create(AddressRequest request);

    AddressResponse getById(Long id);

    List<AddressResponse> getAll();

    List<AddressResponse> getByUserId(Long userId);

    List<AddressResponse> search(AddressSearchCriteria criteria);

    AddressResponse update(Long id, AddressRequest request);

    void delete(Long id);

    void deleteByUserId(Long userId);
}
