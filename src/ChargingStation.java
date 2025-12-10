public class ChargingStation {
    private int stationId;
    private String location;
    private boolean available;
    private double price; // Price per hour
    private int maxDuration; // Max charging hours
    private double latitude;   // Latitude coordinate
    private double longitude;  // Longitude coordinate

    public ChargingStation(int stationId, String location, double price, int maxDuration) {
        this.stationId = stationId;
        this.location = location;
        this.price = price;
        this.maxDuration = maxDuration;
        this.available = true;
        this.latitude = 0.0;
        this.longitude = 0.0;
    }

    public ChargingStation(int stationId, String location, double price, int maxDuration, double latitude, double longitude) {
        this.stationId = stationId;
        this.location = location;
        this.price = price;
        this.maxDuration = maxDuration;
        this.available = true;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getStationId() {
        return stationId;
    }

    public String getLocation() {
        return location;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public double getPrice() {
        return price;
    }

    public int getMaxDuration() {
        return maxDuration;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "ChargingStation{" +
                "stationId=" + stationId +
                ", location='" + location + '\'' +
                ", available=" + available +
                ", price=" + price +
                ", maxDuration=" + maxDuration +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
