import java.time.LocalDate;

/** Represents one discount coupon. */
public class Coupon {
    public enum DiscountType { PERCENT, FLAT }

    private final String code;
    private final DiscountType type;
    private final double value;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final int usageLimit;
    private int usedCount;

    public Coupon(String code, DiscountType type, double value,
                  LocalDate startDate, LocalDate endDate,
                  int usageLimit, int usedCount) {
        if (code == null || !code.trim().matches("[A-Za-z0-9_-]+")) throw new IllegalArgumentException("Coupon code must contain only letters, numbers, underscore or hyphen.");
        if (type == null) throw new IllegalArgumentException("Discount type is required.");
        if (!Double.isFinite(value) || value <= 0) throw new IllegalArgumentException("Discount value must be positive.");
        if (type == DiscountType.PERCENT && value > 100) throw new IllegalArgumentException("Percentage cannot exceed 100.");
        if (startDate == null || endDate == null || endDate.isBefore(startDate)) throw new IllegalArgumentException("Invalid date range.");
        if (usageLimit < 1 || usedCount < 0 || usedCount > usageLimit) throw new IllegalArgumentException("Invalid usage count/limit.");
        this.code = code.trim().toUpperCase();
        this.type = type;
        this.value = value;
        this.startDate = startDate;
        this.endDate = endDate;
        this.usageLimit = usageLimit;
        this.usedCount = usedCount;
    }

    public String getCode() { return code; }
    public DiscountType getType() { return type; }
    public double getValue() { return value; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public int getUsageLimit() { return usageLimit; }
    public int getUsedCount() { return usedCount; }
    public int getRemainingUses() { return usageLimit - usedCount; }

    public boolean isValid(LocalDate today) {
        return !today.isBefore(startDate) && !today.isAfter(endDate) && usedCount < usageLimit;
    }

    public double calculateDiscount(double billAmount) {
        double raw = type == DiscountType.PERCENT ? billAmount * value / 100.0 : value;
        return Math.min(billAmount, raw);
    }

    public void recordUse() {
        if (usedCount >= usageLimit) throw new IllegalStateException("Coupon usage limit reached.");
        usedCount++;
    }

    public String toFileLine() {
        return String.join(",", code, type.name(), Double.toString(value), startDate.toString(),
                endDate.toString(), Integer.toString(usageLimit), Integer.toString(usedCount));
    }

    @Override public String toString() {
        String discount = type == DiscountType.PERCENT ? value + "%" : "Rs." + String.format("%.2f", value);
        return String.format("%-14s %-10s %-12s %-12s %-12s %5d/%-5d", code, type, discount,
                startDate, endDate, usedCount, usageLimit);
    }
}
