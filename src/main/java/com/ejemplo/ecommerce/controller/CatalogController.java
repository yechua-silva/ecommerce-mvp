package com.ejemplo.ecommerce.controller;

import com.ejemplo.ecommerce.model.Product;
import com.ejemplo.ecommerce.repository.CategoryRepository;
import com.ejemplo.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class CatalogController {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping({"/", "/catalog"})
    public String catalog(@RequestParam(required = false) String search,
                         @RequestParam(required = false) Integer category,
                         Model model) {
        List<Product> products;
        if (search != null || category != null) {
            products = productRepository.findAllFiltered(
                search != null && !search.isBlank() ? search : null, 
                category
            );
        } else {
            products = productRepository.findByActiveTrue();
        }
        
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("search", search);
        model.addAttribute("selectedCategory", category);
        return "catalog";
    }
}