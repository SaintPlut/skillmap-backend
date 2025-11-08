package com.skillmap.service;

import com.skillmap.dto.RegisterRequest;
import com.skillmap.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private JsonStorageService jsonStorageService;

    public Optional<User> findByEmail(String email) {
        Map<String, Object> userData = jsonStorageService.findUserByEmail(email);
        if (userData != null) {
            return Optional.of(mapToUser(userData));
        }
        return Optional.empty();
    }

    public User registerUser(RegisterRequest request) {
        // Проверяем нет ли уже пользователя
        if (jsonStorageService.findUserByEmail(request.getEmail()) != null) {
            throw new RuntimeException("User with this email already exists");
        }

        // Создаем нового пользователя
        Map<String, Object> userData = new HashMap<>();
        userData.put("id", System.currentTimeMillis()); // Простой ID
        userData.put("name", request.getName());
        userData.put("email", request.getEmail());
        userData.put("password", request.getPassword());
        userData.put("createdAt", LocalDateTime.now().toString());

        // Сохраняем в JSON
        jsonStorageService.saveUser(userData);

        return mapToUser(userData);
    }

    public Optional<User> findById(Long id) {
        return jsonStorageService.getUsers().stream()
                .filter(userData -> id.equals(userData.get("id")))
                .findFirst()
                .map(this::mapToUser);
    }

    private User mapToUser(Map<String, Object> userData) {
        User user = new User();
        user.setId(((Number) userData.get("id")).longValue());
        user.setName((String) userData.get("name"));
        user.setEmail((String) userData.get("email"));
        user.setPassword((String) userData.get("password"));
        // createdAt можно пропустить для простоты
        return user;
    }
}