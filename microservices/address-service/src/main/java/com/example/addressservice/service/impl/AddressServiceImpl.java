package com.example.addressservice.service.impl;

import com.example.addressservice.dto.AddressRequest;
import com.example.addressservice.dto.AddressResponse;
import com.example.addressservice.dto.AddressSearchCriteria;
import com.example.addressservice.entity.Address;
import com.example.addressservice.exception.DuplicateResourceException;
import com.example.addressservice.exception.ResourceNotFoundException;
import com.example.addressservice.repository.AddressRepository;
import com.example.addressservice.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public AddressResponse create(AddressRequest request) {
        if (addressRepository.existsByUserIdAndStreetIgnoreCaseAndCityIgnoreCase(
                request.getUserId(), request.getStreet(), request.getCity())) {
            throw new DuplicateResourceException("This address already exists for user " + request.getUserId());
        }
        Address address = modelMapper.map(request, Address.class);
        return toResponse(addressRepository.save(address));
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getAll() {
        return addressRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getByUserId(Long userId) {
        return addressRepository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> search(AddressSearchCriteria c) {
        return addressRepository.search(
                        blankToNull(c.getKeyword()), blankToNull(c.getCity()), blankToNull(c.getState()),
                        blankToNull(c.getCountry()), blankToNull(c.getZipCode()), c.getUserId())
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public AddressResponse update(Long id, AddressRequest request) {
        Address address = findOrThrow(id);
        modelMapper.map(request, address);      // request has no id, so the id is untouched
        return toResponse(addressRepository.save(address));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        addressRepository.delete(findOrThrow(id));
    }

    @Override
    @Transactional
    public void deleteByUserId(Long userId) {
        addressRepository.deleteByUserId(userId);
    }

    private Address findOrThrow(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", id));
    }

    private AddressResponse toResponse(Address a) {
        return modelMapper.map(a, AddressResponse.class);
    }

    private String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}
