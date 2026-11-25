/** A manually implemented Binary Search Tree ordered by coupon code. */
public class CouponBST {
    private static class Node {
        Coupon coupon; Node left, right;
        Node(Coupon coupon) { this.coupon = coupon; }
    }
    private Node root;

    public void insert(Coupon coupon) { root = insert(root, coupon); }
    private Node insert(Node node, Coupon coupon) {
        if (node == null) return new Node(coupon);
        int cmp = coupon.getCode().compareTo(node.coupon.getCode());
        if (cmp < 0) node.left = insert(node.left, coupon);
        else if (cmp > 0) node.right = insert(node.right, coupon);
        else throw new IllegalArgumentException("Duplicate coupon code: " + coupon.getCode());
        return node;
    }

    public Coupon search(String code) {
        Node current = root;
        while (current != null) {
            int cmp = code.compareTo(current.coupon.getCode());
            if (cmp == 0) return current.coupon;
            current = cmp < 0 ? current.left : current.right;
        }
        return null;
    }

    public void inOrder(java.util.List<Coupon> result) { inOrder(root, result); }
    private void inOrder(Node node, java.util.List<Coupon> result) {
        if (node == null) return;
        inOrder(node.left, result); result.add(node.coupon); inOrder(node.right, result);
    }
}
