package com.skillmap.repository;

import com.skillmap.model.SkillNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillNodeRepository extends JpaRepository<SkillNode, Long> {
    List<SkillNode> findBySkillMapId(Long skillMapId);
    List<SkillNode> findByParentId(Long parentId);
    List<SkillNode> findByParentIsNullAndSkillMapId(Long skillMapId);

    @Query("SELECT sn FROM SkillNode sn WHERE sn.skillMap.id = :mapId AND sn.parent IS NULL")
    List<SkillNode> findRootNodesByMapId(@Param("mapId") Long mapId);
}