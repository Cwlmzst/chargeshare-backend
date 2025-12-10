import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChargingStationManager {
    private static ChargingStationManager instance;
    private List<ChargingStation> stations;
    private List<User> users;
    private List<Booking> bookings;
    private int nextBookingId;

    private ChargingStationManager() {
        stations = new ArrayList<>();
        users = new ArrayList<>();
        bookings = new ArrayList<>();
        nextBookingId = 1;
        initializeData();
    }

    public static ChargingStationManager getInstance() {
        if (instance == null) {
            instance = new ChargingStationManager();
        }
        return instance;
    }

    private void initializeData() {
        // Add sample charging stations with coordinates (Beijing area)
        stations.add(new ChargingStation(1, "朝阳门", 5.0, 8, 39.9173, 116.4152));
        stations.add(new ChargingStation(2, "东直门", 7.0, 12, 39.9496, 116.4352));
        stations.add(new ChargingStation(3, "建国门", 4.5, 6, 39.9110, 116.4197));
        stations.add(new ChargingStation(4, "天安门广场", 6.0, 10, 39.9075, 116.3972));
        stations.add(new ChargingStation(5, "故宫", 5.5, 8, 39.9246, 116.3967));

        // Add sample users
        users.add(new User(1, "John Doe", "john@example.com", 100.0));
        users.add(new User(2, "Jane Smith", "jane@example.com", 150.0));
        users.add(new User(3, "Bob Johnson", "bob@example.com", 75.0));
    }

    // Station methods
    public List<ChargingStation> getAllStations() {
        return new ArrayList<>(stations);
    }

    public ChargingStation getStationById(int stationId) {
        return stations.stream()
                .filter(s -> s.getStationId() == stationId)
                .findFirst()
                .orElse(null);
    }

    public List<ChargingStation> getAvailableStations() {
        List<ChargingStation> available = new ArrayList<>();
        for (ChargingStation station : stations) {
            if (station.isAvailable()) {
                available.add(station);
            }
        }
        return available;
    }

    // User methods
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public User getUserById(int userId) {
        return users.stream()
                .filter(u -> u.getUserId() == userId)
                .findFirst()
                .orElse(null);
    }

    // Booking methods
    public Booking createBooking(int userId, int stationId, int hours) {
        User user = getUserById(userId);
        ChargingStation station = getStationById(stationId);

        if (user == null || station == null) {
            return null;
        }

        if (!station.isAvailable()) {
            return null;
        }

        if (hours > station.getMaxDuration()) {
            return null;
        }

        double totalCost = hours * station.getPrice();

        if (!user.deductBalance(totalCost)) {
            return null;
        }

        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(hours);

        Booking booking = new Booking(nextBookingId++, userId, stationId, startTime, endTime, totalCost);
        bookings.add(booking);
        station.setAvailable(false);

        return booking;
    }

    public List<Booking> getUserBookings(int userId) {
        List<Booking> userBookings = new ArrayList<>();
        for (Booking booking : bookings) {
            if (booking.getUserId() == userId) {
                userBookings.add(booking);
            }
        }
        return userBookings;
    }

    public boolean completeBooking(int bookingId) {
        Booking booking = bookings.stream()
                .filter(b -> b.getBookingId() == bookingId)
                .findFirst()
                .orElse(null);

        if (booking == null || !booking.getStatus().equals("active")) {
            return false;
        }

        booking.setStatus("completed");
        ChargingStation station = getStationById(booking.getStationId());
        if (station != null) {
            station.setAvailable(true);
        }
        return true;
    }

    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings);
    }

    public void printAllStations() {
        System.out.println("\n=== All Charging Stations ===");
        for (ChargingStation station : stations) {
            System.out.println(station);
        }
    }

    public void printAllUsers() {
        System.out.println("\n=== All Users ===");
        for (User user : users) {
            System.out.println(user);
        }
    }

    public void printAllBookings() {
        System.out.println("\n=== All Bookings ===");
        for (Booking booking : bookings) {
            System.out.println(booking);
        }
    }
}
