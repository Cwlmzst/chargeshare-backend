import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库连接管理类
 * 使用 HikariCP 连接池管理数据库连接
 */
public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/charging_station_db?useUnicode=true&characterEncoding=utf8";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "26126";
    
    private static DataSource dataSource;
    
    static {
        try {
            MysqlDataSource mysqlDataSource = new MysqlDataSource();
            mysqlDataSource.setURL(DB_URL);
            mysqlDataSource.setUser(DB_USER);
            mysqlDataSource.setPassword(DB_PASSWORD);
            dataSource = mysqlDataSource;
            
            // 测试连接
            try (Connection conn = dataSource.getConnection()) {
                System.out.println("✅ 数据库连接成功!");
            } catch (SQLException e) {
                System.err.println("❌ 数据库连接失败: " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("❌ 数据库初始化失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 获取数据库连接
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("DataSource 未初始化");
        }
        return dataSource.getConnection();
    }
    
    /**
     * 测试连接
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn.isValid(5);
        } catch (SQLException e) {
            System.err.println("❌ 连接测试失败: " + e.getMessage());
            return false;
        }
    }
}
