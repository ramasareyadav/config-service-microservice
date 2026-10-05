package com.example.userservice.repository;

import com.example.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    /**
     * Optional filters - any parameter that is null is ignored.
     * keyword matches name / email / phone (contains, case-insensitive).
     */
    @Query("""
            SELECT u FROM User u
            WHERE (:keyword IS NULL
                   OR LOWER(u.name)  LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR u.phone        LIKE CONCAT('%', :keyword, '%'))
              AND (:name  IS NULL OR LOWER(u.name)  LIKE LOWER(CONCAT('%', :name, '%')))
              AND (:email IS NULL OR LOWER(u.email) = LOWER(:email))
              AND (:phone IS NULL OR u.phone        = :phone)
            """)
    List<User> search(@Param("keyword") String keyword,
                      @Param("name") String name,
                      @Param("email") String email,
                      @Param("phone") String phone);
}
