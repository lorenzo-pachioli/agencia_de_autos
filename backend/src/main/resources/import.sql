-- ================================================================
--  SEED DATA — Agencia de Vehículos
--  Orden de ejecución: usuarios → marcas → modelos → vehiculos → imagenes_vehiculo → transaccion → auditoria_transaccion → favoritos
-- ================================================================

-- ----------------------------------------------------------------
-- 1. USUARIOS
-- ----------------------------------------------------------------
INSERT INTO usuario (activo, alta, apellido, email, nombre, password, rol_usuario) VALUES (true, CURRENT_DATE, 'Admin', 'admin_test@gmail.com', 'Administrador', '$2a$10$w4PmPgEBGiJmVTGi4ww0H.nGq6bKmWz.RlFQVaGLlVnBx3V1K7I3e', 'ADMINISTRADOR');
INSERT INTO usuario (activo, alta, apellido, email, nombre, password, rol_usuario) VALUES (true, CURRENT_DATE, 'García', 'carlos.garcia@agencia.com', 'Carlos', '$2a$10$w4PmPgEBGiJmVTGi4ww0H.nGq6bKmWz.RlFQVaGLlVnBx3V1K7I3e', 'VENDEDOR');
INSERT INTO usuario (activo, alta, apellido, email, nombre, password, rol_usuario) VALUES (true, CURRENT_DATE, 'Fernández', 'lucia.fernandez@agencia.com', 'Lucía', '$2a$10$w4PmPgEBGiJmVTGi4ww0H.nGq6bKmWz.RlFQVaGLlVnBx3V1K7I3e', 'VENDEDOR');
INSERT INTO usuario (activo, alta, apellido, email, nombre, password, rol_usuario) VALUES (true, CURRENT_DATE, 'Pérez', 'juan.perez@gmail.com', 'Juan', '$2a$10$w4PmPgEBGiJmVTGi4ww0H.nGq6bKmWz.RlFQVaGLlVnBx3V1K7I3e', 'USUARIO');
INSERT INTO usuario (activo, alta, apellido, email, nombre, password, rol_usuario) VALUES (true, CURRENT_DATE, 'Gómez', 'maria.gomez@gmail.com', 'María', '$2a$10$w4PmPgEBGiJmVTGi4ww0H.nGq6bKmWz.RlFQVaGLlVnBx3V1K7I3e', 'USUARIO');
INSERT INTO usuario (activo, alta, apellido, email, nombre, password, rol_usuario) VALUES (true, CURRENT_DATE, 'Rodríguez', 'roberto.rodriguez@gmail.com', 'Roberto', '$2a$10$w4PmPgEBGiJmVTGi4ww0H.nGq6bKmWz.RlFQVaGLlVnBx3V1K7I3e', 'USUARIO');

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
-- ----------------------------------------------------------------
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (true,  'https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?auto=format&fit=crop&w=1200&q=80', 1);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://images.unsplash.com/photo-1549399542-7e3f8b79c341?auto=format&fit=crop&w=1200&q=80', 1);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://images.unsplash.com/photo-1552519507-da3b142c6e3d?auto=format&fit=crop&w=1200&q=80', 1);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (true,  'https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&w=1200&q=80', 2);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://images.unsplash.com/photo-1559416523-140ddc3d238c?auto=format&fit=crop&w=1200&q=80', 2);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (true,  'https://images.unsplash.com/photo-1568605117036-5fe5e7bab0b7?auto=format&fit=crop&w=1200&q=80', 3);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (false, 'https://images.unsplash.com/photo-1541899481282-d53bffe3c35d?auto=format&fit=crop&w=1200&q=80', 3);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (true,  'https://images.unsplash.com/photo-1551830820-330a71b99659?auto=format&fit=crop&w=1200&q=80', 4);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (true,  'https://images.unsplash.com/photo-1583121274602-3e2820c69888?auto=format&fit=crop&w=1200&q=80', 5);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (true,  'https://images.unsplash.com/photo-1542282088-72c9c27ed0cd?auto=format&fit=crop&w=1200&q=80', 6);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (true,  'https://images.unsplash.com/photo-1503376780353-7e6692767b70?auto=format&fit=crop&w=1200&q=80', 7);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (true,  'https://images.unsplash.com/photo-1605559424843-9e4c228bf1c2?auto=format&fit=crop&w=1200&q=80', 8);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (true,  'https://images.unsplash.com/photo-1519641471654-76ce0107ad1b?auto=format&fit=crop&w=1200&q=80', 9);
INSERT INTO imagenes_vehiculo (es_principal, url, id_vehiculo) VALUES (true,  'https://images.unsplash.com/photo-1494976388531-d1058494cdd8?auto=format&fit=crop&w=1200&q=80', 10);

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
