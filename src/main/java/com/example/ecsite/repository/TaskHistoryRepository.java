package com.example.ecsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecsite.domain.TaskHistory;

public interface TaskHistoryRepository extends JpaRepository<TaskHistory, Long> {

}
