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
                .append("\"name\":\"").append(station.getName()).append("\",")
                .append("\"address\":\"").append(station.getAddress()).append("\",")
                .append("\"availableSockets\":").append(station.getAvailableSockets()).append(",")
                .append("\"totalSockets\":").append(station.getTotalSockets()).append(",")
                .append("\"powerOutput\":").append(station.getPowerOutput()).append(",")
                .append("\"pricePerHour\":").append(station.getPricePerHour()).append(",")
                .append("\"status\":\"").append(station.getStatus()).append("\",")
                .append("\"lat\":").append(station.getLatitude()).append(",")
                .append("\"lng\":").append(station.getLongitude())
                .append("}");
        }
        json.append("]");

        out.print(json.toString());
        out.flush();
    }
}