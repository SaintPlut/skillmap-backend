// repository/UserRepository.java
package com.landingapp.repository;

import com.landingapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    // Добавьте эти методы
    boolean existsByUsername(String username);

    // Если в модели есть поле email, добавьте:
    boolean existsByEmail(String email);

    // Если поля email нет, можно пока не добавлять existsByEmail
}