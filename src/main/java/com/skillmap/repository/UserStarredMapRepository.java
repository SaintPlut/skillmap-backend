package com.skillmap.repository;

import com.skillmap.model.UserStarredMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserStarredMapRepository extends JpaRepository<UserStarredMap, Long> {
    Optional<UserStarredMap> findByUserIdAndSkillMapId(Long userId, Long skillMapId);
    boolean existsByUserIdAndSkillMapId(Long userId, Long skillMapId);
    void deleteByUserIdAndSkillMapId(Long userId, Long skillMapId);
}