package com.skillmap.controller;

import com.skillmap.dto.LoginRequest;
import com.skillmap.dto.RegisterRequest;
import com.skillmap.model.User;
import com.skillmap.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private UserService userService;

    // API 1: Регистрация пользователя
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            System.out.println("Получен запрос на регистрацию: " + request.getEmail());

            User user = userService.registerUser(request);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("user", Map.of(
                    "id", user.getId(),
                    "name", user.getName(),
                    "email", user.getEmail()
            ));

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println("Ошибка регистрации: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // API 2: Вход пользователя
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            System.out.println("Получен запрос на вход: " + request.getEmail());

            Optional<User> userOpt = userService.findByEmail(request.getEmail());

            if (userOpt.isPresent() && userOpt.get().getPassword().equals(request.getPassword())) {
                User user = userOpt.get();

                Map<String, Object> response = new HashMap<>();
                response.put("message", "Login successful");
                response.put("user", Map.of(
                        "id", user.getId(),
                        "name", user.getName(),
                        "email", user.getEmail()
                ));

                return ResponseEntity.ok(response);
            } else {
                System.err.println("Неверные учетные данные для: " + request.getEmail());
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid credentials"));
            }
        } catch (Exception e) {
            System.err.println("Ошибка входа: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Login error: " + e.getMessage()));
        }
    }
}