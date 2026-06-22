package com.ejemplo.ecommerce;

import com.ejemplo.ecommerce.model.Category;
import com.ejemplo.ecommerce.model.User;
import com.ejemplo.ecommerce.repository.CategoryRepository;
import com.ejemplo.ecommerce.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class EcommerceSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceSpringBootApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepo, CategoryRepository catRepo, PasswordEncoder encoder) {
		return args -> {
			if (userRepo.findByEmail("admin@egym.com").isEmpty()) {
				User admin = new User();
				admin.setEmail("admin@egym.com");
				admin.setPassword(encoder.encode("admin123"));
				admin.setFirstName("Admin");
				admin.setLastName("EGym");
				admin.setRole("ROLE_ADMIN");
				userRepo.save(admin);
			}
			
			if (catRepo.count() == 0) {
				catRepo.save(new Category(null, "Packs de Entrenamiento", "pack"));
				catRepo.save(new Category(null, "Implementación", "implementacion"));
				catRepo.save(new Category(null, "Suplementación", "suplementacion"));
			}
		};
	}
}