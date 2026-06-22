package com.ejemplo.ecommerce;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EcommerceSpringBootsApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    void catalogoPublico() throws Exception {
        mockMvc.perform(get("/catalog"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminConRol() throws Exception {
        mockMvc.perform(get("/admin/products"))
            .andExpect(status().isOk());
    }

    @Test
    void adminSinAuth() throws Exception {
        mockMvc.perform(get("/admin/products"))
            .andExpect(status().is3xxRedirection());
    }
}