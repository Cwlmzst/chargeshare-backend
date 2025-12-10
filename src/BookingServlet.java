
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/booking")
public class BookingServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>创建预订</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }");
        out.println("h1 { color: #333; }");
        out.println("form { background: white; padding: 20px; border-radius: 4px; max-width: 400px; }");
        out.println("label { display: block; margin-top: 10px; font-weight: bold; }");
        out.println("input, select { width: 100%; padding: 8px; margin-top: 5px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }");
        out.println("button { margin-top: 15px; padding: 10px 20px; background-color: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer; }");
        out.println("button:hover { background-color: #218838; }");
        out.println("a { margin-top: 20px; display: inline-block; padding: 10px 20px; background-color: #007bff; color: white; text-decoration: none; border-radius: 4px; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>创建新预订</h1>");
        out.println("<form method='post' action='/javaweb/booking'>");
        out.println("<label for='userId'>用户ID:</label>");
        out.println("<input type='number' id='userId' name='userId' required>");
        
        out.println("<label for='stationId'>充电站ID:</label>");
        out.println("<select id='stationId' name='stationId' required>");
        out.println("<option value=''>-- 选择充电站 --</option>");
        
        ChargingStationManager manager = ChargingStationManager.getInstance();
        List<ChargingStation> stations = manager.getAvailableStations();
        for (ChargingStation station : stations) {
            out.println("<option value='" + station.getStationId() + "'>" + 
                        station.getLocation() + " ($" + String.format("%.2f", station.getPrice()) + "/小时)</option>");
        }
        
        out.println("</select>");
        out.println("<label for='hours'>持续 (小时):</label>");
        out.println("<input type='number' id='hours' name='hours' min='1' max='12' required>");
        out.println("<button type='submit'>创建预订</button>");
        out.println("</form>");
        out.println("<br><a href='/javaweb/'>主页</a>");
        out.println("</body>");
        out.println("</html>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            int stationId = Integer.parseInt(request.getParameter("stationId"));
            int hours = Integer.parseInt(request.getParameter("hours"));

            ChargingStationManager manager = ChargingStationManager.getInstance();
            Booking booking = manager.createBooking(userId, stationId, hours);

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>预订结果</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }");
            out.println(".success { background-color: #d4edda; color: #155724; padding: 15px; border-radius: 4px; }");
            out.println(".error { background-color: #f8d7da; color: #721c24; padding: 15px; border-radius: 4px; }");
            out.println("a { margin-top: 20px; display: inline-block; padding: 10px 20px; background-color: #007bff; color: white; text-decoration: none; border-radius: 4px; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");

            if (booking != null) {
                out.println("<div class='success'>");
                out.println("<h2>预订成功创建!</h2>");
                out.println("<p><strong>预订ID:</strong> " + booking.getBookingId() + "</p>");
                out.println("<p><strong>充电站:</strong> " + manager.getStationById(stationId).getLocation() + "</p>");
                out.println("<p><strong>持续:</strong> " + hours + " 小时</p>");
                out.println("<p><strong>总成本:</strong> $" + String.format("%.2f", booking.getTotalCost()) + "</p>");
                out.println("<p><strong>状态:</strong> " + ("活动".equals(booking.getStatus()) ? "活动" : "完成") + "</p>");
                out.println("</div>");
            } else {
                out.println("<div class='error'>");
                out.println("<h2>预订失败！</h2>");
                out.println("<p>请检查:</p>");
                out.println("<ul>");
                out.println("<li>用户ID存在</li>");
                out.println("<li>充电站可用</li>");
                out.println("<li>持续不超过站点最大值</li>");
                out.println("<li>用户有足够的余额</li>");
                out.println("</ul>");
                out.println("</div>");
            }

            out.println("<br><a href='/javaweb/'>主页</a>");
            out.println("</body>");
            out.println("</html>");
        } catch (NumberFormatException e) {
            out.println("预订失败: 无效的输入格式!");
        }
    }
}
