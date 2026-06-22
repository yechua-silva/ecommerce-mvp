# 🏋️ E-Gym Fitness — Ecommerce Spring Boot

> Proyecto final del Módulo 6: E-commerce con Spring Boot, Login, Roles y Persistencia real con PostgreSQL.

---

## 🔗 Repositorio Público

```
https://github.com/TU_USUARIO/ecommerce-spring-boot
```

> Reemplazá `TU_USUARIO` por tu usuario real de GitHub antes de entregar.

---

## 📋 Requisitos Previos

| Requisito | Versión | Nota |
|-----------|---------|------|
| Java JDK | 17 | Obligatorio (configurado en `pom.xml`) |
| Maven | 3.9+ | Gestor de dependencias y ciclo de vida |
| PostgreSQL | 14+ | Base de datos relacional externa |
| IDE (opcional) | Eclipse / IntelliJ | Para desarrollo y debug |

### Verificar instalación

```bash
java -version    # debe mostrar "17"
mvn -v           # debe mostrar Maven 3.9+
psql --version   # debe mostrar PostgreSQL 14+
```

---

## ⚙️ Configuración

### 1. Crear la base de datos en PostgreSQL

```bash
sudo -iu postgres psql -c "CREATE DATABASE egym_fitness;"
```

> Si la base ya existe y querés empezar desde cero:
> ```sql
> DROP TABLE IF EXISTS payments, order_items, orders, inventory, products, customers, categories, users CASCADE;
> ```

### 2. Configurar `application.properties`

Ubicación: `src/main/resources/application.properties`

```properties
server.port=8080
spring.application.name=ecommerce-spring-boot

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/egym_fitness
spring.datasource.username=postgres
spring.datasource.password=admin123
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA / Hibernate — desactivado para evitar conflicto con schema.sql
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Thymeleaf (modo desarrollo, sin caché)
spring.thymeleaf.cache=false

# Ejecución automática de scripts SQL (no solo en H2)
spring.sql.init.mode=always
spring.sql.init.schema-locations=classpath:schema.sql
spring.sql.init.data-locations=classpath:seed.sql
```

### 3. Notas de seguridad

- Las contraseñas se almacenan con **BCrypt** (`PasswordEncoder`).
- El login usa **email** como username (no un campo separado de username).
- Spring Security maneja sesiones HTTP estándar.
- El rol se guarda en la base como `ROLE_CLIENT` o `ROLE_ADMIN`.

---

## 🚀 Instrucciones de Ejecución

### Opción A: Terminal con Maven (recomendado para entrega)

```bash
# 1. Navegar al proyecto
cd /ruta/al/proyecto/ecommerce-spring-boot

# 2. Compilar y ejecutar en modo desarrollo
mvn spring-boot:run
```

La aplicación estará disponible en: **http://localhost:8080**

Para detener: `Ctrl + C`

### Opción B: Empaquetar y ejecutar .jar

```bash
# Compilar y generar el artefacto
mvn clean package

# Ejecutar el JAR generado
java -jar target/ecommerce-spring-boot-0.0.1-SNAPSHOT.jar
```

### Opción C: Desde el IDE (Eclipse / IntelliJ)

1. Importar como proyecto **Maven → Existing Maven Projects**.
2. Click derecho sobre `EcommerceSpringBootApplication.java` → **Run As → Java Application**.
3. Verificar en la consola:
   - `Started EcommerceSpringBootApplication in X.XXX seconds`
   - `Executing SQL script from URL [.../schema.sql]`
   - `Executing SQL script from URL [.../seed.sql]`

---

## 🗺️ Descripción de Rutas por Rol

| Rol | Rutas disponibles | Descripción |
|-----|-------------------|-------------|
| **PÚBLICO** (sin autenticar) | `/`, `/catalog`, `/login`, `/register`, `/css/**`, `/js/**`, `/images/**` | Ver catálogo, iniciar sesión, registrarse |
| **CLIENT** (autenticado) | `/catalog`, `/cart/**`, `/checkout/**` | Navegar catálogo, carrito, checkout |
| **ADMIN** (autenticado) | `/admin/**` (listar, crear, editar, eliminar productos) | Gestión completa del catálogo |

### Flujo de redirección post-login

- Usuario con rol `ROLE_CLIENT` → redirige a `/catalog`
- Usuario con rol `ROLE_ADMIN` → redirige a `/catalog` (con link a Admin visible)

### Endpoints del Admin (CRUD Productos)

| Método | Ruta | Acción |
|--------|------|--------|
| GET | `/admin/products` | Listar productos (con filtros por nombre y categoría) |
| GET | `/admin/products/new` | Formulario de creación |
| POST | `/admin/products` | Guardar nuevo producto |
| GET | `/admin/products/edit?id=X` | Formulario de edición |
| POST | `/admin/products/update` | Actualizar producto existente |
| POST | `/admin/products/delete` | Eliminar producto |

---

## 🔑 Credenciales de Prueba

| Email | Contraseña | Rol | Uso |
|-------|------------|-----|-----|
| `admin@egym.com` | `admin123` | ADMIN | Probar panel de administración |
| *(registrarse en `/register`)* | *(elegir)* | CLIENT | Probar registro y catálogo |

> El usuario admin se crea automáticamente al arrancar la app si no existe (`CommandLineRunner`).

---

## 🧪 Pruebas

El proyecto incluye pruebas unitarias e integración con **Spring Boot Test** y **Spring Security Test**.

### Ejecutar todas las pruebas

```bash
mvn test
```

### Pruebas incluidas (≥2 requeridas por rúbrica)

| Prueba | Tipo | Descripción |
|--------|------|-------------|
| `EcommerceSpringBootApplicationTests` | Integración | Verifica que el contexto de Spring Boot levanta correctamente |
| `AuthControllerTest` | Integración + Security | Valida registro de usuario y que el password se encripta con BCrypt |
| `SecurityConfigTest` | Integración + Security | Verifica que `/admin/**` retorna 403 para usuarios sin rol ADMIN y 302/200 para ADMIN |

> Ubicación: `src/test/java/com/ejemplo/ecommerce/`

---

## 📁 Estructura del Proyecto

```
ecommerce-spring-boot/
├── pom.xml                              # Dependencias Maven (Spring Boot 3.5.15)
├── src/
│   ├── main/
│   │   ├── java/com/ejemplo/ecommerce/
│   │   │   ├── EcommerceSpringBootApplication.java   # Clase principal + init data
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java               # Configuración Spring Security
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java               # Login / Register
│   │   │   │   ├── CatalogController.java            # Catálogo público
│   │   │   │   └── AdminProductController.java       # CRUD Admin
│   │   │   ├── model/
│   │   │   │   ├── User.java, Category.java, Product.java, Customer.java
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java, CategoryRepository.java, ProductRepository.java
│   │   │   └── service/
│   │   │       └── UserDetailsServiceImpl.java       # Carga de usuario para Spring Security
│   │   └── resources/
│   │       ├── application.properties                # Configuración datasource
│   │       ├── schema.sql                            # DDL: creación de tablas
│   │       ├── seed.sql                              # DML: datos iniciales
│   │       └── templates/
│   │           ├── login.html, register.html, catalog.html
│   │           └── admin/
│   │               ├── product-list.html, product-form.html
│   └── test/java/com/ejemplo/ecommerce/              # Pruebas JUnit + Spring Test
└── target/                                           # Artefactos generados por Maven
```

---

## 🛠️ Tecnologías Utilizadas

| Capa | Tecnología |
|------|------------|
| Framework | Spring Boot 3.5.15 |
| Seguridad | Spring Security 6 (form login, roles) |
| Vistas | Thymeleaf + Thymeleaf Extras Spring Security 6 |
| Persistencia | Spring Data JPA + Hibernate |
| Base de datos | PostgreSQL 14+ |
| Validación | Jakarta Bean Validation |
| Frontend | Bootstrap 5.3.8 (CDN) |
| Build | Maven |
| Java | 17 |

---

## 📦 Empaquetado para Entrega

```bash
# Desde la carpeta padre del proyecto
zip -r ecommerce-spring-boot.zip ecommerce-spring-boot/ -x "*/target/*" "*/.git/*"
```

El archivo `ecommerce-spring-boot.zip` contiene el proyecto listo para importar en Eclipse/IntelliJ y ejecutar.

---

## ⚠️ Notas Importantes

1. **Scripts SQL automáticos**: `schema.sql` y `seed.sql` se ejecutan al arrancar gracias a `spring.sql.init.mode=always`. Esto solo funciona si `spring.jpa.hibernate.ddl-auto=none`.
2. **Secuencias PostgreSQL**: `seed.sql` incluye `setval()` para sincronizar las secuencias `SERIAL` después de los inserts manuales, evitando errores de PK duplicada al crear nuevos registros desde JPA.
3. **Thymeleaf cache**: desactivado (`cache=false`) para desarrollo. En producción, activar.
4. **DevTools**: incluido para hot-reload durante desarrollo.

---

## 👥 Autor

- **Bootcamp Academy** — Módulo 6: E-commerce Spring Boot
- Proyecto desarrollado como parte del tramo final del bootcamp.

---

> **Estado del proyecto**: ✅ Funcional — arranca, conecta a PostgreSQL, carga datos iniciales, autentica por roles y permite CRUD completo de productos.
