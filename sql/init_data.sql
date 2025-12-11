-- 选择数据库
USE charging_station_db;

-- 删除现有的表（如果存在）
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS charging_stations;
-- 创建充电站表
CREATE TABLE IF NOT EXISTS charging_stations (
    station_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    address VARCHAR(200) NOT NULL,
    total_sockets INT NOT NULL,
    available_sockets INT NOT NULL,
    power_output DECIMAL(5,2) NOT NULL,
    price_per_hour DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 创建用户表
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

-- 创建预约表
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


-- 插入用户数据
INSERT INTO users (name, email, phone, password, balance) VALUES
('John Doe', 'john@example.com', '13800138001', '123456', 100.0),
('Jane Smith', 'jane@example.com', '13800138002', '123456', 150.0),
('Bob Johnson', 'bob@example.com', '13800138003', '123456', 75.0);

-- 创建索引
CREATE INDEX idx_station_available ON charging_stations(available_sockets);
CREATE INDEX idx_booking_user ON bookings(user_id);
CREATE INDEX idx_booking_station ON bookings(station_id);
CREATE INDEX idx_booking_status ON bookings(status);

-- 插入南京充电站数据
INSERT INTO charging_stations (name, latitude, longitude, address, total_sockets, available_sockets, power_output, price_per_hour, status, description, created_at, updated_at) VALUES
('南京鼓楼充电站', 32.05840000, 118.77750000, '南京市鼓楼区中山路1号', 10, 6, 7.00, 5.50, 'ACTIVE', '市中心充电站，24小时服务', NOW(), NOW()),
('南京玄武充电站', 32.05000000, 118.80000000, '南京市玄武区玄武巷1号', 8, 8, 3.50, 3.50, 'ACTIVE', '玄武湖公园附近，快充服务', NOW(), NOW()),
('南京江宁充电站', 31.95390000, 118.87200000, '南京市江宁区双龙大道1号', 12, 4, 10.00, 5.00, 'ACTIVE', '江宁区主要充电站，快充服务', NOW(), NOW()),
('南京建邺充电站', 32.00430000, 118.73270000, '南京市建邺区江东中路1号', 6, 2, 11.00, 6.50, 'ACTIVE', '建邺区超级充电站', NOW(), NOW()),
('南京浦口充电站', 32.07000000, 118.62000000, '南京市浦口区文德路1号', 4, 4, 7.00, 4.50, 'ACTIVE', '浦口区充电站，24小时服务', NOW(), NOW()),
('南京雨花台充电站', 31.99160000, 118.77000000, '南京市雨花台区软件大道1号', 8, 5, 7.50, 5.20, 'ACTIVE', '软件谷附近，上班族便利', NOW(), NOW()),
('南京栖霞充电站', 32.12000000, 118.88000000, '南京市栖霞区仙林大学城', 6, 3, 3.50, 3.20, 'ACTIVE', '大学城充电站，学生优惠', NOW(), NOW()),
('南京秦淮充电站', 32.02000000, 118.79000000, '南京市秦淮区夫子庙附近', 5, 1, 5.00, 4.80, 'ACTIVE', '旅游区充电站，景点附近', NOW(), NOW()),
('南京六合充电站', 32.34000000, 118.84000000, '南京市六合区雄州南路1号', 4, 4, 3.00, 3.00, 'ACTIVE', '六合区充电站', NOW(), NOW()),
('南京溧水充电站', 31.65000000, 119.02000000, '南京市溧水区永阳街道', 6, 2, 7.00, 4.00, 'ACTIVE', '溧水区主要充电站', NOW(), NOW()),
('南京高淳充电站', 31.33000000, 118.89000000, '南京市高淳区淳溪街道', 4, 3, 3.50, 3.50, 'ACTIVE', '高淳区充电站', NOW(), NOW()),
('南京南站充电站', 31.97000000, 118.80000000, '南京南站地下停车场B1层', 15, 7, 11.00, 6.80, 'ACTIVE', '高铁站充电站，交通枢纽', NOW(), NOW()),
('南京禄口机场充电站', 31.74000000, 118.86000000, '南京禄口机场P2停车场', 20, 12, 22.00, 8.50, 'ACTIVE', '机场超级快充站', NOW(), NOW()),
('南京奥体中心充电站', 32.01000000, 118.72000000, '南京奥体中心东停车场', 10, 6, 7.00, 5.50, 'ACTIVE', '体育场馆充电站', NOW(), NOW()),
('南京新街口充电站', 32.04100000, 118.78400000, '新街口德基广场地下停车场', 8, 2, 7.50, 6.00, 'ACTIVE', '市中心商业区充电站', NOW(), NOW()),
('南京河西CBD充电站', 32.00400000, 118.72600000, '河西中央商务区', 12, 5, 10.00, 6.20, 'ACTIVE', '商务区充电站，工作日高峰', NOW(), NOW()),
('南京江北新区充电站', 32.16000000, 118.69000000, '江北新区产业技术研创园', 10, 8, 7.00, 4.80, 'ACTIVE', '新区充电站，科技园区', NOW(), NOW()),
('南京鼓楼医院充电站', 32.05500000, 118.78000000, '鼓楼医院停车场', 6, 4, 3.50, 4.00, 'ACTIVE', '医院附近充电站', NOW(), NOW()),
('南京中山陵充电站', 32.06000000, 118.85000000, '中山陵景区停车场', 8, 3, 5.00, 5.50, 'ACTIVE', '景区充电站，旅游旺季繁忙', NOW(), NOW()),
('南京火车站充电站', 32.08700000, 118.79600000, '南京火车站北广场', 10, 5, 7.00, 5.80, 'ACTIVE', '火车站充电站，交通便利', NOW(), NOW());