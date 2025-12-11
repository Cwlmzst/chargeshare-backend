# 充电站数据库模式更新说明

## 概述
本文档说明了对充电站系统的数据库模式进行的重大更新，以支持更详细的充电站信息和更精确的可用性跟踪。

## 旧模式 vs 新模式

### 旧充电站表结构
```sql
CREATE TABLE charging_stations (
    station_id INT PRIMARY KEY AUTO_INCREMENT,
    location VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    max_duration INT NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    available BOOLEAN DEFAULT TRUE
);
```

### 新充电站表结构
```sql
CREATE TABLE charging_stations (
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
    description TEXT
);
```

## 主要变更

1. **字段重命名**:
   - `location` → `name`
   - `price` → `price_per_hour`
   - `available` → `available_sockets` (更精确的跟踪)

2. **新增字段**:
   - `address`: 充电站的具体地址
   - `total_sockets`: 总插座数
   - `available_sockets`: 可用插座数
   - `power_output`: 功率输出(kW)
   - `status`: 充电站状态(ACTIVE/INACTIVE)
   - `description`: 充电站描述

3. **移除字段**:
   - `max_duration`: 不再限制单次充电时长
   - `available`: 替换为基于插座数量的可用性

## 南京充电站数据
已在 `sql/init_data.sql` 中添加了20个南京地区的充电站数据，包括：
- 市中心区域(鼓楼、新街口)
- 交通枢纽(南京南站、禄口机场、火车站)
- 商业区(河西CBD、奥体中心)
- 居住区(江宁、浦口、栖霞)
- 特殊场所(医院、景区、大学城)

## Java模型更新
相应的Java模型类也已更新以匹配新的数据库模式：
- `ChargingStation.java`: 包含所有新字段的getter/setter方法
- `ChargingStationManager.java`: 更新了数据库查询和业务逻辑
- 所有相关的Servlets都已更新以使用新字段

## API变更
所有REST API端点均已更新以返回新字段：
- `/api/stations` - 返回所有充电站列表
- `/api/stations/{id}` - 返回特定充电站详情
- `/api/stations/nearby` - 返回附近充电站列表

## 前端变更
所有前端页面已更新以显示新信息：
- 充电站列表页面显示更多详细信息
- 预订页面显示可用插座数量
- 预订确认页面显示充电站地址等信息

## 数据库初始化
使用 `sql/init_data.sql` 脚本初始化数据库，该脚本包含了：
1. 更新的表结构定义
2. 南京地区20个充电站的数据
3. 示例用户数据
4. 必要的索引

## 注意事项
1. 旧的 `init.sql` 脚本仍然保留用于兼容性，但建议使用 `init_data.sql`
2. 应用程序现在基于插座数量而不是布尔值来确定充电站的可用性
3. 价格现在按小时计算，不再限制最大充电时长