package com.example.ecsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ecsite.domain.Task;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCompleted(boolean completed);

    @Query("""
            SELECT t
            FROM Task t
            WHERE t.title LIKE %:keyword%
            AND t.completed = :completed
            """)
    List<Task> findByKeywordAndCompleted(@Param("keyword") String keyword, @Param("completed") boolean completed);

    @Query("""
            SELECT t
            FROM Task t
            WHERE t.title LIKE %:keyword%
            """)
    List<Task> findByTitleContaining(@Param("keyword") String keyword);
}
