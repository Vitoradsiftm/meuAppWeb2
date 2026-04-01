package com.example.meuApp.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.meuApp.model.carro;
import com.example.meuApp.repository.ProductRepository;
import com.example.meuApp.service.ProductService;


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List <carro> getAllProducts(){
        return productRepository.findAll();
    }

    @Override
    public void saveProduct(carro product){
        this.productRepository.save(product);
    }

    @Override
    public carro getProductById(long id) {
        Optional < carro > optional = productRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
    }

    @Override
    public void deleteProductById(long id) {
        this.productRepository.deleteById(id);
    }

}