package com.example.meuApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.meuApp.model.carro;
import com.example.meuApp.service.ProductService;

import jakarta.validation.Valid;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/carro")
    public String index(Model model) {
        model.addAttribute("productsList", productService.getAllProducts());
        return "carros/index";
    }

    @GetMapping("/carro/create")
    public String create(Model model) {
        model.addAttribute("product", new carro());
        return "carros/form";
    }

    @PostMapping("/carro/save")
    public String save(@ModelAttribute @Valid carro product,
                       BindingResult result,
                       Model model) {

        System.out.println(product);

        if (result.hasErrors()) {
            model.addAttribute("product", product);
            return "carros/form";
        }

        productService.saveProduct(product);
        return "redirect:/carro";
    }

    @GetMapping("/carro/delete/{id}")
    public String delete(@PathVariable Long id) {
        this.productService.deleteProductById(id);
        return "redirect:/carro";
    }

    @GetMapping("/carro/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        carro product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "carros/form";
    }
}