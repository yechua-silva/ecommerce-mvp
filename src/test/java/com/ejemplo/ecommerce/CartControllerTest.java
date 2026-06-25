package com.ejemplo.ecommerce;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "test@test.com", roles = "CLIENT")
    void addToCart_ShouldAddProduct() throws Exception {
        mockMvc.perform(post("/cart/add")
                .param("productId", "1")
                .param("quantity", "1")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/cart"));
    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "CLIENT")
    void addToCart_WithInvalidProduct_ShouldReturnError() throws Exception {
        mockMvc.perform(post("/cart/add")
                .param("productId", "9999")
                .param("quantity", "1")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/catalog"));
    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "CLIENT")
    void viewCart_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/cart"))
            .andExpect(status().isOk())
            .andExpect(view().name("cart"));
    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "CLIENT")
    void checkout_WithEmptyCart_ShouldRedirectWithError() throws Exception {
        mockMvc.perform(post("/cart/checkout")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/cart"))
            .andExpect(flash().attributeExists("error"));
    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "CLIENT")
    void checkout_WithItemsInCart_ShouldCreateOrder() throws Exception {
        // Usar MockHttpSession para compartir sesión entre requests
        MockHttpSession session = new MockHttpSession();

        // 1. Agregar producto al carrito (usando la misma sesión)
        mockMvc.perform(post("/cart/add")
                .param("productId", "1")
                .param("quantity", "2")
                .session(session)
                .with(csrf()));

        // 2. Hacer checkout (con la misma sesión que tiene el carrito)
        mockMvc.perform(post("/cart/checkout")
                .session(session)
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/catalog"))
            .andExpect(flash().attributeExists("success"));
    }

    @Test
    void cartEndpoints_WithoutAuth_ShouldRedirect() throws Exception {
        // Sin autenticación → debe redirigir al login (302)
        mockMvc.perform(get("/cart"))
            .andExpect(status().is3xxRedirection());

        // POST con CSRF válido pero sin autenticación → debe redirigir al login
        mockMvc.perform(post("/cart/add")
                .param("productId", "1")
                .param("quantity", "1")
                .with(csrf()))
            .andExpect(status().is3xxRedirection());

        mockMvc.perform(post("/cart/checkout")
                .with(csrf()))
            .andExpect(status().is3xxRedirection());
    }
}
