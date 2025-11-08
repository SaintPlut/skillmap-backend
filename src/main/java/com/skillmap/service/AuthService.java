package com.skillmap.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private JsonDataService jsonDataService;

    public Map<String, Object> register(String name, String email, String password) {
        // Проверяем нет ли пользователя
        if (jsonDataService.findUserByEmail(email) != null) {
            throw new RuntimeException("User already exists");
        }

        // Создаем пользователя
        Map<String, Object> user = new HashMap<>();
        user.put("id", System.currentTimeMillis());
        user.put("name", name);
        user.put("email", email);
        user.put("password", password); // В реальном приложении хэшируйте пароль!
        user.put("createdAt", new Date().toString());

        // Сохраняем
        jsonDataService.saveUser(user);

        return user;
    }

    public Map<String, Object> login(String email, String password) {
        Map<String, Object> user = jsonDataService.findUserByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!password.equals(user.get("password"))) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }
}