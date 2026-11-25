# Coupon & Discount Manager (PS No. 70)

A Java console application implementing a manually coded Binary Search Tree, HashMap lookup, greedy best-discount selection, merge sort reporting, date/usage validation, and file persistence.

## Requirements
- Java JDK 17 or newer
- Run commands from the project root (`CouponDiscountManager`)

## Compile
```bash
javac -d out src/*.java
```

## Run
```bash
java -cp out Main
```

The app reads `data/coupons.txt` at startup and saves updated usage counts on exit. Dates use `YYYY-MM-DD`.

## Data format
`code,type,value,startDate,endDate,usageLimit,usedCount`

Types are `PERCENT` and `FLAT`. Example:
`SAVE10,PERCENT,10.0,2026-01-01,2027-12-31,100,0`

## DSA notes
- `CouponBST`: manually implemented BST; in-order traversal displays coupons alphabetically by code.
- `CouponManager`: HashMap for average O(1) direct code lookup.
- Greedy: scans valid coupons and chooses the greatest saving for the entered bill.
- Merge sort: manually implemented, descending by saving for the entered bill.
- `CouponFileStore`: loads at startup and writes current usage counts at exit.

## Important behaviour
- Percentage discounts are calculated from the bill; flat discounts use their fixed amount.
- Discount is capped at the bill amount.
- Coupons are valid on both their start date and end date.
- Applying a coupon increments its usage count. The “Find best valid coupon” and report operations only preview; they do not consume a use.
- Best-coupon selection is greedy and does not consume the coupon; use “Apply coupon” to record a redemption.
