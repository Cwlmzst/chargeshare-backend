import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/users")
public class UserServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        ChargingStationManager manager = ChargingStationManager.getInstance();
        List<User> users = manager.getAllUsers();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>用户</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }");
        out.println("h1 { color: #333; }");
        out.println("table { border-collapse: collapse; width: 100%; max-width: 1000px; background: white; }");
        out.println("th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }");
        out.println("th { background-color: #007bff; color: white; }");
        out.println("tr:nth-child(even) { background-color: #f9f9f9; }");
        out.println("a { margin-top: 20px; display: inline-block; padding: 10px 20px; background-color: #007bff; color: white; text-decoration: none; border-radius: 4px; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>系统用户</h1>");
        out.println("<table>");
        out.println("<tr><th>用户ID</th><th>名称</th><th>电子邮件</th><th>账户余额</th></tr>");

        for (User user : users) {
            out.println("<tr>");
            out.println("<td>" + user.getUserId() + "</td>");
            out.println("<td>" + user.getName() + "</td>");
            out.println("<td>" + user.getEmail() + "</td>");
            out.println("<td>$" + String.format("%.2f", user.getBalance()) + "</td>");
            out.println("</tr>");
        }

        out.println("</table>");
        out.println("<br><a href='/javaweb/'>主页</a>");
        out.println("</body>");
        out.println("</html>");
    }
}
