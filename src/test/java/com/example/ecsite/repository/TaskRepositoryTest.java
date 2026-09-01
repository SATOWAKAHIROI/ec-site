package com.example.ecsite.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.ecsite.domain.Task;
import com.example.ecsite.domain.User;
import com.example.ecsite.exception.TaskNotFoundException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TaskRepositoryTest {
    @Autowired
    TaskRepository taskRepository;

    @Test
    void 指定タスクを取得(){
        Optional<Task> result = taskRepository.findByIdAndUserId(1L, 1L);
        assertTrue(result.isPresent());
        assertEquals("SpringBootの学習", result.get().getTitle());
        assertEquals(1L, result.get().getUser().getId());
    }

    @Test
    void 別のユーザーのタスクは取得できない(){
        Optional<Task> result = taskRepository.findByIdAndUserId(1L, 2L);
        assertTrue(result.isEmpty());
    }

    @Test
    void タスクの削除を確認(){
        Optional<Task> task = taskRepository.findByIdAndUserId(1L, 1L);
        assertTrue(task.isPresent());

        taskRepository.deleteById(1L);
        task = taskRepository.findByIdAndUserId(1L, 1L);

        assertTrue(task.isEmpty());
    }

    @Test
    void タスクの更新を確認(){
        Task task = taskRepository.findByIdAndUserId(1L, 1L).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        assertEquals("SpringBootの学習", task.getTitle());

        task.setTitle("Java学習");
        taskRepository.save(task);

        task = taskRepository.findByIdAndUserId(1L, 1L).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        assertEquals("Java学習", task.getTitle());
    }

    @Test
    void タスクの作成を確認(){
        User user = new User();
        user.setId(1L);
        user.setEmail("yamada@example.com");
        user.setPassword("password");
        user.setRole("USER");

        Task task = new Task();
        task.setTitle("Java学習");
        task.setCompleted(false);
        task.setUser(user);

        Task createdTask = taskRepository.save(task);

        Task result = taskRepository.findByIdAndUserId(createdTask.getId(), 1L).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        assertEquals("Java学習", result.getTitle());
        assertEquals(false, result.isCompleted());
    }
}
