package com.ejemplo.ecommerce.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMin;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    @Column(nullable = false, length = 150)
    @NotBlank(message = "El nombre es obligatorio")
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false, precision = 10, scale = 2)
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor que 0")
    private BigDecimal price;
    
    private String imageUrl;
    
    private Boolean active = true;
    
    public Product() {}
    
    // Getters y setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}