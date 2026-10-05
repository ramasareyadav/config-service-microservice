package com.example.addressservice.repository;

import com.example.addressservice.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUserId(Long userId);

    void deleteByUserId(Long userId);

    boolean existsByUserIdAndStreetIgnoreCaseAndCityIgnoreCase(Long userId, String street, String city);

    /**
     * Optional filters - any parameter that is null is ignored.
     * keyword matches street / city / state / zipCode / country (contains, case-insensitive).
     */
    @Query("""
            SELECT a FROM Address a
            WHERE (:keyword IS NULL
                   OR LOWER(a.street)  LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(a.city)    LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(a.state)   LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(a.country) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR a.zipCode        LIKE CONCAT('%', :keyword, '%'))
              AND (:city    IS NULL OR LOWER(a.city)    = LOWER(:city))
              AND (:state   IS NULL OR LOWER(a.state)   = LOWER(:state))
              AND (:country IS NULL OR LOWER(a.country) = LOWER(:country))
              AND (:zipCode IS NULL OR a.zipCode        = :zipCode)
              AND (:userId  IS NULL OR a.userId         = :userId)
            """)
    List<Address> search(@Param("keyword") String keyword,
                         @Param("city") String city,
                         @Param("state") String state,
                         @Param("country") String country,
                         @Param("zipCode") String zipCode,
                         @Param("userId") Long userId);
}
