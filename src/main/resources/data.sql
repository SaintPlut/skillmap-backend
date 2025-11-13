-- data.sql
-- Создание администратора (пароль: admin123)
INSERT INTO users (username, password, role)
VALUES ('admin', '$2a$10$ABCDEFGHIJKLMNOPQRSTUVWXYZ012345', 'ADMIN')
ON CONFLICT (username) DO NOTHING;

-- Базовые шаблоны
INSERT INTO templates (id, name, description, author, license, price, preview_image)
VALUES
('1', 'Бизнес лендинг', 'Профессиональный лендинг для бизнеса с акцентом на конверсию', 'WebStudio Pro', 'MIT', 49.99, 'https://example.com/business-preview.jpg'),
('2', 'Портфолио фотографа', 'Элегантный шаблон для демонстрации фотографий и творческого портфолио', 'Creative Designs', 'Creative Commons', 0, 'https://example.com/portfolio-preview.jpg')
ON CONFLICT (id) DO NOTHING;

-- Ключевые слова для шаблонов
INSERT INTO template_keywords (template_id, keyword)
VALUES
('1', 'бизнес'),
('1', 'лендинг'),
('1', 'конверсия'),
('1', 'профессиональный'),
('2', 'портфолио'),
('2', 'фотография'),
('2', 'креативный'),
('2', 'галерея')
ON CONFLICT DO NOTHING;

-- Блоки для бизнес шаблона
INSERT INTO template_blocks (block_id, type, label, default_value, required, "order", template_id)
VALUES
('main-header', 'header', 'Главный заголовок', 'Превращаем идеи в успешные бизнес-проекты', true, 0, '1'),
('hero-image', 'image', 'Герой-изображение', 'https://example.com/default-hero.jpg', false, 1, '1'),
('main-description', 'text', 'Основное описание', 'Мы помогаем бизнесу расти с помощью современных digital-решений', true, 2, '1'),
('features', 'text', 'Ключевые преимущества', '• Профессиональный подход\n• Современные технологии\n• Поддержка 24/7', false, 3, '1'),
('footer-text', 'footer', 'Текст в футере', '© 2024 Ваш Бизнес. Все права защищены.', false, 4, '1')
ON CONFLICT DO NOTHING;

-- Блоки для портфолио шаблона
INSERT INTO template_blocks (block_id, type, label, default_value, required, "order", template_id)
VALUES
('portfolio-title', 'header', 'Название портфолио', 'Творческое видение в каждом кадре', true, 0, '2'),
('profile-photo', 'image', 'Фотография автора', 'https://example.com/profile-photo.jpg', true, 1, '2'),
('bio', 'text', 'Биография', 'Я профессиональный фотограф с 5-летним опытом', false, 2, '2'),
('style-description', 'text', 'Описание стиля', 'Мой стиль — это сочетание естественности и художественного подхода', false, 3, '2'),
('contact-info', 'footer', 'Контактная информация', 'Email: hello@photographer.com | Instagram: @photographer', true, 4, '2')
ON CONFLICT DO NOTHING;