package com.example.meuApp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.meuApp.service.ProductService;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/carro")
    public String index(Model model) {
        model.addAttribute("productsList", productService.getAllProducts());
        return "carros/index";
    }

}