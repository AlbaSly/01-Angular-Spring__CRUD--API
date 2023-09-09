package com.raxelarias._spring_crud_api.repositories;

import com.raxelarias._spring_crud_api.models.Tutorial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
    List<Tutorial> findByPublished(boolean published);
    List<Tutorial> findByTitleContainingIgnoreCase(String title);
}
