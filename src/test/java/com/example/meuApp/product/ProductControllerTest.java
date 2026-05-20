package com.example.meuApp.product;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.meuApp.config.TestConfig;
import com.example.meuApp.controller.ProductController;
import com.example.meuApp.model.carro;
import com.example.meuApp.service.ProductService;

@WebMvcTest(ProductController.class)
@Import(TestConfig.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @AfterEach
    void resetMocks() {
        reset(productService);
    }

    private List<carro> testCreateProductList() {
        carro productB = new carro();
        productB.setId(1L);
        productB.setDescription("Descricao");
        productB.setName("Produto B");
        productB.setPrice(65.24f);
        productB.setStock(121);

        return List.of(productB);
    }

    @Test
    @DisplayName("GET /carro - sem usuário autenticado")
    void testIndexNotAuthenticatedUser() throws Exception {
        mockMvc.perform(get("/carro"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /carro - usuário autenticado")
    void testIndexAuthenticatedUser() throws Exception {
        when(productService.getAllProducts()).thenReturn(testCreateProductList());

        mockMvc.perform(get("/carro"))
                .andExpect(status().isOk())
                .andExpect(view().name("carros/index"))
                .andExpect(model().attributeExists("productsList"))
                .andExpect(content().string(containsString("Listagem de Carros")))
                .andExpect(content().string(containsString("Produto B")));
    }

    @Test
    @WithMockUser(username = "teste@gmail.com", authorities = { "Admin" })
    @DisplayName("GET /carro - admin vê link de cadastrar")
    void testCreateFormAuthorizedUser() throws Exception {
        when(productService.getAllProducts()).thenReturn(testCreateProductList());

        mockMvc.perform(get("/carro"))
                .andExpect(status().isOk())
                .andExpect(view().name("carros/index"))
                .andExpect(content().string(containsString("Cadastrar Carro")));
    }

    @Test
    @WithMockUser(username = "teste@gmail.com", authorities = { "Manager" })
    @DisplayName("GET /carro - usuário sem permissão não vê link")
    void testCreateFormNotAuthorizedUser() throws Exception {
        when(productService.getAllProducts()).thenReturn(testCreateProductList());

        mockMvc.perform(get("/carro"))
                .andExpect(status().isOk())
                .andExpect(view().name("carros/index"))
                .andExpect(content().string(containsString("Listagem de Carros")));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /carro/save - erro de validação")
    void testSaveProductValidationError() throws Exception {
        mockMvc.perform(post("/carro/save")
                        .with(csrf())
                        .param("name", "")
                        .param("description", "")
                        .param("price", "")
                        .param("stock", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("carros/form"));

        verify(productService, never()).saveProduct(any(carro.class));
    }

    @Test
    @WithMockUser(username = "teste@gmail.com", authorities = { "Admin" })
    @DisplayName("POST /carro/save - produto salvo")
    void testSaveValidProduct() throws Exception {
        mockMvc.perform(post("/carro/save")
                        .with(csrf())
                        .param("name", "Novo Produto")
                        .param("description", "Descrição")
                        .param("price", "100.0")
                        .param("stock", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/carro"));

        verify(productService).saveProduct(any(carro.class));
    }
}