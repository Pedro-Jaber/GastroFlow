-- CREATE TABLE IF NOT EXISTS address (
--     id SERIAL PRIMARY KEY,
--     street VARCHAR(255),
--     number VARCHAR(50),
--     city VARCHAR(100),
--     cep VARCHAR(20),
--     complement VARCHAR(255),
--     state VARCHAR(100)
-- );

-- CREATE TABLE IF NOT EXISTS users (
--     id SERIAL PRIMARY KEY,
--     name VARCHAR(255) NOT NULL,
--     email VARCHAR(255) UNIQUE NOT NULL,
--     login VARCHAR(255) UNIQUE NOT NULL,
--     password VARCHAR(255) NOT NULL,
--     address_id INTEGER,
--     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
--     user_type VARCHAR(50) NOT NULL,
--     FOREIGN KEY (address_id) REFERENCES address(id)
-- );

-- CREATE TABLE IF NOT EXISTS customers (
-- 	user_id BIGINT PRIMARY KEY,
--     cpf VARCHAR(255) UNIQUE,
--     phone VARCHAR(255),
--     CONSTRAINT fk_customer_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
-- );

-- CREATE TABLE IF NOT EXISTS restaurant_owners (
-- 	user_id BIGINT PRIMARY KEY,
--     cpf_cnpj VARCHAR(255) UNIQUE,
--     phone VARCHAR(255),
--     CONSTRAINT fk_customer_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
-- );

-- Inserindo endereços
INSERT INTO address (street, number, city, cep, complement, state) VALUES
('Rua das Flores', '123', 'São Paulo', '01310100', 'Apto 42', 'SP'),
('Av. Paulista', '1578', 'São Paulo', '01310200', NULL, 'SP'),
('Rua XV de Novembro', '500', 'Curitiba', '80020310', 'Sala 5', 'PR'),
('Av. Beira Mar Norte', '3300', 'Florianópolis', '88036002', NULL, 'SC'),
('Rua da Consolação', '88', 'São Paulo', '01301000', 'Casa', 'SP')
ON CONFLICT DO NOTHING;

-- Inserindo usuários (senha: "senha123" em texto puro — troque por hash em produção)
INSERT INTO users (name, email, login, password, address_id, user_type) VALUES
('Ana Souza',         'ana.souza@email.com',       'ana.souza',       'senha123', 1, 'CUSTOMER'),
('Bruno Lima',        'bruno.lima@email.com',       'bruno.lima',      'qwer12!awes', 2, 'CUSTOMER'),
('Carla Mendes',      'carla.mendes@email.com',     'carla.mendes',    'password456', 3, 'CUSTOMER'),
('Restaurante Bom Sabor', 'bomsabor@email.com',     'bomsabor',        'pass1!!23', 4, 'RESTAURANT_OWNER'),
('Cantina da Nonna',  'cantinanonna@email.com',     'cantinanonna',    'abc123qwe', 5, 'RESTAURANT_OWNER')
ON CONFLICT DO NOTHING;

-- Inserindo customers (user_id 1, 2, 3)
INSERT INTO customers (user_id, cpf, phone) VALUES
(1, '11122233344', '11912345678'),
(2, '22233344455', '11987654321'),
(3, '33344455566', '41998765432')
ON CONFLICT DO NOTHING;

-- Inserindo restaurant_owners (user_id 4, 5)
INSERT INTO restaurant_owners (user_id, cpf_cnpj, phone) VALUES
(4, '12345678000190', '48934567890'),
(5, '98765432000110', '11945678901')
ON CONFLICT DO NOTHING;


-- SELECT 
-- 	u.id,
-- 	u.name,
-- 	u.email,
-- 	u.login,
-- 	u.password,
-- 	u.address_id,
-- 	u.user_type,
-- 	c.cpf,
-- 	ro.cpf_cnpj,
-- 	c.phone,
-- 	ro.phone,
-- 	u.updated_at
-- FROM users u
-- LEFT JOIN customers c ON c.user_id = u.id
-- LEFT JOIN restaurant_owners ro ON ro.user_id = u.id
-- ORDER BY u.id