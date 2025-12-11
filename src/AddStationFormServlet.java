import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/add-station")
public class AddStationFormServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>添加充电站</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }");
        out.println("h1 { color: #333; }");
        out.println("form { background: white; padding: 20px; border-radius: 4px; max-width: 500px; margin: 0 auto; }");
        out.println("label { display: block; margin-top: 15px; font-weight: bold; }");
        out.println("input, select, textarea { width: 100%; padding: 8px; margin-top: 5px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }");
        out.println("button { margin-top: 20px; padding: 12px 20px; background-color: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; }");
        out.println("button:hover { background-color: #218838; }");
        out.println(".cancel-btn { background-color: #6c757d; margin-left: 10px; }");
        out.println(".cancel-btn:hover { background-color: #5a6268; }");
        out.println("a { margin-top: 20px; display: inline-block; padding: 10px 20px; background-color: #007bff; color: white; text-decoration: none; border-radius: 4px; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>添加新充电站</h1>");
        out.println("<form method='post' action='/javaweb/api/stations/add'>");
        
        out.println("<label for='name'>充电站名称:</label>");
        out.println("<input type='text' id='name' name='name' required>");
        
        out.println("<label for='address'>地址:</label>");
        out.println("<input type='text' id='address' name='address' required>");
        
        out.println("<label for='latitude'>纬度:</label>");
        out.println("<input type='number' id='latitude' name='latitude' step='any' required>");
        
        out.println("<label for='longitude'>经度:</label>");
        out.println("<input type='number' id='longitude' name='longitude' step='any' required>");
        
        out.println("<label for='totalSockets'>总插座数:</label>");
        out.println("<input type='number' id='totalSockets' name='totalSockets' min='1' required>");
        
        out.println("<label for='availableSockets'>可用插座数:</label>");
        out.println("<input type='number' id='availableSockets' name='availableSockets' min='0' required>");
        
        out.println("<label for='powerOutput'>功率 (kW):</label>");
        out.println("<input type='number' id='powerOutput' name='powerOutput' step='0.01' min='0' required>");
        
        out.println("<label for='pricePerHour'>每小时价格 (元):</label>");
        out.println("<input type='number' id='pricePerHour' name='pricePerHour' step='0.01' min='0' required>");
        
        out.println("<label for='status'>状态:</label>");
        out.println("<select id='status' name='status'>");
        out.println("<option value='ACTIVE'>启用</option>");
        out.println("<option value='INACTIVE'>禁用</option>");
        out.println("</select>");
        
        out.println("<label for='description'>描述:</label>");
        out.println("<textarea id='description' name='description' rows='3'></textarea>");
        
        out.println("<button type='submit'>添加充电站</button>");
        out.println("<button type='button' class='cancel-btn' onclick=\"window.location.href='/javaweb/'\">取消</button>");
        out.println("</form>");
        out.println("<br><a href='/javaweb/'>主页</a>");
        out.println("</body>");
        out.println("</html>");
    }
}