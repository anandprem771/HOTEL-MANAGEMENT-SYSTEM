import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HotelManagementSystem {

    public static void main(String[] args) {
        ReservationSystem reservationSystem = new ReservationSystem();
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("1. Check Room Availability");
            System.out.println("2. Reserve a Room");
            System.out.println("3. Add Housekeeping Service");
            System.out.println("4. Add Food Service");
            System.out.println("5. Checkout" + "(Generate bill)");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Enter room number: ");
                    int roomNo = scanner.nextInt();
                    if (reservationSystem.checkAvailability(roomNo)) {
                        System.out.println("Room is available");
                    } else {
                        System.out.println("Room is not available");
                    }
                    break;
                case 2:
                    System.out.print("Enter customer name: ");
                    String customerName = scanner.next();
                    System.out.print("Enter address: ");
                    String address = scanner.next();
                    System.out.print("Enter contact number: ");
                    String contactNo = scanner.next();
                    System.out.print("Enter room type: ");
                    String roomType = scanner.next();
                    System.out.print("Enter room number: ");
                    int roomNumber = scanner.nextInt();
                    System.out.print("Enter check-in date (yyyy-mm-dd): ");
                    LocalDate checkInDate = LocalDate.parse(scanner.next());
                    System.out.print("Enter check-out date (yyyy-mm-dd): ");
                    LocalDate checkOutDate = LocalDate.parse(scanner.next());
                    long totalDays = reservationSystem.getNumberOfDaysBetweenDates(checkInDate, checkOutDate);
                    reservationSystem.reserveRoom(customerName, address, contactNo, roomType, roomNumber, checkInDate,
                            checkOutDate, totalDays);
                    break;
                case 3:
                    System.out.print("Enter room number: ");
                    int housekeepingRoomNo = scanner.nextInt();
                    reservationSystem.addHousekeepingService(housekeepingRoomNo);
                    break;
                case 4:
                    System.out.print("Enter room number: ");
                    int foodRoomNo = scanner.nextInt();
                    reservationSystem.addFoodService(foodRoomNo);
                    break;
                case 5:
                    System.out.print("Enter room number: ");
                    int billRoomNo = scanner.nextInt();
                    System.out.print("Enter payment amount: ");
                    double payment = scanner.nextDouble();
                    reservationSystem.generateBill(billRoomNo, payment);
                    break;
                case 6:
                    System.out.println("Exiting the program...");
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (choice != 6);
    }

    static class ReservationSystem {
        private Map<Integer, Reservation> reservations = new HashMap<>();

        public boolean checkAvailability(int roomNo) {
            return !reservations.containsKey(roomNo);
        }

        public void reserveRoom(String customerName, String address, String contactNo, String roomType, int roomNo,
                LocalDate checkInDate, LocalDate checkOutDate, long totalDays) {
            if (checkAvailability(roomNo)) {
                Reservation reservation = new Reservation(customerName, address, contactNo, roomType, roomNo,
                        checkInDate, checkOutDate, totalDays);
                reservations.put(roomNo, reservation);
                System.out.println("Room reserved successfully");
            } else {
                System.out.println("Room is not available for reservation");
            }
        }

        public void addHousekeepingService(int roomNo) {
            Reservation reservation = reservations.get(roomNo);
            if (reservation != null) {
                reservation.setHousekeepingService(true);
                System.out.println("Housekeeping service added to the room");
            } else {
                System.out.println("Invalid room number");
            }
        }

        public void addFoodService(int roomNo) {
            Reservation reservation = reservations.get(roomNo);
            if (reservation != null) {
                reservation.setFoodService(true);
                System.out.println("Food service added to the room");
            } else {
                System.out.println("Invalid room number");
            }
        }

        public void generateBill(int roomNo, double payment) {
            Reservation reservation = reservations.get(roomNo);
            if (reservation != null) {
                double totalBill = reservation.getTotalBill(payment);
                System.out.printf("Total pending amount for room no. %d is %.2f\n", roomNo, totalBill);
                reservations.remove(roomNo);
            } else {
                System.out.println("Invalid room number");
            }
        }

        public long getNumberOfDaysBetweenDates(LocalDate startDate, LocalDate endDate) {
            return ChronoUnit.DAYS.between(startDate, endDate);
        }
    }

    static class Reservation {
        private String customerName;
        private String address;
        private String contactNo;
        private String roomType;
        private int roomNo;
        private LocalDate checkInDate;
        private LocalDate checkOutDate;
        private long totalDays;
        private boolean housekeepingService;
        private boolean foodService;

        public Reservation(String customerName, String address, String contactNo, String roomType, int roomNo,
                LocalDate checkInDate, LocalDate checkOutDate, long totalDays) {
            this.customerName = customerName;
            this.address = address;
            this.contactNo = contactNo;
            this.roomType = roomType;
            this.roomNo = roomNo;
            this.checkInDate = checkInDate;
            this.checkOutDate = checkOutDate;
            this.totalDays = totalDays;
        }

        public void setHousekeepingService(boolean housekeepingService) {
            this.housekeepingService = housekeepingService;
        }

        public void setFoodService(boolean foodService) {
            this.foodService = foodService;
        }

        public double getTotalBill(double payment) {
            double roomCharge = totalDays * getRoomCharge();
            double housekeepingCharge = housekeepingService ? totalDays * 50 : 0;
            double foodCharge = foodService ? totalDays * 100 : 0;
            double totalBill = roomCharge + housekeepingCharge + foodCharge;
            double change = payment - totalBill;
            if (change >= 0) {
                System.out.printf("Total paid amount: %.2f\n", change);
            } else {
                System.out.printf("Additional Payment Required: %.2f\n", -change);
            }
            return totalBill;
        }

        private double getRoomCharge() {
            switch (roomType) {
                case "Simple":
                    return 500;
                case "Standard":
                    return 1000;
                case "Ac":
                    return 2000;
                default:
                    return 0;
            }
        }
    }

    static class DateTime {
        public static LocalDate getCurrentDate() {
            return LocalDate.now();
        }

        public static LocalTime getCurrentTime() {
            return LocalTime.now();
        }

        public static long getNumberOfDaysBetweenDates(LocalDate startDate, LocalDate endDate) {
            return ChronoUnit.DAYS.between(startDate, endDate);
        }
    }
}
