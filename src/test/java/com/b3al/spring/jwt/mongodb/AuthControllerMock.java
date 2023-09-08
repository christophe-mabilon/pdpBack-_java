package com.b3al.spring.jwt.mongodb;

import com.b3al.spring.jwt.mongodb.controllers.AuthController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerMock {
    /*
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthController authController;

    @Test
    public void SignupTest() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully!"));
    }
    */
}
