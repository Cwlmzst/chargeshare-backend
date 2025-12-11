import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/register")
public class AddUserFormServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>添加用户</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }");
        out.println("h1 { color: #333; }");
        out.println("form { background: white; padding: 20px; border-radius: 4px; max-width: 400px; margin: 0 auto; }");
        out.println("label { display: block; margin-top: 15px; font-weight: bold; }");
        out.println("input { width: 100%; padding: 8px; margin-top: 5px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }");
        out.println("button { margin-top: 20px; padding: 12px 20px; background-color: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; }");
        out.println("button:hover { background-color: #218838; }");
        out.println(".cancel-btn { background-color: #6c757d; margin-left: 10px; }");
        out.println(".cancel-btn:hover { background-color: #5a6268; }");
        out.println("a { margin-top: 20px; display: inline-block; padding: 10px 20px; background-color: #007bff; color: white; text-decoration: none; border-radius: 4px; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>添加新用户</h1>");
        out.println("<form method='post' action='/javaweb/api/auth/register'>");
        
        out.println("<label for='name'>姓名:</label>");
        out.println("<input type='text' id='name' name='name' required>");
        
        out.println("<label for='email'>邮箱:</label>");
        out.println("<input type='email' id='email' name='email' required>");
        
        out.println("<label for='phone'>手机号:</label>");
        out.println("<input type='tel' id='phone' name='phone'>");
        
        out.println("<label for='password'>密码:</label>");
        out.println("<input type='password' id='password' name='password' required>");
        
        out.println("<button type='submit'>添加用户</button>");
        out.println("<button type='button' class='cancel-btn' onclick=\"window.location.href='/javaweb/'\">取消</button>");
        out.println("</form>");
        out.println("<br><a href='/javaweb/'>主页</a>");
        out.println("</body>");
        out.println("</html>");
    }
}