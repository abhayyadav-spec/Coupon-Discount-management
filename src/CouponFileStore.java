import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

/** Reads and writes coupon records in CSV-like coupons.txt format. */
public class CouponFileStore {
    private final Path file;
    public CouponFileStore(String filename) { file = Paths.get(filename); }

    public List<Coupon> load() throws IOException {
        List<Coupon> coupons = new ArrayList<>();
        if (!Files.exists(file)) {
            Path parent = file.toAbsolutePath().getParent();
            if (parent != null) Files.createDirectories(parent);
            Files.createFile(file);
            return coupons;
        }
        int lineNumber = 0;
        for (String line : Files.readAllLines(file)) {
            lineNumber++;
            if (line.isBlank() || line.startsWith("#")) continue;
            String[] p = line.split(",", -1);
            if (p.length != 7) throw new IOException("Invalid data at line " + lineNumber + ". Expected 7 comma-separated fields.");
            try {
                coupons.add(new Coupon(p[0], Coupon.DiscountType.valueOf(p[1]), Double.parseDouble(p[2]),
                        LocalDate.parse(p[3]), LocalDate.parse(p[4]), Integer.parseInt(p[5]), Integer.parseInt(p[6])));
            } catch (RuntimeException ex) {
                throw new IOException("Invalid coupon at line " + lineNumber + ": " + ex.getMessage(), ex);
            }
        }
        return coupons;
    }

    public void save(Collection<Coupon> coupons) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("# code,type,value,startDate,endDate,usageLimit,usedCount");
        for (Coupon coupon : coupons) lines.add(coupon.toFileLine());
        Path parent = file.toAbsolutePath().getParent();
        if (parent != null) Files.createDirectories(parent);
        Files.write(file, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}
