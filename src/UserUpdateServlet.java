import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/users/update")
public class UserUpdateServlet extends HttpServlet {
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            // Get user ID and updated fields from request
            int userId = Integer.parseInt(request.getParameter("userId"));
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            
            // Validate required fields
            if (userId <= 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\":false,\"message\":\"用户ID是必填项\"}");
                return;
            }
            
            // Get manager instance
            ChargingStationManager manager = ChargingStationManager.getInstance();
            User user = manager.getUserById(userId);
            
            if (user == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"success\":false,\"message\":\"用户不存在\"}");
                return;
            }
            
            // Update user fields
            if (name != null && !name.trim().isEmpty()) {
                user.setName(name.trim());
            }
            if (email != null && !email.trim().isEmpty()) {
                user.setEmail(email.trim());
            }
            if (phone != null && !phone.trim().isEmpty()) {
                user.setPhone(phone.trim());
            }
            
            // Save to database
            if (manager.useDatabase) {
                if (updateUserInDatabase(user)) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    out.print("{\"success\":true,\"message\":\"用户信息更新成功\",\"user\":{" +
                            "\"userId\":" + user.getUserId() + "," +
                            "\"name\":\"" + user.getName() + "\"," +
                            "\"email\":\"" + user.getEmail() + "\"," +
                            "\"phone\":\"" + user.getPhone() + "\"," +
                            "\"balance\":" + user.getBalance() +
                            "}}");
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print("{\"success\":false,\"message\":\"数据库更新失败\"}");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
                out.print("{\"success\":true,\"message\":\"用户信息已更新到内存（数据库不可用）\",\"user\":{" +
                        "\"userId\":" + user.getUserId() + "," +
                        "\"name\":\"" + user.getName() + "\"," +
                        "\"email\":\"" + user.getEmail() + "\"," +
                        "\"phone\":\"" + user.getPhone() + "\"," +
                        "\"balance\":" + user.getBalance() +
                        "}}");
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"success\":false,\"message\":\"用户ID必须是数字\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"success\":false,\"message\":\"服务器错误: " + e.getMessage() + "\"}");
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Delegate to doPut for easier testing
        doPut(request, response);
    }
    
    private boolean updateUserInDatabase(User user) {
        try {
            ChargingStationManager manager = ChargingStationManager.getInstance();
            return manager.updateUserInDatabase(user);
        } catch (Exception e) {
            System.err.println("❌ 更新用户信息失败: " + e.getMessage());
            return false;
        }
    }
    
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);
    }
}