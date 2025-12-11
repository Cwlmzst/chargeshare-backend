import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/auth/login")
public class LoginApiServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Get user ID/phone/email and password from request
            String identifier = request.getParameter("identifier");
            String password = request.getParameter("password");

            if (identifier == null || identifier.trim().isEmpty() || password == null || password.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\":false,\"message\":\"用户ID/手机号/邮箱和密码是必填项\"}");
                return;
            }

            // Find user by ID, phone, or email
            ChargingStationManager manager = ChargingStationManager.getInstance();
            User user = manager.getUserByIdOrPhone(identifier.trim());
            
            // If not found by ID or phone, try to find by email
            if (user == null) {
                user = manager.getUserByEmail(identifier.trim());
            }

            if (user == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print("{\"success\":false,\"message\":\"用户ID/手机号/邮箱或密码错误\"}");
                return;
            }

            // Verify password
            if (!user.getPassword().equals(password)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print("{\"success\":false,\"message\":\"用户ID/手机号/邮箱或密码错误\"}");
                return;
            }

            // Login successful - return user info
            response.setStatus(HttpServletResponse.SC_OK);
            out.print("{\"success\":true,\"message\":\"登录成功\",\"user\":{" +
                    "\"userId\":" + user.getUserId() + "," +
                    "\"name\":\"" + user.getName() + "\"," +
                    "\"email\":\"" + user.getEmail() + "\"," +
                    "\"phone\":\"" + user.getPhone() + "\"," +
                    "\"balance\":" + user.getBalance() +
                    "}}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"success\":false,\"message\":\"服务器错误: " + e.getMessage() + "\"}");
        }
    }

    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);
    }
}