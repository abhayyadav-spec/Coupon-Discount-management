import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Main {
    private static final Scanner SC = new Scanner(System.in);
    private static final CouponManager MANAGER = new CouponManager();
    private static final CouponFileStore STORE = new CouponFileStore("data/coupons.txt");

    public static void main(String[] args) {
        try {
            for (Coupon coupon : STORE.load()) MANAGER.addCoupon(coupon);
            System.out.println("Loaded " + MANAGER.allCoupons().size() + " coupon(s).");
        } catch (IOException | IllegalArgumentException ex) {
            System.out.println("Could not load coupon data: " + ex.getMessage());
            return;
        }

        boolean running = true;
        while (running) {
            printMenu();
            String choice = SC.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> addCoupon();
                    case "2" -> applyCoupon();
                    case "3" -> searchCoupon();
                    case "4" -> showCoupons();
                    case "5" -> bestCoupon();
                    case "6" -> sortedReport();
                    case "0" -> running = false;
                    default -> System.out.println("Invalid menu choice. Try again.");
                }
            } catch (IllegalArgumentException | IllegalStateException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
        try { STORE.save(MANAGER.allCoupons()); System.out.println("Coupon data saved. Goodbye!"); }
        catch (IOException ex) { System.out.println("Could not save data: " + ex.getMessage()); }
    }

    private static void printMenu() {
        System.out.println("\n===== COUPON & DISCOUNT MANAGER =====");
        System.out.println("1. Add coupon\n2. Apply coupon\n3. Search coupon\n4. Display coupons (BST order)\n5. Find best valid coupon\n6. Sorted discount report\n0. Save and exit");
        System.out.print("Enter choice: ");
    }
    private static void addCoupon() {
        System.out.print("Coupon code: "); String code = SC.nextLine().trim().toUpperCase();
        System.out.print("Type (1=percentage, 2=flat): "); String t = SC.nextLine().trim();
        Coupon.DiscountType type = switch (t) { case "1" -> Coupon.DiscountType.PERCENT; case "2" -> Coupon.DiscountType.FLAT; default -> throw new IllegalArgumentException("Choose 1 or 2 for discount type."); };
        double value = readPositiveDouble("Discount value: ");
        LocalDate start = readDate("Start date (YYYY-MM-DD): ");
        LocalDate end = readDate("End date (YYYY-MM-DD): ");
        int limit = readPositiveInt("Usage limit: ");
        Coupon coupon = new Coupon(code, type, value, start, end, limit, 0);
        MANAGER.addCoupon(coupon);
        System.out.println("Coupon added: " + coupon.getCode() + " | " + (type == Coupon.DiscountType.PERCENT ? value + "%" : "Rs." + value));
    }
    private static void applyCoupon() {
        System.out.print("Coupon code: "); String code = SC.nextLine().trim();
        double bill = readPositiveDouble("Bill amount: ");
        MANAGER.applyCoupon(code, bill, LocalDate.now());
    }
    private static void searchCoupon() {
        System.out.print("Coupon code: "); String code = SC.nextLine().trim();
        Coupon coupon = MANAGER.findCoupon(code);
        if (coupon == null) System.out.println("Coupon " + code.toUpperCase() + " not found."); else System.out.println(coupon);
    }
    private static void showCoupons() {
        List<Coupon> coupons = MANAGER.couponsInBSTOrder();
        if (coupons.isEmpty()) { System.out.println("No coupons available."); return; }
        printHeader(); coupons.forEach(System.out::println);
    }
    private static void bestCoupon() {
        double bill = readPositiveDouble("Bill amount: ");
        Coupon best = MANAGER.findBestCoupon(bill, LocalDate.now());
        if (best == null) System.out.println("No valid coupon available.");
        else {
            double saving = best.calculateDiscount(bill);
            best.recordUse();
            System.out.printf("Best discount applied: %.2f using %s | Final bill: %.2f%n", saving, best.getCode(), bill - saving);
        }
    }
    private static void sortedReport() {
        double bill = readPositiveDouble("Bill amount for comparison: ");
        List<Coupon> sorted = MANAGER.sortedByDiscount(bill);
        if (sorted.isEmpty()) { System.out.println("No coupons available."); return; }
        printHeader(); sorted.forEach(c -> System.out.printf("%s | Saving on Rs.%.2f: Rs.%.2f%n", c, bill, c.calculateDiscount(bill)));
    }
    private static void printHeader() { System.out.printf("%-14s %-10s %-12s %-12s %-12s %s%n", "CODE", "TYPE", "DISCOUNT", "START", "END", "USED/LIMIT"); }
    private static double readPositiveDouble(String prompt) {
        System.out.print(prompt); double n = Double.parseDouble(SC.nextLine().trim());
        if (!Double.isFinite(n) || n <= 0) throw new IllegalArgumentException("Enter a valid amount greater than zero."); return n;
    }
    private static int readPositiveInt(String prompt) {
        System.out.print(prompt); int n = Integer.parseInt(SC.nextLine().trim());
        if (n <= 0) throw new IllegalArgumentException("Enter a positive whole number."); return n;
    }
    private static LocalDate readDate(String prompt) {
        System.out.print(prompt);
        try { return LocalDate.parse(SC.nextLine().trim()); }
        catch (DateTimeParseException ex) { throw new IllegalArgumentException("Date must use YYYY-MM-DD format."); }
    }
}
