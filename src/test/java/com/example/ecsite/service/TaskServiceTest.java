package com.example.ecsite.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ecsite.Dto.TaskResponse;
import com.example.ecsite.domain.Task;
import com.example.ecsite.exception.TaskNotFoundException;
import com.example.ecsite.mapper.TaskMapper;
import com.example.ecsite.repository.TaskRepository;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    TaskRepository taskRepository;
    @Spy
    TaskMapper taskMapper;
    @InjectMocks
    TaskService taskService;

    @Test
    void 指定タスクが存在する場合は正常系のレスポンスを返す(){
        // 準備
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Springboot学習");
        task.setCompleted(false);

        TaskResponse expected = new TaskResponse(1L, "Springboot学習", false);
        when(taskRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(task));

        // 実行
        TaskResponse actual = taskService.getTaskByIdAndUserId(1L, 1L);

        // 結果
        assertEquals(expected, actual);
        verify(taskRepository, times(1)).findByIdAndUserId(1L, 1L);
    }

    @Test
    void 指定タスクが存在しない場合はTaskNotFoundExceptionを返す(){
        //準備
        when(taskRepository.findByIdAndUserId(999L, 1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> {
            taskService.getTaskByIdAndUserId(999L, 1L);
        });
    }

}
