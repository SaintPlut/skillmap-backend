package com.landingapp.repository;

import com.landingapp.model.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends JpaRepository<Template, String> {

    boolean existsByName(String name);
}
