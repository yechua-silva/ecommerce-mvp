-- creando categorias
INSERT INTO categories (id, name, slug) VALUES
(1, 'Packs de Entrenamiento', 'pack'),
(2, 'Implementación',         'implementacion'),
(3, 'Suplementación',         'suplementacion');


-- insetar data mock a product
INSERT INTO products (id, category_id, name, description, price, image_url) VALUES
(1,  1, 'Titan Strength Pack',          'Incluye 12 sesiones presenciales, plan de entrenamiento personalizado y seguimiento por App.', 85000, 'https://loremflickr.com/400/200/fitness,gym?lock=1'),
(2,  1, 'Home Fitness Starter',         'Pack para entrenar en casa: 8 clases online en vivo y guía de nutrición en PDF.',               45000, 'https://loremflickr.com/400/200/fitness,gym?lock=2'),
(3,  1, 'Hypertrophy Masterclass',      'Acceso de por vida a 20 videos de técnica avanzada y e-book de hipertrofia.',                   30000, 'https://loremflickr.com/400/200/fitness,gym?lock=3'),
(6,  2, 'Set Mancuernas Hexagonales',   'Par de mancuernas de 10kg con recubrimiento de caucho para mayor durabilidad.',                 42000, 'https://loremflickr.com/400/200/fitness,gym?lock=6'),
(7,  2, 'Guantes Grip Pro',             'Guantes con protección de muñeca y palma antideslizante para levantamiento pesado.',            15000, 'https://loremflickr.com/400/200/fitness,gym?lock=7'),
(8,  2, 'Barras Paralelas de Calistenia','Paralelas bajas de acero, ideales para push-ups, dips y planchas.',                            28000, 'https://loremflickr.com/400/200/fitness,gym?lock=8'),
(16, 3, 'Whey Protein Isolate (1kg)',   'Proteína de suero de alta pureza, sabor Vainilla, baja en carbohidratos.',                      32000, 'https://loremflickr.com/400/200/fitness,gym?lock=16'),
(17, 3, 'Creatina Monohidratada',       '300g de creatina pura para fuerza y potencia explosiva. Sin sabor.',                            25000, 'https://loremflickr.com/400/200/fitness,gym?lock=17'),
(18, 3, 'Pre-Workout Explosion',        'Suplemento energético con beta-alanina y cafeína para entrenamientos intensos.',                28000, 'https://loremflickr.com/400/200/fitness,gym?lock=18');

-- stock inicial de cada producto
INSERT INTO inventory (product_id, quantity, movement, notes) VALUES
(1,  15,  'entrada', 'Stock inicial'),
(2,  50,  'entrada', 'Stock inicial'),
(3,  100, 'entrada', 'Stock inicial'),
(6,  8,   'entrada', 'Stock inicial'),
(7,  30,  'entrada', 'Stock inicial'),
(8,  12,  'entrada', 'Stock inicial'),
(16, 25,  'entrada', 'Stock inicial'),
(17, 40,  'entrada', 'Stock inicial'),
(18, 18,  'entrada', 'Stock inicial');


-- creacion de clientes
INSERT INTO customers (id, name, email, phone, city) VALUES
(1, 'Valentina Rojas',  'v.rojas@gmail.com',    '+56912340001', 'Temuco'),
(2, 'Matías Fuentes',   'm.fuentes@gmail.com',  '+56912340002', 'Santiago'),
(3, 'Camila Soto',      'c.soto@gmail.com',     '+56912340003', 'Concepción'),
(4, 'Sebastián Vera',   's.vera@gmail.com',     '+56912340004', 'Temuco'),
(5, 'Javiera Morales',  'j.morales@gmail.com',  '+56912340005', 'Valparaíso'),
(6, 'Diego Castillo',   'd.castillo@gmail.com', '+56912340006', 'Santiago');


-- creacion de ordenes
INSERT INTO orders (id, customer_id, status, total, created_at) VALUES
(1, 1, 'pagado',    85000,  '2026-02-05 10:30:00'),
(2, 2, 'pagado',    73000,  '2026-02-12 14:15:00'),
(3, 3, 'pagado',    57000,  '2026-02-20 09:00:00'),
(4, 1, 'pagado',    45000,  '2026-03-03 11:45:00'),
(5, 4, 'pagado',    67000,  '2026-03-10 16:00:00'),
(6, 2, 'pagado',    30000,  '2026-03-15 13:30:00'),
(7, 5, 'cancelado', 42000,  '2026-03-18 08:20:00'),
(8, 6, 'pendiente', 28000,  '2026-03-22 17:00:00');



INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
-- Orden 1: Titan Strength Pack
(1, 1,  1, 85000),
-- Orden 2: Mancuernas + Guantes
(2, 6,  1, 42000),
(2, 7,  2, 15000),  -- 15000 x2 = 30000 → total 72000 (precio aprox)
-- Orden 3: Whey + Creatina
(3, 16, 1, 32000),
(3, 17, 1, 25000),
-- Orden 4: Home Fitness Starter
(4, 2,  1, 45000),
-- Orden 5: Barras + Guantes
(5, 8,  1, 28000),
(5, 7,  1, 15000),
(5, 16, 1, 32000), -- incluye whey también
-- Orden 6: Hypertrophy Masterclass
(6, 3,  1, 30000),
-- Orden 7 (cancelada): Mancuernas
(7, 6,  1, 42000),
-- Orden 8 (pendiente): Barras
(8, 8,  1, 28000);


-- se pagan las ordenes con status en pagado
INSERT INTO payments (order_id, method, amount, paid_at) VALUES
(1, 'tarjeta',       85000, '2026-02-05 10:35:00'),
(2, 'transferencia', 73000, '2026-02-12 14:20:00'),
(3, 'tarjeta',       57000, '2026-02-20 09:05:00'),
(4, 'efectivo',      45000, '2026-03-03 11:50:00'),
(5, 'tarjeta',       67000, '2026-03-10 16:05:00'),
(6, 'transferencia', 30000, '2026-03-15 13:35:00');


-- Resetear secuencias para que JPA pueda seguir insertando después
SELECT setval('categories_id_seq',      COALESCE((SELECT MAX(id) FROM categories),      1));
SELECT setval('products_id_seq',        COALESCE((SELECT MAX(id) FROM products),        1));
SELECT setval('customers_id_seq',       COALESCE((SELECT MAX(id) FROM customers),       1));
SELECT setval('orders_id_seq',          COALESCE((SELECT MAX(id) FROM orders),          1));
SELECT setval('inventory_id_seq',       COALESCE((SELECT MAX(id) FROM inventory),       1));
SELECT setval('order_items_id_seq',     COALESCE((SELECT MAX(id) FROM order_items),     1));
SELECT setval('payments_id_seq',        COALESCE((SELECT MAX(id) FROM payments),        1));
SELECT setval('users_id_seq',           COALESCE((SELECT MAX(id) FROM users),           1));

