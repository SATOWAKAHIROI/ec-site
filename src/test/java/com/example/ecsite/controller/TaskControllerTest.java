package com.example.ecsite.controller;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.ecsite.Dto.TaskResponse;
import com.example.ecsite.exception.TaskNotFoundException;
import com.example.ecsite.security.LoginUser;
import com.example.ecsite.service.JwtService;
import com.example.ecsite.service.TaskService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TaskService taskService;

    @MockitoBean
    JwtService jwtService;

    @Test
    void 認証済みユーザーのタスクを取得する() throws Exception{
        LoginUser loginUser = new LoginUser(
            1L,
            "yamada@example.com",
            "USER"
        );

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginUser, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

        mockMvc.perform(get("/api/tasks").with(authentication(authentication)))
            .andExpect(status().isOk());

        verify(taskService).getTasks(null, null, loginUser.userId());
    }

    @Test
    void 認証済みユーザーのタスクのうちキーワードと完了状態がマッチしたもののみ取得する() throws Exception{
        LoginUser loginUser = new LoginUser(
            1L,
            "yamada@example.com",
            "USER"
        );

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginUser, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

        mockMvc.perform(get("/api/tasks").param("keyword", "Spring").param("isCompleted", "false").with(authentication(authentication))).andExpect(status().isOk());
        verify(taskService).getTasks("Spring", false, loginUser.userId());
    }

    @Test
    void 指定タスクを取得する() throws Exception{
        LoginUser loginUser = new LoginUser(
            1L,
            "yamada@example.com",
            "USER"
        );

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginUser, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

        TaskResponse expected = new TaskResponse(1L, "Springboot学習", false);
        when(taskService.getTaskByIdAndUserId(1L, 1L)).thenReturn(expected);

        mockMvc.perform(get("/api/tasks/1").with(authentication(authentication)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.title", is("Springboot学習")))
            .andExpect(jsonPath("$.completed", is(false)));
            verify(taskService).getTaskByIdAndUserId(1L, 1L);
    }

    @Test
    void 存在しないタスクが指定されたらTaskNotFoundを投げる() throws Exception{
        LoginUser loginUser = new LoginUser(
            1L,
            "yamada@example.com",
            "USER"
        );

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginUser, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

        when(taskService.getTaskByIdAndUserId(999L, 1L)).thenThrow(new TaskNotFoundException("Task not Found"));

        mockMvc.perform(get("/api/tasks/999").with(authentication(authentication)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message", is("Task not Found")));
    }
}
