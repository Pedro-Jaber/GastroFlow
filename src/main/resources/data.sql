CREATE TABLE IF NOT EXISTS address (
    id SERIAL PRIMARY KEY,
    street VARCHAR(255),
    number VARCHAR(50),
    city VARCHAR(100),
    cep VARCHAR(20),
    complement VARCHAR(255),
    state VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    login VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    address_id INTEGER,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_type VARCHAR(50) NOT NULL,
    FOREIGN KEY (address_id) REFERENCES address(id)
);

CREATE TABLE IF NOT EXISTS customers (
	user_id BIGINT PRIMARY KEY,
    cpf VARCHAR(255) UNIQUE,
    phone VARCHAR(255),
    CONSTRAINT fk_customer_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS restaurant_owners (
	user_id BIGINT PRIMARY KEY,
    cpf_cnpj VARCHAR(255) UNIQUE,
    phone VARCHAR(255),
    CONSTRAINT fk_customer_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

INSERT INTO users (name, email, login, password, user_type) VALUES
('Admin User', 'test@test.com', 'admin', 'password123', 'CUSTOMER');

SELECT 
	u.id,
	u.name,
	u.email,
	u.login,
	u.password,
	u.address_id,
	u.user_type,
	c.cpf,
	ro.cpf_cnpj,
	c.phone,
	ro.phone,
	u.updated_at
FROM users u
LEFT JOIN customers c ON c.user_id = u.id
LEFT JOIN restaurant_owners ro ON ro.user_id = u.id
ORDER BY u.id