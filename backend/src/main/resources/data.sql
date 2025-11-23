-- Miasta
INSERT INTO cities (name, postal_code) VALUES
('Wroclaw', '12-345'),
('Zawiercie', '54-321');

-- Restauracje
INSERT INTO restaurants (name, phone, city_id, street, created_at, updated_at) VALUES
('Testowa restauracja', '123456789', 1, 'ul. Testowa 1', NOW(), NOW());

-- Oferty
INSERT INTO offers (title, description, price, available_quantity, status, restaurant_id, created_at, updated_at) VALUES
('Margherita', 'Klasyczna pizza', 20.00, 10, 'ACTIVE', 1, NOW(), NOW()),
('Sushi Mix', 'Zestaw sushi', 50.00, 5, 'ACTIVE', 1, NOW(), NOW());
