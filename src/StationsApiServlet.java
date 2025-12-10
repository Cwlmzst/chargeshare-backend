import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/stations")
public class StationsApiServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();

        ChargingStationManager manager = ChargingStationManager.getInstance();
        List<ChargingStation> stations = manager.getAllStations();

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < stations.size(); i++) {
            if (i > 0) json.append(",");
            ChargingStation station = stations.get(i);
            json.append("{")
                .append("\"id\":").append(station.getStationId()).append(",")
                .append("\"stationId\":").append(station.getStationId()).append(",")
                .append("\"location\":\"").append(station.getLocation()).append("\",")
                .append("\"available\":").append(station.isAvailable()).append(",")
                .append("\"price\":").append(station.getPrice()).append(",")
                .append("\"maxDuration\":").append(station.getMaxDuration()).append(",")
                .append("\"lat\":").append(station.getLatitude()).append(",")
                .append("\"lng\":").append(station.getLongitude())
                .append("}");
        }
        json.append("]");

        out.print(json.toString());
        out.flush();
    }
}
