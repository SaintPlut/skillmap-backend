package com.landingapp.service;

import com.landingapp.model.User;
import com.landingapp.exception.AuthenticationException;
import com.landingapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Пользователь не найден"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationException("Неверный пароль");
        }

        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        String roleWithPrefix = "ROLE_" + user.getRole();

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(roleWithPrefix)
                .build();
    }

    @PostConstruct
    public void createDefaultAdmin() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            admin.setCreatedAt(LocalDateTime.now());
            userRepository.save(admin);
        }
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public long getTotalUsersCount() {
        return userRepository.count();
    }

    // ДОБАВЬТЕ ЭТИ МЕТОДЫ ДЛЯ РЕГИСТРАЦИИ

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        // Если в вашей модели User есть поле email, используйте existsByEmail
        // Если нет, можно временно вернуть false или добавить поле email
        try {
            return userRepository.existsByEmail(email);
        } catch (Exception e) {
            // Если метода existsByEmail еще нет в репозитории
            return false;
        }
    }

    public User createUser(String username, String password, String email, String role) {
        // Проверка на уникальность имени пользователя
        if (existsByUsername(username)) {
            throw new RuntimeException("Пользователь с таким именем уже существует: " + username);
        }

        // Создание нового пользователя
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setCreatedAt(LocalDateTime.now());

        // Если в вашей модели есть поле email, установите его
        try {
            user.setEmail(email);
        } catch (Exception e) {
            // Если поля email нет, игнорируем
            System.out.println("Email field not available: " + e.getMessage());
        }

        return userRepository.save(user);
    }

    // Дополнительный метод для создания пользователя без email (если поле отсутствует)
    public User createUser(String username, String password, String role) {
        return createUser(username, password, null, role);
    }
}