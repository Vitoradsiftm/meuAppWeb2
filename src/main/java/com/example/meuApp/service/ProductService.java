package com.example.meuApp.service;



import java.util.List;
import com.example.meuApp.model.carro;

public interface ProductService {

    List <carro> getAllProducts();
    void saveProduct(carro product);
    carro getProductById(long id);
    void deleteProductById(long id);
}