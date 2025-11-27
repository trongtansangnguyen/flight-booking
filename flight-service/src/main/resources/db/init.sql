CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Table: airports
CREATE TABLE IF NOT EXISTS airports (
    airport_id UUID PRIMARY KEY,
    code VARCHAR(10) NOT NULL,
    name VARCHAR(255) NOT NULL,
    city VARCHAR(100),
    country VARCHAR(100),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_airport_code ON airports(code);
CREATE INDEX idx_airport_city ON airports(city);

-- Table: aircrafts
CREATE TABLE IF NOT EXISTS aircrafts (
    aircraft_id UUID PRIMARY KEY,
    model VARCHAR(100) NOT NULL,
    name VARCHAR(255) NOT NULL,
    capacity INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_aircraft_model ON aircrafts(model);
CREATE INDEX idx_aircraft_capacity ON aircrafts(capacity);

-- Table: flights
CREATE TABLE IF NOT EXISTS flights (
    flight_id UUID PRIMARY KEY,
    flight_number VARCHAR(20) NOT NULL,
    origin_airport_id UUID NOT NULL,
    dest_airport_id UUID NOT NULL,
    departure_time TIMESTAMP NOT NULL,
    arrival_time TIMESTAMP NOT NULL,
    aircraft_id UUID NOT NULL,
    base_price DECIMAL(10,2) NOT NULL,
    available_seats INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_flight_origin_airport FOREIGN KEY (origin_airport_id) REFERENCES airports(airport_id),
    CONSTRAINT fk_flight_dest_airport FOREIGN KEY (dest_airport_id) REFERENCES airports(airport_id),
    CONSTRAINT fk_flight_aircraft FOREIGN KEY (aircraft_id) REFERENCES aircrafts(aircraft_id)
);

CREATE INDEX idx_flight_number ON flights(flight_number);
CREATE INDEX idx_flight_origin ON flights(origin_airport_id);
CREATE INDEX idx_flight_destination ON flights(dest_airport_id);
CREATE INDEX idx_flight_departure_time ON flights(departure_time);
CREATE INDEX idx_flight_status ON flights(status);