import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/auth/register")
public class UserRegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            // Get user details from request
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String password = request.getParameter("password");
            
            // Validate required fields
            if (name == null || name.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\":false,\"message\":\"姓名是必填项\"}");
                return;
            }
            
            if (email == null || email.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\":false,\"message\":\"邮箱是必填项\"}");
                return;
            }
            
            if (password == null || password.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\":false,\"message\":\"密码是必填项\"}");
                return;
            }
            
            // Basic email validation
            if (!email.contains("@") || !email.contains(".")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\":false,\"message\":\"邮箱格式不正确\"}");
                return;
            }
            
            // Get manager instance
            ChargingStationManager manager = ChargingStationManager.getInstance();
            
            // Try to register user
            if (manager.registerUser(name.trim(), email.trim(), phone, password)) {
                User newUser = manager.getUserByEmail(email.trim());
                response.setStatus(HttpServletResponse.SC_OK);
                out.print("{\"success\":true,\"message\":\"注册成功\",\"user\":{" +
                        "\"userId\":" + newUser.getUserId() + "," +
                        "\"name\":\"" + newUser.getName() + "\"," +
                        "\"email\":\"" + newUser.getEmail() + "\"," +
                        "\"phone\":\"" + newUser.getPhone() + "\"," +
                        "\"balance\":" + newUser.getBalance() +
                        "}}");
            } else {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                out.print("{\"success\":false,\"message\":\"该邮箱已被注册\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"success\":false,\"message\":\"服务器错误: " + e.getMessage() + "\"}");
        }
    }
    
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
    }
}