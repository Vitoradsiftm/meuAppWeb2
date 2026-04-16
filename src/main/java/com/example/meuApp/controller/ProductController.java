package com.example.meuApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.meuApp.service.ProductService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;

import com.example.meuApp.model.carro;

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
        return "carros/create";
    }

    @PostMapping("/carro/save")
    public String save(@ModelAttribute("product") @Valid carro product,
                       BindingResult result,
                       Model model) {

        // 🔥 validação
        if (result.hasErrors()) {
            model.addAttribute("product", product);
            return "carros/create";
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
        return "carros/edit";
    }
}