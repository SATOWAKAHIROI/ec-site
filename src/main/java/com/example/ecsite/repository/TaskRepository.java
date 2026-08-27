package com.example.ecsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ecsite.domain.Task;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCompletedAndUserId(boolean completed, Long userId);

    List<Task> findByUserId(Long userId);

    @Query("""
            SELECT t
            FROM Task t
            WHERE t.title LIKE %:keyword%
            AND t.completed = :completed
            AND t.user.id = :userId
            """)
    List<Task> findByKeywordAndCompletedAndUserId(@Param("keyword") String keyword, @Param("completed") boolean completed, @Param("userId") Long userId);

    @Query("""
            SELECT t
            FROM Task t
            WHERE t.title LIKE %:keyword%
            AND t.user.id = :userId
            """)
    List<Task> findByTitleContainingAndUserId(@Param("keyword") String keyword, @Param("userId") Long userId);

    Optional<Task> findByIdAndUserId(Long id, Long userId);
}
