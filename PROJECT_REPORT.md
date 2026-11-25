# PROJECT REPORT
## Coupon & Discount Manager — PS No. 70

### Abstract
Coupon & Discount Manager is a Java console application that stores discount coupons with unique codes, discount types, validity dates and usage limits. It uses a manually implemented Binary Search Tree to organise coupon records, a HashMap for direct code lookup, a greedy strategy to select the valid coupon giving the highest saving, merge sort to prepare discount reports, and file handling to preserve records between runs.

### 1. Introduction
Discount coupons are commonly used to reduce the amount payable on a bill. Managing coupon codes manually can lead to duplicate codes, expired coupons, overuse and incorrect discount calculations. This project provides a small system to manage coupons and apply eligible discounts safely.

### 2. Problem Statement
Build a Java program to manage coupons with unique codes, validity dates and usage limits. Store coupons in a BST, use hashing for direct lookup, choose the best valid discount using a greedy comparison, sort reports, and persist coupon information in `data/coupons.txt`. A coupon is valid only within its date range and while uses remain. A discount must never exceed the bill amount.

### 3. Objectives
- Add and search coupons using unique codes.
- Reject duplicate codes and invalid coupon data.
- Validate date range and remaining uses before redemption.
- Support percentage and flat discounts.
- Select the greatest available saving for a bill.
- Sort coupon reports using merge sort.
- Load records at startup and save usage updates on exit.

### 4. Modules
1. `Coupon`: stores coupon properties, validation and discount calculation.
2. `CouponBST`: manually implemented BST with insertion and in-order traversal.
3. `CouponManager`: coordinates BST, HashMap, greedy selection and merge sort.
4. `CouponFileStore`: reads and writes `coupons.txt`.
5. `Main`: console menu and input/output.

### 5. Algorithms
**BST insertion:** Compare each new coupon code lexicographically with the current node. Move left for a smaller code and right for a greater code. Reject equal codes. In-order traversal displays records in ascending code order.

**Hash lookup:** Convert the entered code to uppercase and retrieve the coupon from the HashMap by key. Average lookup complexity is O(1).

**Greedy selection:** Visit all coupons. Ignore coupons outside their date range or with no remaining uses. Calculate each eligible coupon's saving, capped at the bill amount. Keep the coupon with the greatest saving. Redeem it and increment its usage count.

**Merge sort:** Divide the report list into halves, recursively sort each half, then merge by descending saving for the entered bill. Time complexity is O(n log n).

### 6. Data Format
Each non-comment line in `data/coupons.txt` contains seven comma-separated fields:
`code,type,value,startDate,endDate,usageLimit,usedCount`

Example:
`SAVE10,PERCENT,10.0,2026-01-01,2027-12-31,100,0`

Dates use ISO format `YYYY-MM-DD`. Types are `PERCENT` and `FLAT`.

### 7. Test Cases
| Test | Input / Scenario | Expected result |
|---|---|---|
| Normal | Add SAVE10 with 10% | Coupon added: SAVE10 | 10% |
| Boundary | Apply valid SAVE10 to bill 100 | Discount: 10 | Final bill: 90 |
| Invalid | Apply BAD99 | Coupon BAD99 not found. |
| Best coupon | 10% or flat 15 on bill 100 | Best discount: 15 | Final bill: 85 |

### 8. Complexity Analysis
| Operation | Average / expected complexity |
|---|---|
| HashMap code lookup | O(1) average |
| BST insertion/search | O(log n) average; O(n) worst case |
| BST in-order traversal | O(n) |
| Greedy best-coupon scan | O(n) |
| Merge sort report | O(n log n) time; O(n) auxiliary space |
| File load/save | O(n) records, excluding filesystem overhead |

### 9. Advantages
- Prevents duplicate coupon codes.
- Checks expiry and usage limits.
- Prevents discount from exceeding the bill.
- Uses multiple required DSA concepts in meaningful roles.
- Saves coupon usage updates between program runs.

### 10. Applications
The same logic can be adapted for retail billing, online shopping coupon systems, promotional campaigns and small point-of-sale applications.

### 11. Limitations and Assumptions
- This is a console-based, single-user demonstration, not a networked production service.
- Currency is represented using `double` for a simple academic demonstration; production billing should use `BigDecimal`.
- The data file is a simple comma-separated format; coupon codes are restricted to letters, digits, underscore and hyphen.
- The best-coupon action redeems the selected coupon. The sorted report is informational and does not consume coupon uses.

### 12. Conclusion
The project demonstrates how BST, hashing, greedy selection, merge sort and file handling can work together in a practical coupon-management problem. It validates coupon eligibility, calculates safe discounts and preserves usage information.

### 13. References
- Oracle Java documentation: https://docs.oracle.com/en/java/
- Java Collections Framework documentation: https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/package-summary.html
