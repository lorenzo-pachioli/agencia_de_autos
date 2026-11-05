-- ================================================================
--  SEED DATA — Agencia de Vehículos
--  Orden de ejecución: usuarios → marcas → modelos → vehiculos → imagenes_vehiculo → transaccion → auditoria_transaccion → favoritos
-- ================================================================

-- ----------------------------------------------------------------
-- 1. USUARIOS
-- ----------------------------------------------------------------
INSERT INTO usuario (activo, alta, apellido, email, nombre, password, rol_usuario) VALUES (true, CURRENT_DATE, 'Admin', 'admin_test@gmail.com', 'Administrador', '$2a$10$yCOJQwn.XSnYhaIjpTeGneOgAdq3XLdP5it7Vwy9x1EKY8N9np6MC', 'ADMINISTRADOR');
INSERT INTO usuario (activo, alta, apellido, email, nombre, password, rol_usuario) VALUES (true, CURRENT_DATE, 'García', 'carlos.garcia@agencia.com', 'Carlos', '$2a$10$yCOJQwn.XSnYhaIjpTeGneOgAdq3XLdP5it7Vwy9x1EKY8N9np6MC', 'VENDEDOR');
INSERT INTO usuario (activo, alta, apellido, email, nombre, password, rol_usuario) VALUES (true, CURRENT_DATE, 'Fernández', 'lucia.fernandez@agencia.com', 'Lucía', '$2a$10$yCOJQwn.XSnYhaIjpTeGneOgAdq3XLdP5it7Vwy9x1EKY8N9np6MC', 'VENDEDOR');
INSERT INTO usuario (activo, alta, apellido, email, nombre, password, rol_usuario) VALUES (true, CURRENT_DATE, 'Pérez', 'juan.perez@gmail.com', 'Juan', '$2a$10$yCOJQwn.XSnYhaIjpTeGneOgAdq3XLdP5it7Vwy9x1EKY8N9np6MC', 'USUARIO');
INSERT INTO usuario (activo, alta, apellido, email, nombre, password, rol_usuario) VALUES (true, CURRENT_DATE, 'Gómez', 'maria.gomez@gmail.com', 'María', '$2a$10$yCOJQwn.XSnYhaIjpTeGneOgAdq3XLdP5it7Vwy9x1EKY8N9np6MC', 'USUARIO');
INSERT INTO usuario (activo, alta, apellido, email, nombre, password, rol_usuario) VALUES (true, CURRENT_DATE, 'Rodríguez', 'roberto.rodriguez@gmail.com', 'Roberto', '$2a$10$yCOJQwn.XSnYhaIjpTeGneOgAdq3XLdP5it7Vwy9x1EKY8N9np6MC', 'USUARIO');

-- ----------------------------------------------------------------
-- 2. MARCAS
-- ----------------------------------------------------------------
INSERT INTO marcas (id_marca, activo, nombre) VALUES (1, true, 'Toyota');
INSERT INTO marcas (id_marca, activo, nombre) VALUES (2, true, 'Ford');
INSERT INTO marcas (id_marca, activo, nombre) VALUES (3, true, 'Volkswagen');
INSERT INTO marcas (id_marca, activo, nombre) VALUES (4, true, 'Chevrolet');
INSERT INTO marcas (id_marca, activo, nombre) VALUES (5, true, 'Honda');
INSERT INTO marcas (id_marca, activo, nombre) VALUES (6, true, 'Renault');
INSERT INTO marcas (id_marca, activo, nombre) VALUES (7, true, 'Peugeot');
INSERT INTO marcas (id_marca, activo, nombre) VALUES (8, true, 'Fiat');

-- ----------------------------------------------------------------
-- 3. MODELOS
-- ----------------------------------------------------------------
INSERT INTO modelos (id_modelo, activo, anio, nombre, tipo_modelo, id_marca) VALUES (1, true, 2022, 'Corolla', 'AUTO', 1);
INSERT INTO modelos (id_modelo, activo, anio, nombre, tipo_modelo, id_marca) VALUES (2, true, 2023, 'Hilux', 'CAMIONETA', 1);
INSERT INTO modelos (id_modelo, activo, anio, nombre, tipo_modelo, id_marca) VALUES (3, true, 2023, 'RAV4', 'SUV', 1);
INSERT INTO modelos (id_modelo, activo, anio, nombre, tipo_modelo, id_marca) VALUES (4, true, 2021, 'Focus', 'AUTO', 2);
INSERT INTO modelos (id_modelo, activo, anio, nombre, tipo_modelo, id_marca) VALUES (5, true, 2022, 'Ranger', 'CAMIONETA', 2);
INSERT INTO modelos (id_modelo, activo, anio, nombre, tipo_modelo, id_marca) VALUES (6, true, 2022, 'Golf', 'AUTO', 3);
INSERT INTO modelos (id_modelo, activo, anio, nombre, tipo_modelo, id_marca) VALUES (7, true, 2021, 'Cruze', 'AUTO', 4);
INSERT INTO modelos (id_modelo, activo, anio, nombre, tipo_modelo, id_marca) VALUES (8, true, 2023, 'Civic', 'AUTO', 5);
INSERT INTO modelos (id_modelo, activo, anio, nombre, tipo_modelo, id_marca) VALUES (9, true, 2022, 'Duster', 'SUV', 6);
INSERT INTO modelos (id_modelo, activo, anio, nombre, tipo_modelo, id_marca) VALUES (10, true, 2023, 'Cronos', 'AUTO', 8);

-- ----------------------------------------------------------------
-- 4. VEHICULOS
-- ----------------------------------------------------------------
INSERT INTO vehiculos (id_vehiculo, anio, color, created_at, descripcion, estado, kilometraje, patente, precio_adquisicion, precio_venta, tipo_combustible, tipo_transmision, updated_at, id_modelo) VALUES (1, 2022, 'Blanco', CURRENT_TIMESTAMP, 'Toyota Corolla XEI 2022. Único dueño, service al día. Tapizado en cuero.', 'VENDIDO', 25000, 'AB123CD', 18000000, 22500000, 'NAFTA', 'AUTOMATICA', CURRENT_TIMESTAMP, 1);
INSERT INTO vehiculos (id_vehiculo, anio, color, created_at, descripcion, estado, kilometraje, patente, precio_adquisicion, precio_venta, tipo_combustible, tipo_transmision, updated_at, id_modelo) VALUES (2, 2023, 'Gris Oscuro', CURRENT_TIMESTAMP, 'Toyota Hilux SRX 2023. Cabina doble, 4x4, equipo completo.', 'RESERVADO', 15000, 'CD456EF', 36000000, 44000000, 'DIESEL', 'AUTOMATICA', CURRENT_TIMESTAMP, 2);
INSERT INTO vehiculos (id_vehiculo, anio, color, created_at, descripcion, estado, kilometraje, patente, precio_adquisicion, precio_venta, tipo_combustible, tipo_transmision, updated_at, id_modelo) VALUES (3, 2023, 'Negro', CURRENT_TIMESTAMP, 'Toyota RAV4 Hybrid 2023. Tracción integral eléctrica, modo EV.', 'DISPONIBLE', 8000, 'EF789GH', 46000000, 56000000, 'HIBRIDO', 'AUTOMATICA', CURRENT_TIMESTAMP, 3);
INSERT INTO vehiculos (id_vehiculo, anio, color, created_at, descripcion, estado, kilometraje, patente, precio_adquisicion, precio_venta, tipo_combustible, tipo_transmision, updated_at, id_modelo) VALUES (4, 2021, 'Rojo', CURRENT_TIMESTAMP, 'Ford Focus S 2021. Motor 2.0 nafta, caja manual 6 velocidades.', 'DISPONIBLE', 40000, 'GH012IJ', 11500000, 15000000, 'NAFTA', 'MANUAL', CURRENT_TIMESTAMP, 4);
INSERT INTO vehiculos (id_vehiculo, anio, color, created_at, descripcion, estado, kilometraje, patente, precio_adquisicion, precio_venta, tipo_combustible, tipo_transmision, updated_at, id_modelo) VALUES (5, 2022, 'Blanco', CURRENT_TIMESTAMP, 'Ford Ranger XLT 2022. Cabina doble, motor diesel 2.2, 4x4.', 'VENDIDO', 30000, 'IJ345KL', 31000000, 38000000, 'DIESEL', 'AUTOMATICA', CURRENT_TIMESTAMP, 5);
INSERT INTO vehiculos (id_vehiculo, anio, color, created_at, descripcion, estado, kilometraje, patente, precio_adquisicion, precio_venta, tipo_combustible, tipo_transmision, updated_at, id_modelo) VALUES (6, 2022, 'Plata', CURRENT_TIMESTAMP, 'Volkswagen Golf Comfortline 2022. Motor 1.4 TSI 150 CV, DSG.', 'DISPONIBLE', 18000, 'KL678MN', 23000000, 28500000, 'NAFTA', 'AUTOMATICA', CURRENT_TIMESTAMP, 6);
INSERT INTO vehiculos (id_vehiculo, anio, color, created_at, descripcion, estado, kilometraje, patente, precio_adquisicion, precio_venta, tipo_combustible, tipo_transmision, updated_at, id_modelo) VALUES (7, 2021, 'Azul Metalizado', CURRENT_TIMESTAMP, 'Chevrolet Cruze LTZ 2021. Motor nafta 1.4 turbo, automática.', 'DISPONIBLE', 38000, 'MN901OP', 13500000, 17500000, 'NAFTA', 'AUTOMATICA', CURRENT_TIMESTAMP, 7);
INSERT INTO vehiculos (id_vehiculo, anio, color, created_at, descripcion, estado, kilometraje, patente, precio_adquisicion, precio_venta, tipo_combustible, tipo_transmision, updated_at, id_modelo) VALUES (8, 2023, 'Negro', CURRENT_TIMESTAMP, 'Honda Civic EX-L 2023. Motor 1.5 VTEC Turbo, sunroof panorámico.', 'DISPONIBLE', 5000, 'OP234QR', 29000000, 35500000, 'NAFTA', 'AUTOMATICA', CURRENT_TIMESTAMP, 8);
INSERT INTO vehiculos (id_vehiculo, anio, color, created_at, descripcion, estado, kilometraje, patente, precio_adquisicion, precio_venta, tipo_combustible, tipo_transmision, updated_at, id_modelo) VALUES (9, 2022, 'Gris', CURRENT_TIMESTAMP, 'Renault Duster Outsider 2022. Motor diesel 1.5 dCi, 4x4.', 'DISPONIBLE', 22000, 'QR567ST', 20000000, 25500000, 'DIESEL', 'MANUAL', CURRENT_TIMESTAMP, 9);
INSERT INTO vehiculos (id_vehiculo, anio, color, created_at, descripcion, estado, kilometraje, patente, precio_adquisicion, precio_venta, tipo_combustible, tipo_transmision, updated_at, id_modelo) VALUES (10, 2023, 'Blanco', CURRENT_TIMESTAMP, 'Fiat Cronos Drive 2023. Motor 1.3 FireFly nafta, manual.', 'DISPONIBLE', 12000, 'ST890UV', 10500000, 13500000, 'NAFTA', 'MANUAL', CURRENT_TIMESTAMP, 10);

-- ----------------------------------------------------------------
-- 5. IMAGENES VEHICULO
--    1 imagen principal + 3 secundarias por vehiculo (URLs de fotos reales
--    de la web, hotlinkeadas; no son de uso libre: sirven para desarrollo/demo).
--    Si alguna URL deja de cargar, se reemplaza con un UPDATE por id.
-- ----------------------------------------------------------------

-- 1 · Toyota Corolla 2022 (Blanco)
--     Fuentes: AutoScout24 (Corolla 2022 blanco) + Zigwheels. Orden: principal, 3 secundarias.
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (true,  'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTtcOORrFCwG_7WJ29s7IBJSPPAvFcr10H6iXtY_215ZA&s=10', 1);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://imagecdn.zigwheels.ae/large/gallery/exterior/40/417/toyota-corolla-front-angle-low-view-931780.jpg?tr=w-930', 1);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://prod.pictures.autoscout24.net/listing-images/169b1fd0-dc07-425a-affe-638b633cd4d6_17ec5585-6625-4b61-9c0d-5ec7207c2731.jpg/1920x1080.webp', 1);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://imagecdn.zigwheels.ae/large/gallery/interior/40/417/toyota-corolla-dashboard-view-806093.jpg?tr=w-930', 1);

-- 2 · Toyota Hilux 2023 (Gris Oscuro, cabina doble)
--     Fuente: Autocar India (Hilux). Frente, lateral, trasera, interior.
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (true,  'https://cdn-s3.autocarindia.com/Toyota/Hilux/2026_Toyota_Hilux_Front_Quarter_Static.jpg', 2);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://cdn-s3.autocarindia.com/Toyota/Hilux/2026_Toyota_Hilux_Side_Tracking.jpg', 2);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://cdn-s3.autocarindia.com/Toyota/Hilux/2026_Toyota_Hilux_Rear_Quarter_Static.jpg', 2);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://cdn-s3.autocarindia.com/Toyota/Hilux/2026_Toyota_Hilux_Interior.jpg', 2);

-- 3 · Toyota RAV4 Hybrid 2023 (Negro)
--     Fuente: Sayarabay (RAV4 HEV). Frente, trasera lateral, trasera, interior.
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (true,  'https://imgcdn.sayaratbay.com/medium/gallery/exterior/40/1675/toyota-rav-4-hev-full-front-view-926803.jpg', 3);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://imgcdn.sayaratbay.com/medium/gallery/exterior/40/1675/toyota-rav-4-hev-rear-medium-side-view-530466.jpg', 3);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://imgcdn.sayaratbay.com/medium/gallery/exterior/40/1675/toyota-rav-4-hev-rear-cross-view-993654.jpg', 3);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://imgcdn.sayaratbay.com/medium/gallery/interior/40/1675/toyota-rav-4-hev-dashboard-view-941454.jpg', 3);

-- 4 · Ford Focus 2021 (Rojo)
--     Fuente: Zigwheels (Focus). Frente, lateral, trasera, interior.
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (true,  'https://imagecdn.zigwheels.ae/large/gallery/exterior/12/112/ford-focus-front-angle-low-view-247844.jpg?tr=w-930', 4);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://imagecdn.zigwheels.ae/large/gallery/exterior/12/112/ford-focus-side-view-439448.jpg?tr=w-930', 4);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://imagecdn.zigwheels.ae/large/gallery/exterior/12/112/ford-focus-rear-angle-view-591108.jpg?tr=w-930', 4);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://imagecdn.zigwheels.ae/large/gallery/interior/12/112/ford-focus-stereo-view-862014.jpg?tr=w-930', 4);

-- 5 · Ford Ranger XLT 2022 (Blanco)
--     Fuente: AutoDeal (nota sobre la Ranger XLT 2022). Fotos del mismo modelo/año.
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (true,  'https://d1hv7ee95zft1i.cloudfront.net/custom/blog-post/original/2022-ford-ranger-xlt-62a1b9ff4f411.jpg', 5);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://d1hv7ee95zft1i.cloudfront.net/custom/blog-post-photo/gallery/ford-ranger-exterior-62a1bd0ddc8b2.jpg', 5);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://d1hv7ee95zft1i.cloudfront.net/custom/blog-post-photo/gallery/ford-ranger-xlt-driving-and-handling-62a1bbe19f39b.jpg', 5);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://d1hv7ee95zft1i.cloudfront.net/custom/blog-post-photo/gallery/ford-ranger-xlt-interior-passenger-seat-62a1bbbc7dd45.jpg', 5);

-- 6 · Volkswagen Golf 2022 (Plata)
--     Fuente: Zigwheels. OJO: son fotos del Golf GTI (misma generacion), no del Comfortline.
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (true,  'https://imagecdn.zigwheels.ae/large/gallery/exterior/41/1182/volkswagen-golf-gti-front-angle-low-view-676498.jpg?tr=w-930', 6);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://imagecdn.zigwheels.ae/large/gallery/exterior/41/1182/volkswagen-golf-gti-side-view-856051.jpg?tr=w-930', 6);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://imagecdn.zigwheels.ae/large/gallery/exterior/41/1182/volkswagen-golf-gti-rear-angle-view-144784.jpg?tr=w-930', 6);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://imagecdn.zigwheels.ae/large/gallery/interior/41/1182/volkswagen-golf-gti-dashboard-view-204039.jpg?tr=w-930', 6);

-- 7 · Chevrolet Cruze 2021 (Azul Metalizado)
--     Fuente: Perfil/Parabrisas (lanzamiento Cruze 2021 Argentina). Angulos de las fotos 2-4 sin verificar.
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (true,  'https://fotos.perfil.com/2021/01/21/chevrolet-cruze-1117286.jpg', 7);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://fotos.perfil.com/2021/01/21/chevrolet-cruze-1117290.jpg', 7);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://fotos.perfil.com/2021/01/21/chevrolet-cruze-1117289.jpg', 7);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://fotos.perfil.com/2021/01/21/chevrolet-cruze-1117288.jpg', 7);

-- 8 · Honda Civic 2023 (Negro)
--     Fuente: Zigwheels (Civic 2022-2023). Frente, lateral, trasera (luz), interior.
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (true,  'https://imagecdn.zigwheels.ae/large/gallery/exterior/14/132/honda-civic-front-angle-low-view-781258.jpg?tr=w-930', 8);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://imagecdn.zigwheels.ae/large/gallery/exterior/14/132/honda-civic-front-side-view-763437.jpg?tr=w-930', 8);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://imagecdn.zigwheels.ae/large/gallery/exterior/14/132/honda-civic-tail-light-644115.jpg?tr=w-930', 8);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://imagecdn.zigwheels.ae/large/gallery/interior/14/132/honda-civic-dashboard-view-301286.jpg?tr=w-930', 8);

-- 9 · Renault Duster 2022 (Gris)
--     Fuente: Sayarabay (Duster 2022-2024). Frente, lateral, trasera, interior.
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (true,  'https://imgcdn.sayaratbay.com/medium/gallery/exterior/33/1086/renault-duster-front-medium-view-376682.jpg', 9);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://imgcdn.sayaratbay.com/medium/gallery/exterior/33/1086/renault-duster-side-view-586782.jpg', 9);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://imgcdn.sayaratbay.com/medium/gallery/exterior/33/1086/renault-duster-rear-angle-view-631190.jpg', 9);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://imgcdn.sayaratbay.com/medium/gallery/interior/33/1086/renault-duster-dashboard-view-312982.jpg', 9);

-- 10 · Fiat Cronos 2023 (Blanco)
--     Fuente: Perfil/Parabrisas (Cronos Precision 2023 Argentina). Angulos de las fotos 2-4 sin verificar.
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (true,  'https://fotos.perfil.com/2022/07/21/fiat-cronos-precision-2023-1389802.jpg', 10);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://fotos.perfil.com/2022/07/21/fiat-cronos-precision-2023-1389808.jpg', 10);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://fotos.perfil.com/2022/07/21/fiat-cronos-precision-2023-1389809.jpg', 10);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://fotos.perfil.com/2022/07/21/fiat-cronos-precision-2023-1389810.jpg', 10);

-- ----------------------------------------------------------------
-- 6. TRANSACCIONES
-- ----------------------------------------------------------------
INSERT INTO transaccion (id, comision_calculada, created_at, estado_transaccion, metodo_pago, observaciones, precio_final, updated_at, cliente_id, vendedor_id, vehiculo_id) VALUES (1, 1125000, CURRENT_DATE, 'SENIADO', 'EFECTIVO', 'Operación concretada en pago efectivo.', 22500000, CURRENT_DATE, 4, 2, 1);
INSERT INTO transaccion (id, comision_calculada, created_at, estado_transaccion, metodo_pago, observaciones, precio_final, updated_at, cliente_id, vendedor_id, vehiculo_id) VALUES (2, 0, CURRENT_DATE, 'RESERVA', 'TRANSFERENCIA', 'Cliente entregó seña por transferencia bancaria.', 44000000, CURRENT_DATE, 5, 3, 2);
INSERT INTO transaccion (id, comision_calculada, created_at, estado_transaccion, metodo_pago, observaciones, precio_final, updated_at, cliente_id, vendedor_id, vehiculo_id) VALUES (3, 1900000, CURRENT_DATE, 'SENIADO', 'PLAN_AHORRO', 'Vendido mediante crédito prendario bancario.', 38000000, CURRENT_DATE, 6, 2, 5);
INSERT INTO transaccion (id, comision_calculada, created_at, estado_transaccion, metodo_pago, observaciones, precio_final, updated_at, cliente_id, vendedor_id, vehiculo_id) VALUES (4, 0, CURRENT_DATE, 'CANCELADO', 'CUOTA_BANCARIZADA', 'Reserva cancelada por crédito rechazado.', 56000000, CURRENT_DATE, 4, 3, 3);

-- ----------------------------------------------------------------
-- 7. AUDITORIA TRANSACCION
-- ----------------------------------------------------------------
INSERT INTO auditoria_transaccion (id, created_at, estado_anterior, estado_nuevo, metodo_pago_anterior, metodo_pago_nuevo, precio_final_anterior, precio_final_nuevo, transaccion_id, vendedor_id) VALUES (1, CURRENT_DATE, 'RESERVA', 'SENIADO', 'EFECTIVO', 'EFECTIVO', 23000000, 22500000, 1, 2);
INSERT INTO auditoria_transaccion (id, created_at, estado_anterior, estado_nuevo, metodo_pago_anterior, metodo_pago_nuevo, precio_final_anterior, precio_final_nuevo, transaccion_id, vendedor_id) VALUES (2, CURRENT_DATE, 'SENIADO', 'VENDIDO', 'EFECTIVO', 'EFECTIVO', 22500000, 22500000, 1, 2);
INSERT INTO auditoria_transaccion (id, created_at, estado_anterior, estado_nuevo, metodo_pago_anterior, metodo_pago_nuevo, precio_final_anterior, precio_final_nuevo, transaccion_id, vendedor_id) VALUES (3, CURRENT_DATE, 'RESERVA', 'SENIADO', 'TRANSFERENCIA', 'TRANSFERENCIA', 44000000, 44000000, 2, 3);
INSERT INTO auditoria_transaccion (id, created_at, estado_anterior, estado_nuevo, metodo_pago_anterior, metodo_pago_nuevo, precio_final_anterior, precio_final_nuevo, transaccion_id, vendedor_id) VALUES (4, CURRENT_DATE, 'RESERVA', 'CANCELADO', 'PLAN_AHORRO', 'PLAN_AHORRO', 56000000, 56000000, 4, 3);

-- ----------------------------------------------------------------
-- 8. FAVORITOS
-- ----------------------------------------------------------------
INSERT INTO favoritos (id, usuario_id, vehiculo_id) VALUES (1, 4, 3);
INSERT INTO favoritos (id, usuario_id, vehiculo_id) VALUES (2, 5, 8);
INSERT INTO favoritos (id, usuario_id, vehiculo_id) VALUES (3, 6, 6);