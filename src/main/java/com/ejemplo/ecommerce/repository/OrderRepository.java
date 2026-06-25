package com.ejemplo.ecommerce.repository;

import com.ejemplo.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}