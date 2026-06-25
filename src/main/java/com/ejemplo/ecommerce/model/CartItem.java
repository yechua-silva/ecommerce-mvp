package com.ejemplo.ecommerce.model;

import java.math.BigDecimal;

public class CartItem {
    private Integer productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;

    public CartItem() {}

    public CartItem(Integer productId, String name, BigDecimal price, Integer quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getSubtotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}