import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("")
public class HomeServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>共享充电站系统</title>");
        out.println("<style>");
        out.println("body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 20px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; }");
        out.println(".container { max-width: 900px; margin: 0 auto; background: white; padding: 40px; border-radius: 10px; box-shadow: 0 10px 30px rgba(0,0,0,0.3); }");
        out.println("h1 { color: #333; text-align: center; margin-bottom: 10px; }");
        out.println(".subtitle { text-align: center; color: #666; margin-bottom: 40px; }");
        out.println(".menu { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 20px; }");
        out.println(".menu-item { padding: 20px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; border-radius: 8px; text-decoration: none; text-align: center; transition: transform 0.3s, box-shadow 0.3s; }");
        out.println(".menu-item:hover { transform: translateY(-5px); box-shadow: 0 5px 20px rgba(0,0,0,0.2); }");
        out.println(".menu-item h3 { margin: 0 0 10px 0; }");
        out.println(".menu-item p { margin: 0; font-size: 14px; opacity: 0.9; }");
        out.println(".info { background: #f0f0f0; padding: 20px; border-radius: 8px; margin-top: 40px; }");
        out.println(".info h3 { margin-top: 0; color: #333; }");
        out.println(".info p { margin: 5px 0; color: #666; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<h1>⚡ 共享充电站系统</h1>");
        out.println("<p class='subtitle'>轻松管理您的电动车充电预订</p>");
        
        out.println("<div class='menu'>");
        out.println("<a href='/javaweb/stations' class='menu-item'>");
        out.println("<h3>📍 查看充电站</h3>");
        out.println("<p>浏览所有可用充电站</p>");
        out.println("</a>");
        
        out.println("<a href='/javaweb/booking' class='menu-item'>");
        out.println("<h3>🔋 创建预订</h3>");
        out.println("<p>预订一个充电站</p>");
        out.println("</a>");
        
        out.println("<a href='/javaweb/users' class='menu-item'>");
        out.println("<h3>👥 查看用户</h3>");
        out.println("<p>查看所有已注册用户和余额</p>");
        out.println("</a>");
        
        out.println("<a href='/javaweb/bookings' class='menu-item'>");
        out.println("<h3>📋 查看预订</h3>");
        out.println("<p>检查所有预订及其状态</p>");
        out.println("</a>");
        out.println("</div>");
        
        out.println("<div class='info'>");
        out.println("<h3>系统功能:</h3>");
        out.println("<p>✓ 浏览带有详细信息的充电站（地址、插座数量、功率等）</p>");
        out.println("<p>✓ 实时查看充电站可用插座数量</p>");
        out.println("<p>✓ 通过选择用户、充电站和持续时间来创建预订</p>");
        out.println("<p>✓ 自动从用户账户扣除余额</p>");
        out.println("<p>✓ 跟踪所有预订及其状态</p>");
        out.println("<p>✓ 实时可用性更新</p>");
        out.println("</div>");
        
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}