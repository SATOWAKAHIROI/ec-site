package com.example.ecsite.mapper;

import org.springframework.stereotype.Component;

import com.example.ecsite.Dto.TaskResponse;
import com.example.ecsite.domain.Task;

@Component
public class TaskMapper {
    public TaskResponse toResponse(Task task) {
        TaskResponse taskResponse = new TaskResponse(task.getId(), task.getTitle(), task.isCompleted());
        return taskResponse;
    }
}
