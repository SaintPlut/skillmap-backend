package com.skillmap.repository;

import com.skillmap.model.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    Optional<UserProgress> findByUserIdAndSkillNodeId(Long userId, Long skillNodeId);
    List<UserProgress> findByUserId(Long userId);

    @Query("SELECT up FROM UserProgress up WHERE up.user.id = :userId AND up.skillNode.skillMap.id = :mapId")
    List<UserProgress> findByUserIdAndMapId(@Param("userId") Long userId, @Param("mapId") Long mapId);
}