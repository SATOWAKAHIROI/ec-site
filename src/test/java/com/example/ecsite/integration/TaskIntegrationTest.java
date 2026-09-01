package com.example.ecsite.integration;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import com.example.ecsite.security.LoginUser;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void タスク一覧を取得できる() throws Exception{
         LoginUser loginUser = new LoginUser(
            1L,
            "yamada@example.com",
            "USER"
        );

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginUser, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        
        mockMvc.perform(get("/api/tasks").with(authentication(authentication)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].title", is("SpringBootの学習")));
    }
}
