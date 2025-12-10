-- Create charging stations table
CREATE TABLE IF NOT EXISTS charging_stations (
    station_id INT PRIMARY KEY AUTO_INCREMENT,
    location VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    max_duration INT NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20) NULL,
    password VARCHAR(100) NOT NULL DEFAULT '123456',
    balance DECIMAL(10, 2) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Create bookings table
CREATE TABLE IF NOT EXISTS bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    station_id INT NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    total_cost DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (station_id) REFERENCES charging_stations(station_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Insert charging station data (Beijing coordinates)
INSERT INTO charging_stations (location, price, max_duration, latitude, longitude, available) VALUES
('ChaoYangMen', 5.0, 8, 39.9173, 116.4152, TRUE),
('DongZhiMen', 7.0, 12, 39.9496, 116.4352, TRUE),
('JianGuoMen', 4.5, 6, 39.9110, 116.4197, TRUE),
('TianAnMen Square', 6.0, 10, 39.9075, 116.3972, TRUE),
('Forbidden City', 5.5, 8, 39.9246, 116.3967, TRUE);

-- Insert user data
INSERT INTO users (name, email, phone, password, balance) VALUES
('John Doe', 'john@example.com', '13800138001', '123456', 100.0),
('Jane Smith', 'jane@example.com', '13800138002', '123456', 150.0),
('Bob Johnson', 'bob@example.com', '13800138003', '123456', 75.0);

-- Create indexes
CREATE INDEX idx_station_available ON charging_stations(available);
CREATE INDEX idx_booking_user ON bookings(user_id);
CREATE INDEX idx_booking_station ON bookings(station_id);
CREATE INDEX idx_booking_status ON bookings(status);