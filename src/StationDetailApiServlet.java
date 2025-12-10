import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/stations/*")
public class StationDetailApiServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();

        String pathInfo = request.getPathInfo(); // /123
        if (pathInfo == null || pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Station ID required\"}");
            return;
        }

        try {
            int stationId = Integer.parseInt(pathInfo.substring(1));
            ChargingStationManager manager = ChargingStationManager.getInstance();
            ChargingStation station = manager.getStationById(stationId);

            if (station == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Station not found\"}");
                return;
            }

            String json = "{" +
                    "\"id\":" + station.getStationId() + "," +
                    "\"stationId\":" + station.getStationId() + "," +
                    "\"location\":\"" + station.getLocation() + "\"," +
                    "\"available\":" + station.isAvailable() + "," +
                    "\"price\":" + station.getPrice() + "," +
                    "\"maxDuration\":" + station.getMaxDuration() + "," +
                    "\"lat\":" + station.getLatitude() + "," +
                    "\"lng\":" + station.getLongitude() +
                    "}";

            out.print(json);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Invalid station ID\"}");
        }
        out.flush();
    }
}
