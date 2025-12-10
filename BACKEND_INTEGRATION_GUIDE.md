# 前后端集成指南

## 概述
本项目采用前后端分离架构：
- **前端**: React + 高德地图（D:\java_proj\javaweb-client）
- **后端**: Java Servlet + 业务逻辑（D:\java_proj\javaweb）

## 前端准备工作（已完成）

### 1. API 配置文件
- 创建 `src/config/api.config.js` - 配置后端地址和API端点
- 创建 `src/services/stationService.js` - 服务层，支持模拟数据和真实API切换
- 更新 `src/services/api.js` - 使用配置的API端点

### 2. 支持的功能
- ✅ 地图定位
- ✅ 充电站列表显示
- ✅ 附近充电站查询（基于用户位置）
- ✅ 模拟数据降级

## Java 后端需要完成的改动

### 1. 更新 ChargingStation 类
需要添加坐标信息（latitude, longitude）

```java
public class ChargingStation {
    private int stationId;
    private String location;
    private boolean available;
    private double price;
    private int maxDuration;
    private double latitude;   // 新增：纬度
    private double longitude;  // 新增：经度

    public ChargingStation(int stationId, String location, double price, 
                          int maxDuration, double latitude, double longitude) {
        this.stationId = stationId;
        this.location = location;
        this.price = price;
        this.maxDuration = maxDuration;
        this.latitude = latitude;
        this.longitude = longitude;
        this.available = true;
    }

    // 添加 getter 方法
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
}
```

### 2. 更新 ChargingStationManager 类
初始化数据时添加坐标

```java
private void initializeData() {
    // 北京的充电站坐标
    stations.add(new ChargingStation(1, "朝阳门", 5.0, 8, 39.9173, 116.4152));
    stations.add(new ChargingStation(2, "东直门", 7.0, 12, 39.9496, 116.4352));
    stations.add(new ChargingStation(3, "建国门", 4.5, 6, 39.9110, 116.4197));
    stations.add(new ChargingStation(4, "天安门广场", 6.0, 10, 39.9075, 116.3972));
    stations.add(new ChargingStation(5, "故宫", 5.5, 8, 39.9246, 116.3967));
    
    users.add(new User(1, "John Doe", "john@example.com", 100.0));
    users.add(new User(2, "Jane Smith", "jane@example.com", 150.0));
    users.add(new User(3, "Bob Johnson", "bob@example.com", 75.0));
}
```

### 3. 创建 JSON 工具类
`src/JsonUtil.java`

```java
import java.util.*;

public class JsonUtil {
    /**
     * 对象转 JSON 字符串
     */
    public static String toJson(Object obj) {
        if (obj == null) return "null";
        
        if (obj instanceof String) {
            return "\"" + escapeJson((String) obj) + "\"";
        }
        if (obj instanceof Number) {
            return obj.toString();
        }
        if (obj instanceof Boolean) {
            return obj.toString();
        }
        if (obj instanceof List) {
            return listToJson((List<?>) obj);
        }
        if (obj instanceof Map) {
            return mapToJson((Map<?, ?>) obj);
        }
        return "\"" + obj.toString() + "\"";
    }
    
    private static String listToJson(List<?> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(toJson(list.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }
    
    private static String mapToJson(Map<?, ?> map) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!first) sb.append(",");
            sb.append("\"").append(entry.getKey()).append("\":");
            sb.append(toJson(entry.getValue()));
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }
    
    private static String escapeJson(String str) {
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r");
    }
}
```

### 4. 创建 CORS 过滤器
`src/CorsFilter.java`

```java
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class CorsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        
        // 设置 CORS 头
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        httpResponse.setHeader("Access-Control-Max-Age", "3600");
        
        // 处理 OPTIONS 请求
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
```

### 5. 创建 REST API Servlets

#### StationsApiServlet.java
`/api/stations` - 获取所有充电站

```java
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/api/stations")
public class StationsApiServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();

        ChargingStationManager manager = ChargingStationManager.getInstance();
        List<ChargingStation> stations = manager.getAllStations();

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < stations.size(); i++) {
            if (i > 0) json.append(",");
            ChargingStation station = stations.get(i);
            json.append("{")
                .append("\"id\":").append(station.getStationId()).append(",")
                .append("\"stationId\":").append(station.getStationId()).append(",")
                .append("\"location\":\"").append(station.getLocation()).append("\",")
                .append("\"available\":").append(station.isAvailable()).append(",")
                .append("\"price\":").append(station.getPrice()).append(",")
                .append("\"maxDuration\":").append(station.getMaxDuration()).append(",")
                .append("\"lat\":").append(station.getLatitude()).append(",")
                .append("\"lng\":").append(station.getLongitude())
                .append("}");
        }
        json.append("]");

        out.print(json.toString());
        out.flush();
    }
}
```

#### StationDetailApiServlet.java
`/api/stations/{id}` - 获取单个充电站详情

```java
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/stations/*")
public class StationDetailApiServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();

        String pathInfo = request.getPathInfo(); // /123
        if (pathInfo == null || pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Station ID required\"}");
            return;
        }

        try {
            int stationId = Integer.parseInt(pathInfo.substring(1));
            ChargingStationManager manager = ChargingStationManager.getInstance();
            ChargingStation station = manager.getStationById(stationId);

            if (station == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Station not found\"}");
                return;
            }

            String json = "{" +
                    "\"id\":" + station.getStationId() + "," +
                    "\"stationId\":" + station.getStationId() + "," +
                    "\"location\":\"" + station.getLocation() + "\"," +
                    "\"available\":" + station.isAvailable() + "," +
                    "\"price\":" + station.getPrice() + "," +
                    "\"maxDuration\":" + station.getMaxDuration() + "," +
                    "\"lat\":" + station.getLatitude() + "," +
                    "\"lng\":" + station.getLongitude() +
                    "}";

            out.print(json);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Invalid station ID\"}");
        }
        out.flush();
    }
}
```

#### NearbyStationsApiServlet.java
`/api/stations/nearby` - 获取附近充电站

```java
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/stations/nearby")
public class NearbyStationsApiServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            double latitude = Double.parseDouble(request.getParameter("latitude"));
            double longitude = Double.parseDouble(request.getParameter("longitude"));
            int radius = Integer.parseInt(request.getParameter("radius") != null ? 
                    request.getParameter("radius") : "5000");

            ChargingStationManager manager = ChargingStationManager.getInstance();
            List<ChargingStation> allStations = manager.getAllStations();

            // 计算距离并筛选
            StringBuilder json = new StringBuilder("[");
            boolean first = true;
            for (ChargingStation station : allStations) {
                double distance = calculateDistance(latitude, longitude, 
                        station.getLatitude(), station.getLongitude());
                
                if (distance <= radius) {
                    if (!first) json.append(",");
                    json.append("{")
                        .append("\"id\":").append(station.getStationId()).append(",")
                        .append("\"location\":\"").append(station.getLocation()).append("\",")
                        .append("\"available\":").append(station.isAvailable()).append(",")
                        .append("\"lat\":").append(station.getLatitude()).append(",")
                        .append("\"lng\":").append(station.getLongitude()).append(",")
                        .append("\"distance\":").append((int)distance)
                        .append("}");
                    first = false;
                }
            }
            json.append("]");

            out.print(json.toString());
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Invalid parameters\"}");
        }
        out.flush();
    }

    // 计算两点之间的距离（米）- Haversine 公式
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000; // 地球半径，单位：米
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }
}
```

## 配置步骤

### 前端配置
1. 确保 Node.js 和 npm 已安装
2. 进入前端目录：`cd D:\java_proj\javaweb-client`
3. 安装依赖：`npm install`
4. 启动开发服务器：`npm start`（默认 localhost:3000）

### 后端配置
1. 将上述 Java 文件添加到 `D:\java_proj\javaweb\src\` 目录
2. 配置 web.xml（如果使用 Servlet 注解则不需要）
3. 编译并部署到 Tomcat（或其他应用服务器）
4. 确保后端运行在 `http://localhost:8080/javaweb`

## 切换模拟数据和真实API

### 使用模拟数据（开发/测试）
编辑 `src/config/api.config.js`：
```javascript
useMockData: true  // 使用本地模拟数据
```

### 使用真实 API（生产）
编辑 `src/config/api.config.js`：
```javascript
useMockData: false  // 使用真实后端API
```

## API 文档

### 1. 获取所有充电站
- **URL**: `/api/stations`
- **方法**: `GET`
- **返回**:
```json
[
  {
    "id": 1,
    "stationId": 1,
    "location": "朝阳门",
    "available": true,
    "price": 5.0,
    "maxDuration": 8,
    "lat": 39.9173,
    "lng": 116.4152
  }
]
```

### 2. 获取单个充电站
- **URL**: `/api/stations/{id}`
- **方法**: `GET`
- **参数**: id (充电站ID)
- **返回**: 单个充电站 JSON 对象

### 3. 获取附近充电站
- **URL**: `/api/stations/nearby`
- **方法**: `GET`
- **参数**:
  - `latitude` (必需) - 用户纬度
  - `longitude` (必需) - 用户经度
  - `radius` (可选) - 搜索半径，单位米（默认 5000）
- **返回**: 附近充电站数组

## 测试

### 使用 curl 测试后端 API
```bash
# 获取所有充电站
curl http://localhost:8080/javaweb/api/stations

# 获取单个充电站
curl http://localhost:8080/javaweb/api/stations/1

# 获取附近充电站
curl "http://localhost:8080/javaweb/api/stations/nearby?latitude=39.9&longitude=116.4&radius=5000"
```

## 常见问题

### 1. CORS 错误
- 确保后端已配置 CORS 过滤器
- 检查浏览器控制台的错误信息

### 2. 后端连接失败
- 确保 Java 应用已启动且运行在正确的端口
- 检查防火墙设置
- 在 `src/config/api.config.js` 中验证后端地址

### 3. 坐标显示错误
- 验证 ChargingStation 中的 latitude 和 longitude 是否正确
- 检查地图配置中的坐标范围

## 下一步
- [ ] 完成 Java 后端 REST API 实现
- [ ] 测试前后端集成
- [ ] 添加数据库支持（MySQL/PostgreSQL）
- [ ] 实现用户认证和授权
- [ ] 添加预约功能 API
- [ ] 性能优化和缓存
- [ ] 部署到生产环境
