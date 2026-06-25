package com.ejemplo.ecommerce.controller;

import com.ejemplo.ecommerce.model.Category;
import com.ejemplo.ecommerce.model.Product;
import com.ejemplo.ecommerce.repository.CategoryRepository;
import com.ejemplo.ecommerce.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public String list(@RequestParam(required = false) String search,
                      @RequestParam(required = false) Integer category,
                      Model model) {
        // Use empty string instead of null to avoid Hibernate binding
        // the parameter as JAVA_OBJECT (which becomes bytea in PostgreSQL)
        String searchParam = (search != null && !search.isBlank()) ? search : "";
        model.addAttribute("products", productRepository.findAllFiltered(
            searchParam,
            category
        ));
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("search", search);
        model.addAttribute("selectedCategory", category);
        return "admin/product-list";
    }

    @GetMapping("/new")
    public String formNew(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/product-form";
    }

    @GetMapping("/edit")
    public String formEdit(@RequestParam Integer id, Model model) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) return "redirect:/admin/products";
        model.addAttribute("product", product.get());
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/product-form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("product") Product product,
                       BindingResult result,
                       @RequestParam Integer categoryId,
                       RedirectAttributes redirectAttrs,
                       Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            return "admin/product-form";
        }
        
        Category cat = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        product.setCategory(cat);
        product.setActive(true); // por defecto activo
        
        productRepository.save(product);
        redirectAttrs.addFlashAttribute("msg", "creado");
        return "redirect:/admin/products";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("product") Product product,
                         BindingResult result,
                         @RequestParam Integer categoryId,
                         @RequestParam(required = false) Boolean active,
                         RedirectAttributes redirectAttrs,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            return "admin/product-form";
        }
        
        // Buscar el producto existente para mantener el id y otros campos
        Product existing = productRepository.findById(product.getId())
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        // Actualizar campos permitidos
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setImageUrl(product.getImageUrl());
        existing.setActive(active != null ? active : false);
        
        Category cat = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        existing.setCategory(cat);
        
        productRepository.save(existing);
        redirectAttrs.addFlashAttribute("msg", "actualizado");
        return "redirect:/admin/products";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Integer id, RedirectAttributes redirectAttrs) {
        productRepository.deleteById(id);
        redirectAttrs.addFlashAttribute("msg", "eliminado");
        return "redirect:/admin/products";
    }
}