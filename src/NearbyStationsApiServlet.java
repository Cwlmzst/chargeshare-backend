import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/stations/nearby")
public class NearbyStationsApiServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            double latitude = Double.parseDouble(request.getParameter("latitude"));
            double longitude = Double.parseDouble(request.getParameter("longitude"));
            int radius = Integer.parseInt(request.getParameter("radius") != null ? 
                    request.getParameter("radius") : "5000");

            ChargingStationManager manager = ChargingStationManager.getInstance();
            List<ChargingStation> allStations = manager.getAllStations();

            // Calculate distance and filter
            StringBuilder json = new StringBuilder("[");
            boolean first = true;
            for (ChargingStation station : allStations) {
                double distance = calculateDistance(latitude, longitude, 
                        station.getLatitude(), station.getLongitude());
                
                if (distance <= radius) {
                    if (!first) json.append(",");
                    json.append("{")
                        .append("\"id\":").append(station.getStationId()).append(",")
                        .append("\"location\":\"").append(station.getLocation()).append("\",")
                        .append("\"available\":").append(station.isAvailable()).append(",")
                        .append("\"price\":").append(station.getPrice()).append(",")
                        .append("\"maxDuration\":").append(station.getMaxDuration()).append(",")
                        .append("\"lat\":").append(station.getLatitude()).append(",")
                        .append("\"lng\":").append(station.getLongitude()).append(",")
                        .append("\"distance\":").append((int)distance)
                        .append("}");
                    first = false;
                }
            }
            json.append("]");

            out.print(json.toString());
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Invalid parameters\"}");
        }
        out.flush();
    }

    // Calculate distance between two points (in meters) - Haversine formula
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000; // Earth radius in meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }
}
