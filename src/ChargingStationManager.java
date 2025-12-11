import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ChargingStationManager {
    private static ChargingStationManager instance;
    private List<ChargingStation> stations;
    private List<User> users;
    private List<Booking> bookings;
    private int nextBookingId;

    public boolean useDatabase = true; // 切换数据库和内存存储

    private ChargingStationManager() {
        stations = new ArrayList<>();
        users = new ArrayList<>();
        bookings = new ArrayList<>();
        nextBookingId = 1;
        
        // 测试数据库连接
        if (!DatabaseConnection.testConnection()) {
            System.err.println("⚠️ 数据库连接失败，使用内存存储");
            useDatabase = false;
        }
        
        initializeData();
    }

    public static ChargingStationManager getInstance() {
        if (instance == null) {
            instance = new ChargingStationManager();
        }
        return instance;
    }

    private void initializeData() {
        if (useDatabase) {
            loadStationsFromDatabase();
            loadUsersFromDatabase();
            loadBookingsFromDatabase();
        } else {
            // 如果数据库不可用，使用内存数据
            loadDefaultData();
        }
    }
    
    /**
     * 从数据库加载充电站
     */
    private void loadStationsFromDatabase() {
        String sql = "SELECT station_id, name, latitude, longitude, address, total_sockets, available_sockets, power_output, price_per_hour, status, description FROM charging_stations";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                ChargingStation station = new ChargingStation(
                    rs.getInt("station_id"),
                    rs.getString("name"),
                    rs.getDouble("latitude"),
                    rs.getDouble("longitude"),
                    rs.getString("address"),
                    rs.getInt("total_sockets"),
                    rs.getInt("available_sockets"),
                    rs.getDouble("power_output"),
                    rs.getDouble("price_per_hour"),
                    rs.getString("status"),
                    rs.getString("description")
                );
                stations.add(station);
            }
            System.out.println("✅ 从数据库加载了 " + stations.size() + " 个充电站");
        } catch (SQLException e) {
            System.err.println("❌ 加载充电站失败: " + e.getMessage());
            useDatabase = false;
        }
    }
    
    /**
     * 从数据库加载用户
     */
    private void loadUsersFromDatabase() {
        String sql = "SELECT user_id, name, email, phone, password, balance FROM users";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
        
        while (rs.next()) {
            User user = new User(
                rs.getInt("user_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("password"),
                rs.getDouble("balance")
            );
            users.add(user);
        }
        System.out.println("✅ 从数据库加载了 " + users.size() + " 个用户");
    } catch (SQLException e) {
        System.err.println("❌ 加载用户失败: " + e.getMessage());
        useDatabase = false;
    }
}
    
    /**
     * 从数据库加载预约
     */
    private void loadBookingsFromDatabase() {
        String sql = "SELECT booking_id, user_id, station_id, start_time, end_time, total_cost, status FROM bookings";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            int maxBookingId = 0;
            while (rs.next()) {
                int bookingId = rs.getInt("booking_id");
                Booking booking = new Booking(
                    bookingId,
                    rs.getInt("user_id"),
                    rs.getInt("station_id"),
                    rs.getTimestamp("start_time").toLocalDateTime(),
                    rs.getTimestamp("end_time").toLocalDateTime(),
                    rs.getDouble("total_cost")
                );
                booking.setStatus(rs.getString("status"));
                bookings.add(booking);
                if (bookingId > maxBookingId) {
                    maxBookingId = bookingId;
                }
            }
            nextBookingId = maxBookingId + 1;
            System.out.println("✅ 从数据库加载了 " + bookings.size() + " 个预约");
        } catch (SQLException e) {
            System.err.println("❌ 加载预约失败: " + e.getMessage());
            useDatabase = false;
        }
    }
    
    /**
     * 加载默认内存数据
     */
    private void loadDefaultData() {
        // Add sample charging stations with coordinates (Nanjing area)
        stations.add(new ChargingStation(1, "南京鼓楼充电站", 32.05840000, 118.77750000, 
            "南京市鼓楼区中山路1号", 10, 6, 7.00, 5.50, "ACTIVE", "市中心充电站，24小时服务"));
        stations.add(new ChargingStation(2, "南京玄武充电站", 32.05000000, 118.80000000, 
            "南京市玄武区玄武巷1号", 8, 8, 3.50, 3.50, "ACTIVE", "玄武湖公园附近，快充服务"));
        stations.add(new ChargingStation(3, "南京江宁充电站", 31.95390000, 118.87200000, 
            "南京市江宁区双龙大道1号", 12, 4, 10.00, 5.00, "ACTIVE", "江宁区主要充电站，快充服务"));
    }

    // Station methods
    public List<ChargingStation> getAllStations() {
        return new ArrayList<>(stations);
    }

    public ChargingStation getStationById(int stationId) {
        return stations.stream()
                .filter(s -> s.getStationId() == stationId)
                .findFirst()
                .orElse(null);
    }

    public List<ChargingStation> getAvailableStations() {
        List<ChargingStation> available = new ArrayList<>();
        for (ChargingStation station : stations) {
            // Assuming stations with available sockets are available
            if (station.getAvailableSockets() > 0) {
                available.add(station);
            }
        }
        return available;
    }

    // User methods
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public User getUserById(int userId) {
        return users.stream()
                .filter(u -> u.getUserId() == userId)
                .findFirst()
                .orElse(null);
    }

    public User getUserByEmail(String email) {
        return users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    public User getUserByIdOrPhone(String identifier) {
        // Try to parse as integer for user ID
        try {
            int userId = Integer.parseInt(identifier);
            return getUserById(userId);
        } catch (NumberFormatException e) {
            // Not a number, treat as phone number
            return users.stream()
                    .filter(u -> u.getPhone() != null && u.getPhone().equals(identifier))
                    .findFirst()
                    .orElse(null);
        }
    }

    public User getUserByEmailOrName(String identifier) {
        return users.stream()
            .filter(u -> u.getEmail().equalsIgnoreCase(identifier) || 
                         u.getName().equalsIgnoreCase(identifier))
            .findFirst()
            .orElse(null);
    }

    public boolean updateUserInDatabase(User user) {
        String sql = "UPDATE users SET name = ?, email = ?, phone = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhone());
            pstmt.setInt(4, user.getUserId());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ 用户信息已更新到数据库");
                return true;
            } else {
                System.err.println("❌ 未找到要更新的用户");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("❌ 更新用户信息失败: " + e.getMessage());
            return false;
        }
    }

    public boolean updateUserBalanceInDatabase(int userId, double newBalance) {
        String sql = "UPDATE users SET balance = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, newBalance);
            pstmt.setInt(2, userId);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ 用户余额已更新到数据库");
                return true;
            } else {
                System.err.println("❌ 未找到要更新余额的用户");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("❌ 更新用户余额失败: " + e.getMessage());
            return false;
        }
    }

    // Booking methods
    public Booking createBooking(int userId, int stationId, int hours) {
        User user = getUserById(userId);
        ChargingStation station = getStationById(stationId);

        if (user == null || station == null) {
            return null;
        }

        // Check if station has available sockets
        if (station.getAvailableSockets() <= 0) {
            return null;
        }

        double totalCost = hours * station.getPricePerHour();

        if (!user.deductBalance(totalCost)) {
            return null;
        }

        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(hours);

        Booking booking = new Booking(nextBookingId++, userId, stationId, startTime, endTime, totalCost);
        bookings.add(booking);
        
        // Update available sockets
        station.setAvailableSockets(station.getAvailableSockets() - 1);
        
        // 保存到数据库
        if (useDatabase) {
            if (!saveBookingToDatabase(booking)) {
                // 如果数据库保存失败，回滚内存中的更改
                bookings.remove(booking);
                user.addBalance(totalCost); // 退还用户余额
                station.setAvailableSockets(station.getAvailableSockets() + 1); // 恢复插座数量
                return null;
            }
            updateStationAvailability(stationId, station.getAvailableSockets());
        }

        return booking;
    }
    
    /**
     * 保存预约到数据库
     */
    private boolean saveBookingToDatabase(Booking booking) {
        String sql = "INSERT INTO bookings (user_id, station_id, start_time, end_time, total_cost, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, booking.getUserId());
            pstmt.setInt(2, booking.getStationId());
            pstmt.setTimestamp(3, Timestamp.valueOf(booking.getStartTime()));
            pstmt.setTimestamp(4, Timestamp.valueOf(booking.getEndTime()));
            pstmt.setDouble(5, booking.getTotalCost());
            pstmt.setString(6, booking.getStatus());
            pstmt.executeUpdate();
            System.out.println("✅ 预约已保存到数据库");
            return true;
        } catch (SQLException e) {
            System.err.println("❌ 保存预约失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 更新充电站可用性
     */
    private void updateStationAvailability(int stationId, int availableSockets) {
        String sql = "UPDATE charging_stations SET available_sockets = ? WHERE station_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, availableSockets);
            pstmt.setInt(2, stationId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ 更新充电站状态失败: " + e.getMessage());
        }
    }

    public List<Booking> getUserBookings(int userId) {
        List<Booking> userBookings = new ArrayList<>();
        for (Booking booking : bookings) {
            if (booking.getUserId() == userId) {
                userBookings.add(booking);
            }
        }
        return userBookings;
    }

    public boolean completeBooking(int bookingId) {
        Booking booking = bookings.stream()
                .filter(b -> b.getBookingId() == bookingId)
                .findFirst()
                .orElse(null);

        if (booking == null || !booking.getStatus().equals("active")) {
            return false;
        }

        booking.setStatus("completed");
        ChargingStation station = getStationById(booking.getStationId());
        if (station != null) {
            // Increase available sockets when booking is completed
            station.setAvailableSockets(station.getAvailableSockets() + 1);
            
            // 更新数据库
            if (useDatabase) {
                updateBookingStatus(bookingId, "completed");
                updateStationAvailability(booking.getStationId(), station.getAvailableSockets());
            }
        }
        return true;
    }
    
    /**
     * 更新预约状态
     */
    private void updateBookingStatus(int bookingId, String status) {
        String sql = "UPDATE bookings SET status = ? WHERE booking_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, bookingId);
            pstmt.executeUpdate();
            System.out.println("✅ 预约状态已更新");
        } catch (SQLException e) {
            System.err.println("❌ 更新预约失败: " + e.getMessage());
        }
    }

    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings);
    }

    public void printAllStations() {
        System.out.println("\n=== All Charging Stations ===");
        for (ChargingStation station : stations) {
            System.out.println(station);
        }
    }

    public void printAllUsers() {
        System.out.println("\n=== All Users ===");
        for (User user : users) {
            System.out.println(user);
        }
    }

    public void printAllBookings() {
        System.out.println("\n=== All Bookings ===");
        for (Booking booking : bookings) {
            System.out.println(booking);
        }
    }
}