package com.example.meuApp.config;



import com.example.meuApp.service.ProductService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public ProductService productService() {
        return Mockito.mock(ProductService.class);
    }

} 