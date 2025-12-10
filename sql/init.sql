-- 创建数据库
CREATE DATABASE IF NOT EXISTS charging_station_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE charging_station_db;

-- 创建充电站表
CREATE TABLE IF NOT EXISTS charging_stations (
    station_id INT PRIMARY KEY AUTO_INCREMENT,
    location VARCHAR(100) NOT NULL COMMENT '充电站地点',
    price DECIMAL(10, 2) NOT NULL COMMENT '价格/小时',
    max_duration INT NOT NULL COMMENT '最长充电时长（小时）',
    latitude DOUBLE NOT NULL COMMENT '纬度',
    longitude DOUBLE NOT NULL COMMENT '经度',
    available BOOLEAN DEFAULT TRUE COMMENT '是否可用',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '用户名称',
    email VARCHAR(100) UNIQUE NOT NULL COMMENT '邮箱',
    balance DECIMAL(10, 2) DEFAULT 0 COMMENT '账户余额',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建预约表
CREATE TABLE IF NOT EXISTS bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL COMMENT '用户ID',
    station_id INT NOT NULL COMMENT '充电站ID',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME NOT NULL COMMENT '结束时间',
    total_cost DECIMAL(10, 2) NOT NULL COMMENT '总费用',
    status VARCHAR(20) DEFAULT 'active' COMMENT '预约状态(active/completed/cancelled)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (station_id) REFERENCES charging_stations(station_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 插入充电站初始数据（北京坐标）
INSERT INTO charging_stations (location, price, max_duration, latitude, longitude, available) VALUES
('朝阳门', 5.0, 8, 39.9173, 116.4152, TRUE),
('东直门', 7.0, 12, 39.9496, 116.4352, TRUE),
('建国门', 4.5, 6, 39.9110, 116.4197, TRUE),
('天安门广场', 6.0, 10, 39.9075, 116.3972, TRUE),
('故宫', 5.5, 8, 39.9246, 116.3967, TRUE);

-- 插入用户初始数据
INSERT INTO users (name, email, balance) VALUES
('John Doe', 'john@example.com', 100.0),
('Jane Smith', 'jane@example.com', 150.0),
('Bob Johnson', 'bob@example.com', 75.0);

-- 创建索引
CREATE INDEX idx_station_available ON charging_stations(available);
CREATE INDEX idx_booking_user ON bookings(user_id);
CREATE INDEX idx_booking_station ON bookings(station_id);
CREATE INDEX idx_booking_status ON bookings(status);
