package com.landingapp.service;

import com.landingapp.model.Template;
import com.landingapp.model.TemplateBlock;
import com.landingapp.exception.ResourceNotFoundException;
import com.landingapp.repository.TemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TemplateService {

    @Autowired
    private TemplateRepository templateRepository;

    @PostConstruct
    public void initDefaultTemplates() {
        if (templateRepository.count() == 0) {
            Template businessTemplate = createBusinessTemplate();
            templateRepository.save(businessTemplate);
        }
    }

    private Template createBusinessTemplate() {
        Template template = new Template();
        template.setId("1");
        template.setName("Бизнес лендинг");
        template.setDescription("Профессиональный лендинг для бизнеса с акцентом на конверсию");
        template.setAuthor("WebStudio Pro");
        template.setLicense("MIT");
        template.setPrice(49.99);
        template.setPreviewImage("https://example.com/business-preview.jpg");
        template.setKeywords(Arrays.asList("бизнес", "лендинг", "конверсия", "профессиональный"));

        List<TemplateBlock> blocks = new ArrayList<>();

        TemplateBlock headerBlock = new TemplateBlock();
        headerBlock.setBlockId("main-header");
        headerBlock.setType("header");
        headerBlock.setLabel("Главный заголовок");
        headerBlock.setDefaultValue("Превращаем идеи в успешные бизнес-проекты");
        headerBlock.setRequired(true);
        headerBlock.setOrder(0);
        headerBlock.setTemplate(template);
        blocks.add(headerBlock);

        TemplateBlock imageBlock = new TemplateBlock();
        imageBlock.setBlockId("hero-image");
        imageBlock.setType("image");
        imageBlock.setLabel("Герой-изображение");
        imageBlock.setDefaultValue("https://example.com/default-hero.jpg");
        imageBlock.setRequired(false);
        imageBlock.setOrder(1);
        imageBlock.setTemplate(template);
        blocks.add(imageBlock);

        template.setEditableBlocks(blocks);
        return template;
    }

    public List<Template> getAllTemplates() {
        return templateRepository.findAll();
    }

    public Optional<Template> getTemplateById(String id) {
        return templateRepository.findById(id);
    }

    public Template createTemplate(Template template) {
        return templateRepository.save(template);
    }

    public Template updateTemplate(String id, Template templateDetails) {
        Template template = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Шаблон не найден"));

        template.setName(templateDetails.getName());
        template.setDescription(templateDetails.getDescription());
        template.setAuthor(templateDetails.getAuthor());
        template.setLicense(templateDetails.getLicense());
        template.setPrice(templateDetails.getPrice());
        template.setPreviewImage(templateDetails.getPreviewImage());
        template.setKeywords(templateDetails.getKeywords());

        return templateRepository.save(template);
    }

    public void deleteTemplate(String id) {
        Template template = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Шаблон не найден"));
        templateRepository.delete(template);
    }
}
