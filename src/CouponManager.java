import java.time.LocalDate;
import java.util.*;

/** Coordinates BST storage, HashMap lookup, greedy selection and sorting. */
public class CouponManager {
    private final CouponBST bst = new CouponBST();
    private final Map<String, Coupon> index = new HashMap<>();

    public void addCoupon(Coupon coupon) {
        if (index.containsKey(coupon.getCode())) throw new IllegalArgumentException("Duplicate coupon code: " + coupon.getCode());
        bst.insert(coupon);
        index.put(coupon.getCode(), coupon);
    }
    public Coupon findCoupon(String code) { return index.get(code.trim().toUpperCase()); }
    public Collection<Coupon> allCoupons() { return Collections.unmodifiableCollection(index.values()); }
    public List<Coupon> couponsInBSTOrder() {
        List<Coupon> result = new ArrayList<>(); bst.inOrder(result); return result;
    }

    /** Greedy choice: select the valid coupon that saves the greatest amount. */
    public Coupon findBestCoupon(double bill, LocalDate today) {
        Coupon best = null; double bestSaving = 0;
        for (Coupon coupon : index.values()) {
            if (!coupon.isValid(today)) continue;
            double saving = coupon.calculateDiscount(bill);
            if (saving > bestSaving) { bestSaving = saving; best = coupon; }
        }
        return best;
    }

    public void applyCoupon(String code, double bill, LocalDate today) {
        Coupon coupon = findCoupon(code);
        if (coupon == null) throw new IllegalArgumentException("Coupon " + code.toUpperCase() + " not found.");
        if (!coupon.isValid(today)) throw new IllegalStateException("Coupon is expired, not yet valid, or has no uses remaining.");
        double discount = coupon.calculateDiscount(bill);
        coupon.recordUse();
        System.out.printf("Discount: %.2f | Final bill: %.2f%n", discount, bill - discount);
    }

    /** Manual merge sort, descending by calculated discount for the supplied bill. */
    public List<Coupon> sortedByDiscount(double bill) {
        List<Coupon> list = new ArrayList<>(index.values());
        mergeSort(list, 0, list.size() - 1, bill);
        return list;
    }
    private void mergeSort(List<Coupon> a, int left, int right, double bill) {
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        mergeSort(a, left, mid, bill); mergeSort(a, mid + 1, right, bill);
        List<Coupon> temp = new ArrayList<>(); int i = left, j = mid + 1;
        while (i <= mid && j <= right) {
            if (compare(a.get(i), a.get(j), bill) >= 0) temp.add(a.get(i++));
            else temp.add(a.get(j++));
        }
        while (i <= mid) temp.add(a.get(i++));
        while (j <= right) temp.add(a.get(j++));
        for (int k = 0; k < temp.size(); k++) a.set(left + k, temp.get(k));
    }
    private int compare(Coupon x, Coupon y, double bill) {
        int bySaving = Double.compare(x.calculateDiscount(bill), y.calculateDiscount(bill));
        return bySaving != 0 ? bySaving : x.getCode().compareTo(y.getCode());
    }
}
