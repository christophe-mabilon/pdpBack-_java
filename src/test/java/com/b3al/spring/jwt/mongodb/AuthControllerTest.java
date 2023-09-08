package com.b3al.spring.jwt.mongodb;

import com.b3al.spring.jwt.mongodb.controllers.AuthController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest {
    /*
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void SignupTest() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully!"));
    }
    */
}
