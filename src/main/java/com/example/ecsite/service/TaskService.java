package com.example.ecsite.service;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.example.ecsite.Dto.TaskRequest;
import com.example.ecsite.Dto.TaskResponse;
import com.example.ecsite.domain.Task;
import com.example.ecsite.repository.TaskRepository;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskResponse> getTasks() {
        // ここにタスクを取得するロジックを実装する
        List<Task> tasks = taskRepository.findAll();
        Stream<Task> taskStream = tasks.stream();
        Stream<TaskResponse> taskResponseStream = taskStream.map(task -> new TaskResponse(task.getId(), task.getTitle(), task.isCompleted()));
        List<TaskResponse> taskResponses = taskResponseStream.toList();

        return taskResponses;
    }

    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        return new TaskResponse(task.getId(), task.getTitle(), task.isCompleted());
    }

    public TaskResponse createTask(TaskRequest taskRequest) {
        Task task = new Task();
        task.setTitle(taskRequest.title());
        task.setCompleted(taskRequest.completed());
        Task savedTask = taskRepository.save(task);
        return new TaskResponse(savedTask.getId(), savedTask.getTitle(), savedTask.isCompleted());
    }
}
