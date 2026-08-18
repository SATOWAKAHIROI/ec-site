package com.example.ecsite.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecsite.Dto.TaskRequest;
import com.example.ecsite.Dto.TaskResponse;
import com.example.ecsite.service.TaskService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



/** タスクAPI */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
	private final TaskService taskService;

	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	// ここにエンドポイントを実装する
	@GetMapping
	public List<TaskResponse> getAllTasks() {
		return taskService.getTasks();
	}

	@GetMapping("/{id}")
	public TaskResponse getTaskById(@PathVariable Long id) {
		return taskService.getTaskById(id);
	}

	@PostMapping
	public TaskResponse createTask(@RequestBody TaskRequest taskRequest) {
		return taskService.createTask(taskRequest);
	}
	
	
}
