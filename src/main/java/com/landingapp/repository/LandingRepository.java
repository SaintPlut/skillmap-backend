package com.landingapp.repository;

import com.landingapp.model.Landing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LandingRepository extends JpaRepository<Landing, Long>, JpaSpecificationExecutor<Landing> {

    List<Landing> findByUserId(Long userId);

    @Query("SELECT l FROM Landing l WHERE l.user.username = :username")
    List<Landing> findByUsername(@Param("username") String username);

    List<Landing> findByTemplateId(String templateId);

    @Query("SELECT COUNT(l) FROM Landing l")
    Long countAllLandings();
}
