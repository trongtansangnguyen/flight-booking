TRUNCATE TABLE flights RESTART IDENTITY CASCADE;
TRUNCATE TABLE aircrafts RESTART IDENTITY CASCADE;
TRUNCATE TABLE airports RESTART IDENTITY CASCADE;

-- Insert 100 random airports
INSERT INTO airports (airport_id, code, name, city, country, created_at, updated_at)
SELECT 
    gen_random_uuid(),
    'AP' || LPAD(i::text, 3, '0'),
    'Airport ' || i,
    'City ' || i,
    CASE (i % 5)
        WHEN 0 THEN 'Vietnam'
        WHEN 1 THEN 'Thailand'
        WHEN 2 THEN 'Singapore'
        WHEN 3 THEN 'Malaysia'
        ELSE 'Indonesia'
    END,
    NOW() - (RANDOM() * INTERVAL '365 days'),
    NOW() - (RANDOM() * INTERVAL '30 days')
FROM generate_series(1, 100) AS i;

-- Insert 100 random aircrafts
INSERT INTO aircrafts (aircraft_id, model, name, capacity, created_at, updated_at)
SELECT 
    gen_random_uuid(),
    CASE (i % 10)
        WHEN 0 THEN 'Airbus A320'
        WHEN 1 THEN 'Airbus A321'
        WHEN 2 THEN 'Airbus A330'
        WHEN 3 THEN 'Boeing 737'
        WHEN 4 THEN 'Boeing 777'
        WHEN 5 THEN 'Boeing 787'
        WHEN 6 THEN 'Embraer E190'
        WHEN 7 THEN 'ATR 72'
        WHEN 8 THEN 'Airbus A350'
        ELSE 'Boeing 747'
    END,
    'AC-' || LPAD(i::text, 4, '0'),
    100 + (RANDOM() * 300)::int,
    NOW() - (RANDOM() * INTERVAL '365 days'),
    NOW() - (RANDOM() * INTERVAL '30 days')
FROM generate_series(1, 100) AS i;

-- Insert 100 random flights
-- Note: This uses airports and aircrafts that were just inserted
INSERT INTO flights (
    flight_id, flight_number, origin_airport_id, dest_airport_id,
    departure_time, arrival_time, aircraft_id, base_price,
    available_seats, status, created_at, updated_at
)
SELECT 
    gen_random_uuid(),
    'VN' || LPAD(i::text, 4, '0'),
    (SELECT airport_id FROM airports ORDER BY RANDOM() LIMIT 1),
    (SELECT airport_id FROM airports ORDER BY RANDOM() LIMIT 1),
    NOW() + (RANDOM() * INTERVAL '30 days'),
    NOW() + (RANDOM() * INTERVAL '30 days') + (RANDOM() * INTERVAL '8 hours'),
    (SELECT aircraft_id FROM aircrafts ORDER BY RANDOM() LIMIT 1),
    500000 + (RANDOM() * 5000000),
    (SELECT capacity FROM aircrafts ORDER BY RANDOM() LIMIT 1) - (RANDOM() * 50)::int,
    CASE (i % 3)
        WHEN 0 THEN 'SCHEDULED'
        WHEN 1 THEN 'DELAYED'
        ELSE 'CANCELLED'
    END,
    NOW() - (RANDOM() * INTERVAL '365 days'),
    NOW() - (RANDOM() * INTERVAL '30 days')
FROM generate_series(1, 100) AS i;

