package com.skillmap.repository;

import com.skillmap.model.SkillMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillMapRepository extends JpaRepository<SkillMap, Long> {
    List<SkillMap> findByOwnerId(Long ownerId);
    List<SkillMap> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT sm FROM SkillMap sm JOIN sm.categories c WHERE c.id = :categoryId")
    List<SkillMap> findByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT sm FROM SkillMap sm JOIN UserStarredMap usm ON usm.skillMap = sm WHERE usm.user.id = :userId")
    List<SkillMap> findStarredByUserId(@Param("userId") Long userId);
}