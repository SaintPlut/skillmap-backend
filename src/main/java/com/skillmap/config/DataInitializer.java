/*
package com.skillmap.config;

import com.skillmap.model.*;
import com.skillmap.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillMapRepository skillMapRepository;

    @Autowired
    private SkillNodeRepository skillNodeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserProgressRepository userProgressRepository;

    @Override
    public void run(String... args) throws Exception {
        // Проверяем, есть ли уже данные
        if (userRepository.count() > 0) {
            return;
        }

        // Создание тестового пользователя
        User user = new User("alex@example.com", "password123", "Алексей Петров");
        user = userRepository.save(user);

        // Создание категорий
        Category frontendCategory = new Category("Frontend", "#3498db");
        Category backendCategory = new Category("Backend", "#e74c3c");
        Category mobileCategory = new Category("Mobile", "#2ecc71");

        frontendCategory = categoryRepository.save(frontendCategory);
        backendCategory = categoryRepository.save(backendCategory);
        mobileCategory = categoryRepository.save(mobileCategory);

        // Создание карты навыков Frontend
        SkillMap frontendMap = new SkillMap("Frontend Development",
                "Карта навыков фронтенд-разработчика", user);
        frontendMap = skillMapRepository.save(frontendMap);

        // Добавляем категорию после сохранения карты
        frontendMap.getCategories().add(frontendCategory);
        skillMapRepository.save(frontendMap);

        // Создание узлов для Frontend карты
        SkillNode htmlCss = new SkillNode("HTML/CSS", "Основы верстки", frontendMap);
        htmlCss.setProgress(75);
        htmlCss = skillNodeRepository.save(htmlCss);

        SkillNode javascript = new SkillNode("JavaScript", "Основы программирования", frontendMap);
        javascript.setProgress(60);
        javascript = skillNodeRepository.save(javascript);

        SkillNode react = new SkillNode("React", "Библиотека для UI", frontendMap);
        react.setProgress(30);
        react = skillNodeRepository.save(react);

        // Дочерние узлы
        SkillNode semanticHtml = new SkillNode("Semantic HTML", "Семантическая разметка", frontendMap);
        semanticHtml.setProgress(90);
        semanticHtml.setParent(htmlCss);
        semanticHtml = skillNodeRepository.save(semanticHtml);

        SkillNode cssGrid = new SkillNode("CSS Grid & Flexbox", "Современная верстка", frontendMap);
        cssGrid.setProgress(80);
        cssGrid.setParent(htmlCss);
        cssGrid = skillNodeRepository.save(cssGrid);

        SkillNode es6 = new SkillNode("ES6+ Features", "Современный JavaScript", frontendMap);
        es6.setProgress(50);
        es6.setParent(javascript);
        es6 = skillNodeRepository.save(es6);

        SkillNode dom = new SkillNode("DOM Manipulation", "Работа с DOM API", frontendMap);
        dom.setProgress(70);
        dom.setParent(javascript);
        dom = skillNodeRepository.save(dom);

        // Создание карты навыков Backend
        SkillMap backendMap = new SkillMap("Backend Development",
                "Карта навыков бэкенд-разработчика", user);
        backendMap = skillMapRepository.save(backendMap);

        // Добавляем категорию
        backendMap.getCategories().add(backendCategory);
        skillMapRepository.save(backendMap);

        // Создание узлов для Backend карты
        SkillNode java = new SkillNode("Java", "Основы языка", backendMap);
        java.setProgress(85);
        java = skillNodeRepository.save(java);

        SkillNode spring = new SkillNode("Spring Boot", "Фреймворк для приложений", backendMap);
        spring.setProgress(60);
        spring = skillNodeRepository.save(spring);

        SkillNode database = new SkillNode("Database", "Работа с базами данных", backendMap);
        database.setProgress(70);
        database = skillNodeRepository.save(database);

        SkillNode restApi = new SkillNode("REST API", "Создание API", backendMap);
        restApi.setProgress(55);
        restApi.setParent(spring);
        restApi = skillNodeRepository.save(restApi);

        // Создание прогресса пользователя
        UserProgress progress1 = new UserProgress(user, semanticHtml, 90);
        UserProgress progress2 = new UserProgress(user, es6, 50);
        userProgressRepository.saveAll(Arrays.asList(progress1, progress2));

        System.out.println("✅ Тестовые данные созданы успешно!");
        System.out.println("👤 Пользователь: alex@example.com / password123");
        System.out.println("🗺 Карты: Frontend Development, Backend Development");
    }
}*/
