import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Booking {
    private int bookingId;
    private int userId;
    private int stationId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double totalCost;
    private String status; // "active", "completed", "cancelled"

    public Booking(int bookingId, int userId, int stationId, LocalDateTime startTime, LocalDateTime endTime, double totalCost) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.stationId = stationId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalCost = totalCost;
        this.status = "active";
    }

    public int getBookingId() {
        return bookingId;
    }

    public int getUserId() {
        return userId;
    }

    public int getStationId() {
        return stationId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "Booking{" +
                "bookingId=" + bookingId +
                ", userId=" + userId +
                ", stationId=" + stationId +
                ", startTime=" + startTime.format(formatter) +
                ", endTime=" + endTime.format(formatter) +
                ", totalCost=" + totalCost +
                ", status='" + status + '\'' +
                '}';
    }
}
