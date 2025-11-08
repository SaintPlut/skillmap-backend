package com.skillmap.controller;

import com.skillmap.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthService authService;

    // ДЕБАГ ЭНДПОИНТ - покажет что приходит
    @PostMapping("/debug")
    public ResponseEntity<?> debug(@RequestBody Object rawBody) {
        System.out.println("=== DEBUG RAW BODY ===");
        System.out.println("Type: " + rawBody.getClass().getName());
        System.out.println("Value: " + rawBody);
        System.out.println("Is array: " + rawBody.getClass().isArray());

        if (rawBody.getClass().isArray()) {
            Object[] array = (Object[]) rawBody;
            System.out.println("Array length: " + array.length);
            for (int i = 0; i < array.length; i++) {
                System.out.println("Array[" + i + "]: " + array[i]);
            }
        }

        return ResponseEntity.ok(Map.of(
                "receivedType", rawBody.getClass().getName(),
                "receivedValue", rawBody.toString(),
                "isArray", rawBody.getClass().isArray()
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Object rawBody) {
        try {
            System.out.println("=== REGISTER RAW ===");
            System.out.println("Type: " + rawBody.getClass().getName());
            System.out.println("Is array: " + rawBody.getClass().isArray());

            // ЕСЛИ ПРИШЕЛ МАССИВ - преобразуем в объект
            Map<String, String> request;
            if (rawBody.getClass().isArray()) {
                System.out.println("❌ ARRAY DETECTED! Converting...");
                Object[] array = (Object[]) rawBody;
                if (array.length > 0 && array[0] instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> firstElement = (Map<String, Object>) array[0];
                    request = Map.of(
                            "name", firstElement.getOrDefault("name", "").toString(),
                            "email", firstElement.getOrDefault("email", "").toString(),
                            "password", firstElement.getOrDefault("password", "").toString()
                    );
                } else {
                    throw new RuntimeException("Invalid array format");
                }
            } else if (rawBody instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> bodyMap = (Map<String, Object>) rawBody;
                request = Map.of(
                        "name", bodyMap.getOrDefault("name", "").toString(),
                        "email", bodyMap.getOrDefault("email", "").toString(),
                        "password", bodyMap.getOrDefault("password", "").toString()
                );
            } else {
                throw new RuntimeException("Invalid request format");
            }

            System.out.println("🔐 Register: " + request.get("email"));

            Map<String, Object> user = authService.register(
                    request.get("name"),
                    request.get("email"),
                    request.get("password")
            );

            return ResponseEntity.ok(Map.of(
                    "message", "User registered successfully",
                    "user", Map.of(
                            "id", user.get("id"),
                            "name", user.get("name"),
                            "email", user.get("email")
                    )
            ));
        } catch (Exception e) {
            System.err.println("❌ Register error: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            System.out.println("🔐 Login: " + request.get("email"));

            Map<String, Object> user = authService.login(
                    request.get("email"),
                    request.get("password")
            );

            return ResponseEntity.ok(Map.of(
                    "message", "Login successful",
                    "user", Map.of(
                            "id", user.get("id"),
                            "name", user.get("name"),
                            "email", user.get("email")
                    )
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}