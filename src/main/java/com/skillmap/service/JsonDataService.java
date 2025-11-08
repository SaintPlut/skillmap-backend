package com.skillmap.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class JsonDataService {
    private static final String DATA_FILE = "data/skillmap-data.json";
    private final ObjectMapper objectMapper;

    public JsonDataService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        ensureDataFile();
    }

    private void ensureDataFile() {
        try {
            File file = new File(DATA_FILE);
            if (!file.exists()) {
                file.getParentFile().mkdirs();

                Map<String, Object> initialData = new HashMap<>();
                initialData.put("users", new ArrayList<>());
                initialData.put("skillMaps", new ArrayList<>());

                // Добавляем тестового пользователя
                Map<String, Object> testUser = new HashMap<>();
                testUser.put("id", 1);
                testUser.put("name", "Алексей Петров");
                testUser.put("email", "alex@example.com");
                testUser.put("password", "password123");
                testUser.put("createdAt", new Date().toString());

                ((List<Map<String, Object>>) initialData.get("users")).add(testUser);

                objectMapper.writeValue(file, initialData);
                System.out.println("✅ Created data file: " + DATA_FILE);
            }
        } catch (IOException e) {
            System.err.println("❌ Error creating data file: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> readAllData() {
        try {
            return objectMapper.readValue(new File(DATA_FILE), Map.class);
        } catch (IOException e) {
            System.err.println("❌ Error reading data: " + e.getMessage());
            return new HashMap<>();
        }
    }

    public void writeAllData(Map<String, Object> data) {
        try {
            objectMapper.writeValue(new File(DATA_FILE), data);
        } catch (IOException e) {
            System.err.println("❌ Error writing data: " + e.getMessage());
        }
    }

    // Методы для пользователей
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getAllUsers() {
        Map<String, Object> data = readAllData();
        return (List<Map<String, Object>>) data.getOrDefault("users", new ArrayList<>());
    }

    public void saveUser(Map<String, Object> user) {
        Map<String, Object> data = readAllData();
        List<Map<String, Object>> users = getAllUsers();

        // Удаляем старого пользователя если есть
        users.removeIf(u -> u.get("email").equals(user.get("email")));

        // Добавляем нового
        users.add(user);
        data.put("users", users);
        writeAllData(data);
    }

    public Map<String, Object> findUserByEmail(String email) {
        return getAllUsers().stream()
                .filter(user -> email.equals(user.get("email")))
                .findFirst()
                .orElse(null);
    }

    // Методы для карт навыков
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getSkillMapsByUser(Long userId) {
        Map<String, Object> data = readAllData();
        List<Map<String, Object>> allMaps = (List<Map<String, Object>>) data.getOrDefault("skillMaps", new ArrayList<>());

        return allMaps.stream()
                .filter(map -> userId.equals(map.get("ownerId")))
                .toList();
    }

    public void saveSkillMap(Map<String, Object> skillMap) {
        Map<String, Object> data = readAllData();
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> skillMaps = (List<Map<String, Object>>) data.getOrDefault("skillMaps", new ArrayList<>());

        // Удаляем старую карту если есть
        skillMaps.removeIf(map -> map.get("id").equals(skillMap.get("id")));

        // Добавляем новую
        skillMaps.add(skillMap);
        data.put("skillMaps", skillMaps);
        writeAllData(data);
    }
}