package com.example.ecsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecsite.domain.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    
}
