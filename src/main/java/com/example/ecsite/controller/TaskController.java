package com.example.ecsite.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecsite.Dto.TaskRequest;
import com.example.ecsite.Dto.TaskResponse;
import com.example.ecsite.security.LoginUser;
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
	public List<TaskResponse> getAllTasks(@RequestParam(name = "keyword" ,required = false) String keyword,
			@RequestParam(name = "isCompleted", required = false) Boolean isCompleted, @AuthenticationPrincipal LoginUser loginUser) {
		Long userId = loginUser.userId();
		return taskService.getTasks(keyword, isCompleted, userId);
	}

	@GetMapping("/{id}")
	public TaskResponse getTaskById(@PathVariable("id") Long id, @AuthenticationPrincipal LoginUser loginUser) {
		Long userId = loginUser.userId();
		return taskService.getTaskByIdAndUserId(id, userId);
	}

	@PostMapping
	public ResponseEntity<TaskResponse> createTask(@RequestBody @Valid TaskRequest taskRequest, @AuthenticationPrincipal LoginUser loginUser) {
		Long userId = loginUser.userId();
		TaskResponse response = taskService.createTask(taskRequest, userId);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/{id}")
	public TaskResponse updateTask(@PathVariable("id") Long id, @RequestBody @Valid TaskRequest taskRequest, @AuthenticationPrincipal LoginUser loginUser) {
		Long userId = loginUser.userId();
		return taskService.updateTask(id, taskRequest, userId);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTask(@PathVariable("id") Long id, @AuthenticationPrincipal LoginUser loginUser) {
		Long userId = loginUser.userId();
		taskService.deleteTask(id, userId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
