# 🏋️ E-Gym Fitness — Ecommerce Spring Boot

E-commerce con autenticación, roles, carrito de compras, checkout y persistencia con PostgreSQL.

---

## 📋 Requisitos Previos

| Requisito | Versión |
|-----------|---------|
| Java JDK | 17 |
| Maven | 3.9+ |
| PostgreSQL | 14+ |

---

## ⚙️ Configuración

### 1. Crear base de datos

```bash
sudo -iu postgres psql -c "CREATE DATABASE egym_fitness;"
```

### 2. Configurar `application.properties`

```properties
server.port=8080

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/egym_fitness
spring.datasource.username=postgres
spring.datasource.password=admin123
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

spring.thymeleaf.cache=false
spring.sql.init.mode=never
spring.sql.init.schema-locations=classpath:schema.sql
spring.sql.init.data-locations=classpath:seed.sql
```

> Para primera ejecución, cambiar `spring.sql.init.mode=always`.

---

## 🚀 Ejecución

```bash
mvn spring-boot:run
```

La aplicación estará en **http://localhost:8080**

---

## 🗺️ Rutas por Rol

| Rol | Rutas |
|-----|-------|
| **PÚBLICO** | `/`, `/catalog`, `/login`, `/register` |
| **CLIENT** | `/catalog`, `/cart`, `/cart/add`, `/cart/update`, `/cart/remove`, `/cart/checkout` |
| **ADMIN** | `/admin/products` (listar, crear, editar, eliminar) |

---

## 🔑 Credenciales

| Email | Contraseña | Rol |
|-------|------------|-----|
| `admin@egym.com` | `admin123` | ADMIN |
| *(registrarse)* | *(elegir)* | CLIENT |

---

## 🧪 Pruebas

```bash
mvn test
```

| Prueba | Tipo | Descripción |
|--------|------|-------------|
| `EcommerceSpringBootApplicationTests` | Integración | Contexto Spring Boot y seguridad (4 tests) |
| `CartControllerTest` | Integración | Flujo carrito: agregar, checkout, control de acceso (6 tests) |

---

## 📁 Estructura

```
ecommerce-spring-boot/
├── pom.xml
├── src/main/java/com/ejemplo/ecommerce/
│   ├── config/SecurityConfig.java
│   ├── controller/
│   │   ├── AuthController.java
│   │   ├── CatalogController.java
│   │   ├── CartController.java
│   │   └── AdminProductController.java
│   ├── model/
│   │   ├── User.java, Category.java, Product.java
│   │   ├── Customer.java, Order.java, OrderItem.java
│   │   └── CartItem.java
│   ├── repository/
│   │   ├── UserRepository.java, CategoryRepository.java
│   │   ├── ProductRepository.java, CustomerRepository.java
│   │   ├── OrderRepository.java, OrderItemRepository.java
│   └── service/
│       └── UserDetailsServiceImpl.java
├── src/main/resources/
│   ├── application.properties
│   ├── schema.sql
│   ├── seed.sql
│   ├── static/css/custom.css
│   └── templates/ (login, register, catalog, cart, admin/)
└── src/test/java/
    └── com/ejemplo/ecommerce/ (tests)
```

---

## 🛠️ Tecnologías

| Capa | Tecnología |
|------|------------|
| Framework | Spring Boot 3.5.15 |
| Seguridad | Spring Security 6 |
| Vistas | Thymeleaf |
| Persistencia | Spring Data JPA + Hibernate |
| Base de datos | PostgreSQL |
| Validación | Jakarta Bean Validation |
| Frontend | Bootstrap 5.3.8 |
| Build | Maven |
| Java | 17 |
