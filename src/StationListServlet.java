import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/stations")
public class StationListServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        ChargingStationManager manager = ChargingStationManager.getInstance();
        List<ChargingStation> stations = manager.getAllStations();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>充电站</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }");
        out.println("h1 { color: #333; }");
        out.println("table { border-collapse: collapse; width: 100%; max-width: 1000px; background: white; }");
        out.println("th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }");
        out.println("th { background-color: #007bff; color: white; }");
        out.println("tr:nth-child(even) { background-color: #f9f9f9; }");
        out.println(".available { color: green; font-weight: bold; }");
        out.println(".unavailable { color: red; font-weight: bold; }");
        out.println("a { margin-top: 20px; display: inline-block; padding: 10px 20px; background-color: #007bff; color: white; text-decoration: none; border-radius: 4px; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>可用充电站</h1>");
        out.println("<table>");
        out.println("<tr><th>充电站编列</th><th>位置</th><th>状态</th><th>价格 (元/小时)</th><th>最大持续 (小时)</th></tr>");

        for (ChargingStation station : stations) {
            String status = station.isAvailable() ? "<span class='available'>可用</span>" : "<span class='unavailable'>使用中</span>";
            out.println("<tr>");
            out.println("<td>" + station.getStationId() + "</td>");
            out.println("<td>" + station.getLocation() + "</td>");
            out.println("<td>" + status + "</td>");
            out.println("<td>$" + String.format("%.2f", station.getPrice()) + "/元</td>");
            out.println("<td>" + station.getMaxDuration() + "</td>");
            out.println("</tr>");
        }

        out.println("</table>");
        out.println("<br><a href='/javaweb/'>主页</a>");
        out.println("</body>");
        out.println("</html>");
    }
}
