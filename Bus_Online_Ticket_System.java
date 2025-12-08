import java.util.*;
import java.io.*;

// ========================= PERSON (Base Class) =========================
abstract class Person {
    protected String username;
    protected String password;

    public Person(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
}

// ========================= USER (Child of Person) =========================
class User extends Person {
    public User(String username, String password) {
        super(username, password);
    }
}

// ========================= ADMIN (Child of Person) =========================
class Admin extends Person {
    public Admin(String username, String password) {
        super(username, password);
    }

    public boolean verify(String pwd) {
        return this.password.equals(pwd);
    }
}

// ========================= POLYMORPHISM (Interface) =========================
interface Bookable {
    void book();
}

// ========================= BUS (Base Class) =========================
class Bus {
    String id, route, time, type;
    int capacity;
    double fare;

    public Bus(String id, String route, String time, int capacity, double fare, String type) {
        this.id = id;
        this.route = route;
        this.time = time;
        this.capacity = capacity;
        this.fare = fare;
        this.type = type;
    }

    public double getFare() { return fare; }
}

// ========================= POLYMORPHIC BUS =========================
class ACBus extends Bus {
    public ACBus(String id, String route, String time, int capacity, double fare) {
        super(id, route, time, capacity, fare , "AC");
    }
    public double getFare() { return fare +100; }
}

class NonACBus extends Bus {
    public NonACBus(String id, String route, String time, int capacity, double fare) {
        super(id, route, time, capacity, fare, "Non-AC");
    }
}

// ========================= BOOKING =========================
class Booking implements Bookable {
    String bookingId, busId, passenger, route, date, type;
    int seats;
    double totalFare;

    public Booking(String bookingId, String busId, String passenger, String route,
                   int seats, String date, double fare, String type) {
        this.bookingId = bookingId;
        this.busId = busId;
        this.passenger = passenger;
        this.route = route;
        this.date = date;
        this.seats = seats;
        this.totalFare = seats * fare;
        this.type = type;
    }

    public void book() {
        System.out.println("\nBooking Confirmed!");
    }
}

// ========================= MAIN SYSTEM =========================
public class Bus_Online_Ticket_System {

    static List<Bus> buses = new ArrayList<>();
    static Map<String, Booking> bookings = new HashMap<>();
    static Map<String, User> users = new HashMap<>();
    static Scanner sc = new Scanner(System.in);

    static Person loggedInUser = null;
    static Admin admin = new Admin("admin", "azizur123");

    public static void main(String[] args) {

        loadUsers();
        loadBuses();
        loadBookings();

        while (true) {
            if (loggedInUser == null) {
                System.out.println("\n=== Bus Ticket Booking System ===");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("0. Exit");
                System.out.print("Choose: ");
                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1 -> registerUser();
                    case 2 -> loginUser();
                    case 0 -> {
                        saveUsers(); saveBuses(); saveBookings();
                        System.out.println("Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid option.");
                }

            } else {
                System.out.println("\n=== Welcome, " + loggedInUser.username + " ===");
                System.out.println("1. Book Ticket");
                System.out.println("2. Cancel Ticket");
                System.out.println("3. View Booking Details");
                System.out.println("4. Admin Panel");
                System.out.println("0. Logout");
                System.out.print("Choose: ");
                int c = Integer.parseInt(sc.nextLine());

                switch (c) {
                    case 1 -> bookTicket();
                    case 2 -> cancelTicket();
                    case 3 -> viewBooking();
                    case 4 -> adminPanel();
                    case 0 -> { loggedInUser = null; System.out.println("Logged out!"); }
                    default -> System.out.println("Invalid option.");
                }
            }
        }
    }

    // ---------------------------LOAD USERS------------------------------
    static void loadUsers() {
        try {
            File file = new File("users.txt");
            if (!file.exists()) return;

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                users.put(p[0], new User(p[0], p[1]));
            }

            br.close();
        } catch (Exception e) {
            System.out.println("Error loading users.");
        }
    }

    static void saveUsers() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("users.txt"));

            for (User u : users.values()) {
                bw.write(u.username + "," + u.password);
                bw.newLine();
            }

            bw.close();
        } catch (Exception e) {
            System.out.println("Error saving users.");
        }
    }

    // ---------------------------LOAD BUSES------------------------------
    static void loadBuses() {
        try {
            File file = new File("buses.txt");
            if (!file.exists()) { seedData(); return; }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");

                if (p[5].equals("AC"))
                    buses.add(new ACBus(p[0], p[1], p[2], Integer.parseInt(p[3]), Double.parseDouble(p[4]) + 100));
                else
                    buses.add(new NonACBus(p[0], p[1], p[2], Integer.parseInt(p[3]), Double.parseDouble(p[4])));
            }

            br.close();
        } catch (Exception e) {
            System.out.println("Error loading buses.");
        }
    }

    static void saveBuses() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("buses.txt"));

            for (Bus b : buses) {
                bw.write(b.id + "," + b.route + "," + b.time + "," + b.capacity + "," + b.fare + "," + b.type);
                bw.newLine();
            }

            bw.close();
        } catch (Exception e) {
            System.out.println("Error saving buses.");
        }
    }

    // ---------------------------LOAD BOOKINGS------------------------------
    static void loadBookings() {
        try {
            File file = new File("bookings.txt");
            if (!file.exists()) return;

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                Booking bk = new Booking(p[0], p[1], p[2], p[3], Integer.parseInt(p[4]),
                        p[5], Double.parseDouble(p[6]), p[7]);
                bookings.put(p[0], bk);
            }

            br.close();
        } catch (Exception e) {
            System.out.println("Error loading bookings.");
        }
    }

    static void saveBookings() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("bookings.txt"));

            for (Booking bk : bookings.values()) {
                bw.write(bk.bookingId + "," + bk.busId + "," + bk.passenger + "," +
                        bk.route + "," + bk.seats + "," + bk.date + "," + bk.totalFare +
                        "," + bk.type);
                bw.newLine();
            }

            bw.close();
        } catch (Exception e) {
            System.out.println("Error saving bookings.");
        }
    }

    // ---------------------------SEED DATA-----------------------------------
    static void seedData() {
        buses.add(new ACBus("B101-A", "Dhaka-Barisal", "08:00 AM", 40, 500));
        buses.add(new NonACBus("B102-N", "Dhaka-Barisal", "08:00 AM", 40, 500));

        buses.add(new ACBus("B103-A", "Dhaka-Chittagong", "09:00 AM", 45, 600));
        buses.add(new NonACBus("B104-N", "Dhaka-Chittagong", "09:00 AM", 45, 600));

        buses.add(new ACBus("B105-A", "Dhaka-Khulna", "10:00 AM", 50, 550));
        buses.add(new NonACBus("B106-N", "Dhaka-Khulna", "10:00 AM", 50, 550));

        buses.add(new ACBus("B107-A", "Dhaka-Sylhet", "09:00 AM", 45, 600));
        buses.add(new NonACBus("B108-N", "Dhaka-Sylhet", "10:00 AM", 45, 600));
    }

    // -----------------------------REGISTER-------------------------------
    static void registerUser() {
        System.out.print("Enter username: ");
        String uname = sc.nextLine();

        if (users.containsKey(uname)) {
            System.out.println("Username already exists!");
            return;
        }

        String pwd;
        while (true) {
            try {
                System.out.print("Enter password (4 digit): ");
                pwd = sc.nextLine();

                if (pwd.length() != 4)
                    throw new Exception("Password must be exactly 4 digits.");

                Integer.parseInt(pwd);
                break;

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        users.put(uname, new User(uname, pwd));
        saveUsers();
        System.out.println("Registration successful!");
    }

    // ----------------------------LOGIN-------------------------------
    static void loginUser() {
        System.out.print("Enter username: ");
        String uname = sc.nextLine();
        System.out.print("Enter password: ");
        String pwd = sc.nextLine();

        User u = users.get(uname);

        if (u != null && u.getPassword().equals(pwd)) {
            loggedInUser = u;
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    // ---------------------------------VIEW SCHEDULE-------------------------------
    static void viewSchedule() {
        System.out.println("\n--- Available Buses ---");
        for (Bus b : buses) {
            System.out.printf("%s | %s | %s | %s | Seats:%d | Fare:%.2f%n",
                    b.id, b.route, b.time, b.type, b.capacity, b.getFare());
        }
    }

    // ---------------------------------BOOK TICKET-------------------------------
   static void bookTicket() {

        viewSchedule();

        System.out.print("Enter Route: ");
        String route = sc.nextLine();

        System.out.print("Choose Bus Type (AC / Non-AC): ");
        String type = sc.nextLine().trim();

        // ---- Filter buses based on route & type ----
        List<Bus> matchedBuses = new ArrayList<>();
        for (Bus b : buses) {
            if (b.route.equalsIgnoreCase(route) && b.type.equalsIgnoreCase(type)) {
                matchedBuses.add(b);
            }
        }

        if (matchedBuses.isEmpty()) {
            System.out.println("No bus found for this route & type.");
            return;
        }

        System.out.println("\n--- Buses Found ---");
        for (Bus b : matchedBuses) {
            System.out.printf("%s | %s | %s | Seats:%d | Fare: %.2f\n",
                b.id, b.route, b.time, b.capacity, b.getFare());
        }


        System.out.print("Enter Bus ID: ");
        String busId = sc.nextLine();

        Bus bus = findBus(busId);

        if (bus == null || !bus.route.equalsIgnoreCase(route) || !bus.type.equalsIgnoreCase(type)) {
            System.out.println("Invalid Bus ID!");
            return;
        }

        System.out.print("Passenger Name: ");
        String name = sc.nextLine();

        System.out.print("Seats: ");
        int seats = Integer.parseInt(sc.nextLine());

        if (seats > bus.capacity) {
            System.out.println("Not enough seats.");
            return;
        }

        // -------------- DATE --------------
        java.time.LocalDate today = java.time.LocalDate.now();

        System.out.print("Enter Date (YYYY-MM-DD): ");
        String inputDate = sc.nextLine();

        java.time.LocalDate userDate;

        try {
            userDate = java.time.LocalDate.parse(inputDate);
        } catch (Exception e) {
            System.out.println("Invalid date format!");
            return;
        }

        if (userDate.isBefore(today)) {
            System.out.println("Invalid Date!");
            return;
        }

        double totalFare = seats * bus.getFare();
        System.out.println("Total Fare: " + totalFare);

        System.out.print("Enter Payment: ");
        double payment = Double.parseDouble(sc.nextLine());

        if (payment < totalFare) {
            System.out.println("Insufficient Payment!");
            return;
        }
        if (payment > totalFare)
            System.out.println("Change Returned: " + (payment - totalFare));

        String bookingId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Booking bk = new Booking(
            bookingId, busId, name, route, seats,
            userDate.toString(), bus.getFare(), bus.type
        );

        bookings.put(bookingId, bk);
        bus.capacity -= seats;

        saveBuses();
        saveBookings();

        bk.book();
        System.out.println("\nBooking ID: " + bookingId);
        System.out.println("Bus: " + busId + " | Type: " + bus.type);
        System.out.println("Route: " + route);
        System.out.println("Passenger: " + name + " | Seats: " + seats);
        System.out.println("Total Fare: " + bk.totalFare);
        System.out.println("Journey Date: " + userDate + " | Bus Time: " + bus.time);
    }

    // ----------------------------------CANCEL TICKET------------------------------
    static void cancelTicket() {
        System.out.print("Enter Booking ID: ");
        String bid = sc.nextLine();

        Booking bk = bookings.remove(bid);

        if (bk != null) {
            Bus bus = findBus(bk.busId);
            if (bus != null) bus.capacity += bk.seats;

            saveBookings();
            saveBuses();

            System.out.println("Booking cancelled.");
        } else {
            System.out.println("Not found.");
        }
    }

    // -------------------------------VIEW BOOKING------------------------------
    static void viewBooking() {
        System.out.print("Enter Booking ID: ");
        String bid = sc.nextLine();

        Booking bk = bookings.get(bid);

        if (bk != null) {
            System.out.println("\nBooking ID: " + bk.bookingId);
            System.out.println("Bus: " + bk.busId + " | Type: " + bk.type);
            System.out.println("Passenger: " + bk.passenger + " | Seats: " + bk.seats);
            System.out.println("Route: " + bk.route);
            System.out.println("Total Fare: " + bk.totalFare);
            System.out.println("Journey Date: " + bk.date);
        } else {
            System.out.println("Not found.");
        }
    }

    // ------------------------------ADMIN PANEL--------------------------------
    static void adminPanel() {
        System.out.print("Admin Password: ");
        String pwd = sc.nextLine();

        if (!admin.verify(pwd)) {
            System.out.println("Access denied.");
            return;
        }

        while (true) {
            System.out.println("\n--- Admin Panel ---");
            System.out.println("1. Add Bus");
            System.out.println("2. Remove Bus");
            System.out.println("3. View All Bookings");
            System.out.println("0. Back");
            System.out.print("Choose: ");
            int c = Integer.parseInt(sc.nextLine());

            switch (c) {
                case 1 -> addBus();
                case 2 -> removeBus();
                case 3 -> viewAllBookings();
                case 0 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // ------------------------------ADD BUS--------------------------------
    static void addBus() {
        System.out.print("Bus ID: ");
        String id = sc.nextLine();
        System.out.print("Route: ");
        String route = sc.nextLine();
        System.out.print("Time: ");
        String time = sc.nextLine();
        System.out.print("Capacity: ");
        int cap = Integer.parseInt(sc.nextLine());
        System.out.print("Fare: ");
        double fare = Double.parseDouble(sc.nextLine());

        buses.add(new NonACBus(id + "-N", route, time, cap, fare));
        buses.add(new ACBus(id + "-A", route, time, cap, fare));

        saveBuses();
        System.out.println("AC & Non-AC Both Bus Added.");
    }

    // ------------------------------REMOVE BUS--------------------------------
    static void removeBus() {
        System.out.print("Bus ID: ");
        String id = sc.nextLine();

        Bus bus = findBus(id);

        if (bus != null) {
            buses.remove(bus);
            saveBuses();
            System.out.println("Bus removed.");
        } else {
            System.out.println("Not found.");
        }
    }

    // ------------------------------VIEW ALL BOOKINGS--------------------------------
    static void viewAllBookings() {
        for (Booking bk : bookings.values()) {
            System.out.printf("%s | %s | %s | %s | Seats:%d | Fare:%.2f%n",
                    bk.bookingId, bk.busId, bk.passenger, bk.type, bk.seats, bk.totalFare);
        }
    }

    // --------------------------HELPERS------------------------------
    static Bus findBus(String id) {
        for (Bus b : buses)
            if (b.id.equalsIgnoreCase(id)) return b;
        return null;
    }
}
