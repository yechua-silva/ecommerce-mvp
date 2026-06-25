# 🏋️ E-Gym Fitness — Ecommerce Spring Boot

> Proyecto final del Módulo 7: E-commerce completo con autenticación, roles, carrito de compras, checkout y persistencia real con PostgreSQL.

---

## 🔗 Repositorio Público

```
https://github.com/yechua-silva/ecommerce-spring-boot
```

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

# Ejecución automática de scripts SQL
# NEVER para producción/reinicios sucesivos; cambiar a "always" para primera ejecución
spring.sql.init.mode=never
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

### Opción A: Terminal con Maven (recomendado)

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
| **CLIENT** (autenticado) | `/catalog`, `/cart` y sus subrutas (`/add`, `/update`, `/remove`, `/checkout`) | Navegar catálogo, gestionar carrito y finalizar compra |
| **ADMIN** (autenticado) | `/admin/products` (listar, crear, editar, eliminar) | Gestión completa del catálogo |

### Flujo de redirección post-login

- Usuario con rol `ROLE_CLIENT` → redirige a `/catalog`
- Usuario con rol `ROLE_ADMIN` → redirige a `/catalog` (con link a Admin visible)

### Endpoints del Admin (CRUD Productos)

| Método | Ruta | Acción |
|--------|------|--------|
| GET | `/admin/products` | Listar productos (con filtros por nombre y categoría) |
| GET | `/admin/products/new` | Formulario de creación |
| POST | `/admin/products` | Guardar nuevo producto (con validaciones) |
| GET | `/admin/products/edit?id=X` | Formulario de edición |
| POST | `/admin/products/update` | Actualizar producto existente (con validaciones) |
| POST | `/admin/products/delete` | Eliminar producto |

### Endpoints del Carrito (Cliente autenticado)

| Método | Ruta | Acción |
|--------|------|--------|
| GET | `/cart` | Ver carrito con lista de productos, cantidades y total |
| POST | `/cart/add` | Agregar producto al carrito (desde catálogo) |
| POST | `/cart/update` | Actualizar cantidad de un producto en el carrito |
| POST | `/cart/remove` | Eliminar producto del carrito |
| POST | `/cart/checkout` | Finalizar compra: crea la orden en BD y vacía el carrito |

---

## 🔑 Credenciales de Prueba

| Email | Contraseña | Rol | Uso |
|-------|------------|-----|-----|
| `admin@egym.com` | `admin123` | ADMIN | Probar panel de administración y CRUD |
| *(registrarse en `/register`)* | *(elegir)* | CLIENT | Probar registro, catálogo, carrito y checkout |

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
| `EcommerceSpringBootApplicationTests` | Integración | Verifica que el contexto de Spring Boot levanta correctamente (4 tests) |
| `CartControllerTest` | Integración + Security | Valida el flujo completo del carrito: agregar productos, actualizar cantidades, checkout con y sin items, y control de acceso sin autenticación (6 tests) |

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
│   │   │   │   ├── CartController.java               # Carrito y checkout (NUEVO)
│   │   │   │   └── AdminProductController.java       # CRUD Admin con validaciones
│   │   │   ├── model/
│   │   │   │   ├── User.java
│   │   │   │   ├── Category.java
│   │   │   │   ├── Product.java (con validaciones @NotBlank, @DecimalMin)
│   │   │   │   ├── Customer.java
│   │   │   │   ├── Order.java
│   │   │   │   ├── OrderItem.java
│   │   │   │   └── CartItem.java                     # Modelo para sesión (NUEVO)
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── CategoryRepository.java
│   │   │   │   ├── ProductRepository.java
│   │   │   │   ├── CustomerRepository.java           (NUEVO)
│   │   │   │   ├── OrderRepository.java              (NUEVO)
│   │   │   │   └── OrderItemRepository.java          (NUEVO)
│   │   │   └── service/
│   │   │       └── UserDetailsServiceImpl.java       # Carga de usuario para Spring Security
│   │   └── resources/
│   │       ├── application.properties                # Configuración datasource
│   │       ├── schema.sql                            # DDL: creación de tablas
│   │       ├── seed.sql                              # DML: datos iniciales
│   │       ├── static/
│   │       │   └── css/
│   │       │       └── custom.css                    # Estilos personalizados
│   │       └── templates/
│   │           ├── login.html
│   │           ├── register.html
│   │           ├── catalog.html (con botón "Agregar al carrito")
│   │           ├── cart.html                         # Vista del carrito (NUEVO)
│   │           └── admin/
│   │               ├── product-list.html
│   │               └── product-form.html (con mensajes de validación)
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

## 🔧 Solución de Problemas

### Error en checkout: "Cliente no encontrado"

**Causa**: Al hacer checkout, el sistema busca un registro en la tabla `customers` con el email del usuario autenticado. Si no existe (por ejemplo, si el usuario se registró antes de que se implementara la creación automática de clientes), fallaba con un error.

**Solución aplicada**: En `CartController.java`, se reemplazó `customerRepository.findByEmail(email).orElseThrow(...)` por `customerRepository.findByEmail(email).orElseGet(() -> customerRepository.save(new Customer(email)))`. De esta forma, si el `Customer` aún no existe en la BD, se crea automáticamente en el mismo paso del checkout sin interrumpir el flujo.

### Error en filtros: `lower(bytea)`

**Causa**: Al aplicar filtros de búsqueda en el catálogo o el panel admin, aparecía el error `no existe la función lower(bytea)`. El problema no era el tipo de columna en la base de datos (ya estaba en `text`), sino que Hibernate 6, cuando recibe un parámetro `null` de tipo `String`, lo enlaza como `Types.JAVA_OBJECT`. El driver JDBC de PostgreSQL interpreta `JAVA_OBJECT` como `bytea` (OID 17) para valores nulos, y al concatenarlo en `CONCAT('%', :search, '%')` se genera un resultado `bytea`, que no puede ser pasado a `LOWER()`.

**Solución aplicada**: 
1. En `ProductRepository.java`: Cambiar `(:search IS NULL ...)` por `(:search = '' ...)`.
2. En `CatalogController.java` y `AdminProductController.java`: Pasar `""` (string vacío) en lugar de `null` cuando no hay búsqueda.
3. Así Hibernate enlaza el parámetro como `VARCHAR` y PostgreSQL aplica `LOWER()` sin problemas.

**Si el error persiste** y la base de datos tiene columnas `bytea`, ejecutar:
```sql
ALTER TABLE products ALTER COLUMN name TYPE text;
ALTER TABLE products ALTER COLUMN description TYPE text;
ALTER TABLE products ALTER COLUMN image_url TYPE text;
```

### Productos inactivos visibles en catálogo

**Causa**: Al buscar o filtrar productos en el catálogo, se mostraban todos los productos incluyendo los inactivos.

**Solución aplicada**: Se agregó el método `findAllFilteredActive` en `ProductRepository.java` que incluye `p.active = true` en la consulta, y el `CatalogController.java` ahora usa este método.

---

## ⚠️ Notas Importantes

1. **Scripts SQL automáticos**: `schema.sql` y `seed.sql` se ejecutan SOLO en la primera ejecución si configuras `spring.sql.init.mode=always`. Actualmente está en `never` para evitar conflictos en reinicios. Si necesitas resetear la BD, cambia temporalmente a `always`.
2. **Secuencias PostgreSQL**: `seed.sql` incluye `setval()` para sincronizar las secuencias `SERIAL` después de los inserts manuales, evitando errores de PK duplicada al crear nuevos registros desde JPA.
3. **Carrito por sesión**: El carrito se guarda en la sesión HTTP del usuario. Al hacer checkout, se persiste la orden en la base de datos y se vacía la sesión.
4. **Validaciones**: Los formularios de registro y CRUD de productos tienen validaciones con mensajes de error claros (gracias a Jakarta Bean Validation y `BindingResult`).
5. **Thymeleaf cache**: desactivado (`cache=false`) para desarrollo. En producción, activar.
6. **DevTools**: incluido para hot-reload durante desarrollo.

---

## ✨ Funcionalidades Adicionales (Módulo 7)

- **Carrito de compras** con agregar, quitar y actualizar cantidades.
- **Checkout** que genera una orden con sus ítems y la guarda en la base de datos.
- **Validaciones** en formularios de productos (nombre obligatorio, precio > 0) y en registro (email único, contraseña mínima).
- **Mensajes de éxito/error** en todas las operaciones (flash attributes).
- **Persistencia real** de órdenes y clientes.

---

¡Listo para usar y demostrar todas las habilidades del bootcamp! 🚀
