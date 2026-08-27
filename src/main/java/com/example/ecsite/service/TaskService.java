package com.example.ecsite.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ecsite.Dto.TaskRequest;
import com.example.ecsite.Dto.TaskResponse;
import com.example.ecsite.domain.Task;
import com.example.ecsite.domain.TaskHistory;
import com.example.ecsite.domain.User;
import com.example.ecsite.exception.TaskNotFoundException;
import com.example.ecsite.exception.UserNotFoundException;
import com.example.ecsite.mapper.TaskMapper;
import com.example.ecsite.repository.TaskHistoryRepository;
import com.example.ecsite.repository.TaskRepository;
import com.example.ecsite.repository.UserRepository;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskHistoryRepository taskHistoryRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper,
            TaskHistoryRepository taskHistoryRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.taskHistoryRepository = taskHistoryRepository;
        this.userRepository = userRepository;
    }

    public List<TaskResponse> getTasks(String keyword, Boolean isCompleted, Long userId) {
        // ここにタスクを取得するロジックを実装する
        List<Task> tasks;
        if (isCompleted == null) {
            if (keyword == null) {
                tasks = taskRepository.findByUserId(userId);
            } else {
                tasks = taskRepository.findByTitleContainingAndUserId(keyword, userId);
            }
        } else {
            if (keyword == null) {
                tasks = taskRepository.findByCompletedAndUserId(isCompleted, userId);
            } else {
                tasks = taskRepository.findByKeywordAndCompletedAndUserId(keyword, isCompleted, userId);
            }

        }
        List<TaskResponse> taskResponses = tasks.stream().map(taskMapper::toResponse).toList();

        return taskResponses;
    }

    public TaskResponse getTaskByIdAndUserId(Long id, Long userId) {
        Task task = taskRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        return taskMapper.toResponse(task);
    }

    public TaskResponse createTask(TaskRequest taskRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        Task task = new Task();
        task.setTitle(taskRequest.title());
        task.setCompleted(taskRequest.completed());
        task.setUser(user);
        Task savedTask = taskRepository.save(task);
        return taskMapper.toResponse(savedTask);
    }

    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest taskRequest, Long userId) {
        Task task = taskRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new TaskNotFoundException("Task not Found"));
        task.setTitle(taskRequest.title());
        task.setCompleted(taskRequest.completed());
        Task savedTask = taskRepository.save(task);
        TaskHistory history = new TaskHistory();
        history.setTask(savedTask);
        history.setTitle(savedTask.getTitle());
        taskHistoryRepository.save(history);
        return taskMapper.toResponse(savedTask);
    }

    public void deleteTask(Long id, Long userId) {
        Task task = taskRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new TaskNotFoundException("Task not Found"));
        taskRepository.delete(task);
    }

    public List<TaskResponse> getCompletedTasks(boolean isCompleted, Long userId) {
        List<TaskResponse> tasks = taskRepository.findByCompletedAndUserId(isCompleted, userId).stream()
                .map(taskMapper::toResponse).toList();
        return tasks;
    }
}
