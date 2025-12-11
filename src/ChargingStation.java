public class ChargingStation {
    private int stationId;
    private String name;
    private double latitude;
    private double longitude;
    private String address;
    private int totalSockets;
    private int availableSockets;
    private double powerOutput;
    private double pricePerHour;
    private String status;
    private String description;

    public ChargingStation(int stationId, String name, double latitude, double longitude, 
                          String address, int totalSockets, int availableSockets, 
                          double powerOutput, double pricePerHour, String status, String description) {
        this.stationId = stationId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.totalSockets = totalSockets;
        this.availableSockets = availableSockets;
        this.powerOutput = powerOutput;
        this.pricePerHour = pricePerHour;
        this.status = status;
        this.description = description;
    }

    // Getters
    public int getStationId() {
        return stationId;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public int getTotalSockets() {
        return totalSockets;
    }

    public int getAvailableSockets() {
        return availableSockets;
    }

    public double getPowerOutput() {
        return powerOutput;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTotalSockets(int totalSockets) {
        this.totalSockets = totalSockets;
    }

    public void setAvailableSockets(int availableSockets) {
        this.availableSockets = availableSockets;
    }

    public void setPowerOutput(double powerOutput) {
        this.powerOutput = powerOutput;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ChargingStation{" +
                "stationId=" + stationId +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                ", totalSockets=" + totalSockets +
                ", availableSockets=" + availableSockets +
                ", powerOutput=" + powerOutput +
                ", pricePerHour=" + pricePerHour +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}