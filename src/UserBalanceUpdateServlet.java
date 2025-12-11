import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/users/balance")
public class UserBalanceUpdateServlet extends HttpServlet {
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            // Get user ID and amount from request
            int userId = Integer.parseInt(request.getParameter("userId"));
            double amount = Double.parseDouble(request.getParameter("amount"));
            
            // Validate required fields
            if (userId <= 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\":false,\"message\":\"用户ID是必填项\"}");
                return;
            }
            
            if (amount <= 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\":false,\"message\":\"充值金额必须大于0\"}");
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
            
            // Update user balance
            double oldBalance = user.getBalance();
            user.addBalance(amount);
            double newBalance = user.getBalance();
            
            // Save to database
            if (manager.useDatabase) {
                if (manager.updateUserBalanceInDatabase(userId, newBalance)) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    out.print("{\"success\":true,\"message\":\"余额充值成功\",\"user\":{" +
                            "\"userId\":" + user.getUserId() + "," +
                            "\"name\":\"" + user.getName() + "\"," +
                            "\"email\":\"" + user.getEmail() + "\"," +
                            "\"phone\":\"" + user.getPhone() + "\"," +
                            "\"oldBalance\":" + oldBalance + "," +
                            "\"addedAmount\":" + amount + "," +
                            "\"newBalance\":" + newBalance +
                            "}}");
                } else {
                    // Rollback the balance change if database update failed
                    user.setBalance(oldBalance);
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print("{\"success\":false,\"message\":\"数据库更新失败，余额未变更\"}");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
                out.print("{\"success\":true,\"message\":\"余额已更新到内存（数据库不可用）\",\"user\":{" +
                        "\"userId\":" + user.getUserId() + "," +
                        "\"name\":\"" + user.getName() + "\"," +
                        "\"email\":\"" + user.getEmail() + "\"," +
                        "\"phone\":\"" + user.getPhone() + "\"," +
                        "\"oldBalance\":" + oldBalance + "," +
                        "\"addedAmount\":" + amount + "," +
                        "\"newBalance\":" + newBalance +
                        "}}");
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"success\":false,\"message\":\"用户ID和金额必须是数字\"}");
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
    
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);
    }
}