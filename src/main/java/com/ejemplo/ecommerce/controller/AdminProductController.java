package com.ejemplo.ecommerce.controller;

import com.ejemplo.ecommerce.model.Category;
import com.ejemplo.ecommerce.model.Product;
import com.ejemplo.ecommerce.repository.CategoryRepository;
import com.ejemplo.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;
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
        model.addAttribute("products", productRepository.findAllFiltered(
            search != null && !search.isBlank() ? search : null,
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
    public String save(@RequestParam String name,
                      @RequestParam String description,
                      @RequestParam BigDecimal price,
                      @RequestParam Integer categoryId,
                      @RequestParam(required = false) String imageUrl,
                      RedirectAttributes redirectAttrs) {
        Product p = new Product();
        p.setName(name);
        p.setDescription(description);
        p.setPrice(price);
        p.setImageUrl(imageUrl);
        p.setActive(true);
        
        Category cat = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        p.setCategory(cat);
        
        productRepository.save(p);
        redirectAttrs.addFlashAttribute("msg", "creado");
        return "redirect:/admin/products";
    }

    @PostMapping("/update")
    public String update(@RequestParam Integer id,
                        @RequestParam String name,
                        @RequestParam String description,
                        @RequestParam BigDecimal price,
                        @RequestParam Integer categoryId,
                        @RequestParam(required = false) String imageUrl,
                        @RequestParam(required = false) Boolean active,
                        RedirectAttributes redirectAttrs) {
        Product p = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        p.setName(name);
        p.setDescription(description);
        p.setPrice(price);
        p.setImageUrl(imageUrl);
        p.setActive(active != null ? active : false);
        
        Category cat = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        p.setCategory(cat);
        
        productRepository.save(p);
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