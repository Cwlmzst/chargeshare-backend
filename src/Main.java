public class Main {
    public static void main(String[] args) {
        // Initialize the Charging Station Manager
        ChargingStationManager manager = ChargingStationManager.getInstance();
        
        System.out.println("===== Shared Charging Station System =====");
        System.out.println("\nThe web application is ready to run!");
        System.out.println("\nTo start the system:");
        System.out.println("1. Deploy this project to a servlet container (Tomcat, etc.)");
        System.out.println("2. Access the application at: http://localhost:8080/javaweb/");
        System.out.println("\n--- System Data Initialized ---");
        
        // Display system data
        manager.printAllStations();
        manager.printAllUsers();
        manager.printAllBookings();
        
        System.out.println("\n--- Demo: Creating a Booking ---");
        // Demo booking
        Booking demoBooking = manager.createBooking(1, 1, 3);
        if (demoBooking != null) {
            System.out.println("✓ Successfully created booking: " + demoBooking);
            System.out.println("✓ User balance after booking: $" + String.format("%.2f", manager.getUserById(1).getBalance()));
            System.out.println("✓ Station 1 available sockets: " + manager.getStationById(1).getAvailableSockets());
        }
    }
}