import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/bookings")
public class BookingsListServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        ChargingStationManager manager = ChargingStationManager.getInstance();
        List<Booking> bookings = manager.getAllBookings();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>所有预订</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }");
        out.println("h1 { color: #333; }");
        out.println("table { border-collapse: collapse; width: 100%; background: white; }");
        out.println("th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }");
        out.println("th { background-color: #007bff; color: white; }");
        out.println("tr:nth-child(even) { background-color: #f9f9f9; }");
        out.println(".active { color: green; font-weight: bold; }");
        out.println(".completed { color: blue; font-weight: bold; }");
        out.println("a { margin-top: 20px; display: inline-block; padding: 10px 20px; background-color: #007bff; color: white; text-decoration: none; border-radius: 4px; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>所有预订</h1>");
        out.println("<table>");
        out.println("<tr><th>预订ID</th><th>用户ID</th><th>充电站ID</th><th>开始时间</th><th>结束时间</th><th>成本</th><th>状态</th></tr>");

        if (bookings.isEmpty()) {
            out.println("<tr><td colspan='7' style='text-align:center;'>暂无预订</td></tr>");
        } else {
            for (Booking booking : bookings) {
                String statusClass = booking.getStatus().equals("active") ? "active" : "completed";
                out.println("<tr>");
                out.println("<td>" + booking.getBookingId() + "</td>");
                out.println("<td>" + booking.getUserId() + "</td>");
                out.println("<td>" + booking.getStationId() + "</td>");
                out.println("<td>" + booking.getStartTime().format(formatter) + "</td>");
                out.println("<td>" + booking.getEndTime().format(formatter) + "</td>");
                out.println("<td>$" + String.format("%.2f", booking.getTotalCost()) + "</td>");
                String displayStatus = booking.getStatus().equals("active") ? "活动" : "完成";
                out.println("<td><span class='" + statusClass + "'>" + displayStatus + "</span></td>");
                out.println("</tr>");
            }
        }

        out.println("</table>");
        out.println("<br><a href='/javaweb/'>主页</a>");
        out.println("</body>");
        out.println("</html>");
    }
}
