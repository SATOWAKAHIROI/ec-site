package com.example.ecsite.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecsite.Dto.TaskRequest;
import com.example.ecsite.Dto.TaskResponse;
import com.example.ecsite.service.TaskService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	public List<TaskResponse> getAllTasks(@RequestParam(required = false) String keyword,
			@RequestParam(required = false) Boolean isCompleted) {
		return taskService.getTasks(keyword, isCompleted);
	}

	@GetMapping("/{id}")
	public TaskResponse getTaskById(@PathVariable Long id) {
		return taskService.getTaskById(id);
	}

	@PostMapping
	public ResponseEntity<TaskResponse> createTask(@RequestBody @Valid TaskRequest taskRequest) {
		TaskResponse response = taskService.createTask(taskRequest);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/{id}")
	public TaskResponse updateTask(@PathVariable Long id, @RequestBody @Valid TaskRequest taskRequest) {
		return taskService.updateTask(id, taskRequest);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
		taskService.deleteTask(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/me")
	public Long getCurrentuser(Authentication authentication) {
		return (Long) authentication.getPrincipal();
	}

}
