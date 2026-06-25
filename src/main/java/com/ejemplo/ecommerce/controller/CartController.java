package com.ejemplo.ecommerce.controller;

import com.ejemplo.ecommerce.model.CartItem;
import com.ejemplo.ecommerce.model.Customer;
import com.ejemplo.ecommerce.model.Order;
import com.ejemplo.ecommerce.model.OrderItem;
import com.ejemplo.ecommerce.model.Product;
import com.ejemplo.ecommerce.repository.CustomerRepository;
import com.ejemplo.ecommerce.repository.OrderItemRepository;
import com.ejemplo.ecommerce.repository.OrderRepository;
import com.ejemplo.ecommerce.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    private List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        List<CartItem> cart = getCart(session);
        model.addAttribute("cart", cart);
        BigDecimal total = cart.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("total", total);
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Integer productId,
                            @RequestParam(defaultValue = "1") Integer quantity,
                            HttpSession session,
                            RedirectAttributes redirectAttrs) {
        if (quantity < 1) quantity = 1;
        Optional<Product> opt = productRepository.findById(productId);
        if (opt.isEmpty()) {
            redirectAttrs.addFlashAttribute("error", "Producto no encontrado");
            return "redirect:/catalog";
        }
        Product product = opt.get();
        if (!product.getActive()) {
            redirectAttrs.addFlashAttribute("error", "Producto no disponible");
            return "redirect:/catalog";
        }

        List<CartItem> cart = getCart(session);
        for (CartItem item : cart) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                redirectAttrs.addFlashAttribute("success", "Cantidad actualizada");
                return "redirect:/cart";
            }
        }
        CartItem newItem = new CartItem(productId, product.getName(), product.getPrice(), quantity);
        cart.add(newItem);
        redirectAttrs.addFlashAttribute("success", "Producto agregado al carrito");
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateQuantity(@RequestParam Integer productId,
                                 @RequestParam Integer quantity,
                                 HttpSession session) {
        if (quantity < 1) quantity = 1;
        List<CartItem> cart = getCart(session);
        for (CartItem item : cart) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(quantity);
                break;
            }
        }
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Integer productId,
                                 HttpSession session) {
        List<CartItem> cart = getCart(session);
        cart.removeIf(item -> item.getProductId().equals(productId));
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout(HttpSession session,
                           Authentication authentication,
                           RedirectAttributes redirectAttrs) {
        List<CartItem> cart = getCart(session);
        if (cart.isEmpty()) {
            redirectAttrs.addFlashAttribute("error", "El carrito está vacío");
            return "redirect:/cart";
        }

        String email = authentication.getName();
        Customer customer = customerRepository.findByEmail(email)
                .orElseGet(() -> {
                    // Crear cliente automáticamente si no existe
                    Customer newCustomer = new Customer();
                    newCustomer.setEmail(email);
                    newCustomer.setName("Cliente de " + email);
                    newCustomer.setPhone("");
                    newCustomer.setCity("");
                    return customerRepository.save(newCustomer);
                });

        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus("pagado");
        order.setCreatedAt(LocalDateTime.now());

        BigDecimal total = cart.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);

        order = orderRepository.save(order);

        for (CartItem ci : cart) {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            Product product = productRepository.findById(ci.getProductId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            oi.setProduct(product);
            oi.setQuantity(ci.getQuantity());
            oi.setUnitPrice(ci.getPrice());
            orderItemRepository.save(oi);
        }

        session.removeAttribute("cart");
        redirectAttrs.addFlashAttribute("success", "¡Compra realizada con éxito! Número de orden: " + order.getId());
        return "redirect:/catalog";
    }
}