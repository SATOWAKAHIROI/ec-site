package com.example.ecsite.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ecsite.Dto.TaskRequest;
import com.example.ecsite.Dto.TaskResponse;
import com.example.ecsite.domain.Task;
import com.example.ecsite.domain.TaskHistory;
import com.example.ecsite.exception.TaskNotFoundException;
import com.example.ecsite.mapper.TaskMapper;
import com.example.ecsite.repository.TaskHistoryRepository;
import com.example.ecsite.repository.TaskRepository;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskHistoryRepository taskHistoryRepository;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper,
            TaskHistoryRepository taskHistoryRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.taskHistoryRepository = taskHistoryRepository;
    }

    public List<TaskResponse> getTasks(String keyword, Boolean isCompleted) {
        // ここにタスクを取得するロジックを実装する
        List<Task> tasks;
        if (isCompleted == null) {
            if (keyword == null) {
                tasks = taskRepository.findAll();
            } else {
                tasks = taskRepository.findByTitleContaining(keyword);
            }
        } else {
            if (keyword == null) {
                tasks = taskRepository.findByCompleted(isCompleted);
            } else {
                tasks = taskRepository.findByKeywordAndCompleted(keyword, isCompleted);
            }

        }
        List<TaskResponse> taskResponses = tasks.stream().map(taskMapper::toResponse).toList();

        return taskResponses;
    }

    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        return taskMapper.toResponse(task);
    }

    public TaskResponse createTask(TaskRequest taskRequest) {
        Task task = new Task();
        task.setTitle(taskRequest.title());
        task.setCompleted(taskRequest.completed());
        Task savedTask = taskRepository.save(task);
        return taskMapper.toResponse(savedTask);
    }

    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest taskRequest) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not Found"));
        task.setTitle(taskRequest.title());
        task.setCompleted(taskRequest.completed());
        Task savedTask = taskRepository.save(task);
        TaskHistory history = new TaskHistory();
        history.setTask(savedTask);
        history.setTitle(savedTask.getTitle());
        taskHistoryRepository.save(history);
        return taskMapper.toResponse(savedTask);
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not Found"));
        taskRepository.delete(task);
    }

    public List<TaskResponse> getCompletedTasks(boolean isCompleted) {
        List<TaskResponse> tasks = taskRepository.findByCompleted(isCompleted).stream()
                .map(taskMapper::toResponse).toList();
        return tasks;
    }
}
