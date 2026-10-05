package com.example.userservice.service.impl;

import com.example.userservice.client.AddressClient;
import com.example.userservice.dto.AddressRequest;
import com.example.userservice.dto.AddressResponse;
import com.example.userservice.dto.AddressSearchCriteria;
import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.dto.UserSearchCriteria;
import com.example.userservice.dto.UserWithAddressResponse;
import com.example.userservice.entity.User;
import com.example.userservice.exception.DuplicateResourceException;
import com.example.userservice.exception.InvalidRequestException;
import com.example.userservice.exception.ResourceNotFoundException;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressClient addressClient;   // OpenFeign
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public UserResponse create(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered: " + request.getEmail());
        }
        User user = modelMapper.map(request, User.class);
        return toResponse(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAll() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserWithAddressResponse getUserWithAddresses(Long id) {
        User user = findOrThrow(id);
        UserWithAddressResponse response = modelMapper.map(user, UserWithAddressResponse.class);
        response.setAddresses(addressClient.getAddressesByUserId(id));
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> search(UserSearchCriteria criteria) {
        return userRepository.search(
                        blankToNull(criteria.getKeyword()), blankToNull(criteria.getName()),
                        blankToNull(criteria.getEmail()), blankToNull(criteria.getPhone()))
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserWithAddressResponse> searchByAddress(AddressSearchCriteria criteria) {
        // normalise blanks to null so Feign does not send empty query params
        criteria.setKeyword(blankToNull(criteria.getKeyword()));
        criteria.setCity(blankToNull(criteria.getCity()));
        criteria.setState(blankToNull(criteria.getState()));
        criteria.setCountry(blankToNull(criteria.getCountry()));
        criteria.setZipCode(blankToNull(criteria.getZipCode()));

        if (criteria.getKeyword() == null && criteria.getCity() == null && criteria.getState() == null
                && criteria.getCountry() == null && criteria.getZipCode() == null) {
            throw new InvalidRequestException(
                    "At least one search criterion is required: keyword, city, state, country or zipCode");
        }

        // 1) ask address-service for the matching addresses (Feign)
        List<AddressResponse> addresses = addressClient.searchAddresses(criteria);

        // 2) group the matches by user id
        Map<Long, List<AddressResponse>> byUser = addresses.stream()
                .collect(Collectors.groupingBy(AddressResponse::getUserId));

        // 3) load those users from our own database
        return userRepository.findAllById(byUser.keySet()).stream()
                .map(u -> {
                    UserWithAddressResponse response = modelMapper.map(u, UserWithAddressResponse.class);
                    response.setAddresses(byUser.get(u.getId()));
                    return response;
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse addAddress(Long userId, AddressRequest request) {
        findOrThrow(userId);
        request.setUserId(userId);
        return addressClient.createAddress(request);
    }

    @Override
    @Transactional
    public UserResponse update(Long id, UserRequest request) {
        User user = findOrThrow(id);
        if (!user.getEmail().equalsIgnoreCase(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered: " + request.getEmail());
        }
        modelMapper.map(request, user);          // request has no id, so the id is untouched
        return toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = findOrThrow(id);
        addressClient.deleteAddressesByUserId(id);
        userRepository.delete(user);
    }

    private User findOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    private UserResponse toResponse(User u) {
        return modelMapper.map(u, UserResponse.class);
    }

    private String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}
