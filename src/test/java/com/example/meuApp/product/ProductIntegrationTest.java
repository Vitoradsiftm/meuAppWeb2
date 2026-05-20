package com.example.meuApp.product;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.meuApp.repository.ProductRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @WithMockUser(authorities = { "Admin" })
    void testSaveProductIntegration() throws Exception {

        mockMvc.perform(post("/carro/save")
                .with(csrf())
                .param("name", "Produto A")
                .param("description", "Descricao")
                .param("price", "65.24")
                .param("stock", "121"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/carro"));

        assertTrue(productRepository.findAll()
                .stream()
                .anyMatch(p -> "Produto A".equals(p.getName())));
    }
}