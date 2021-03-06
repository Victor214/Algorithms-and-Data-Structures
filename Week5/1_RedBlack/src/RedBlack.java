public class RedBlack<Key extends Comparable<Key>, Value> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;
    private Node root;

    private class Node {
        private Key key;
        private Value val;
        private Node left, right;
        private int count;
        boolean color;

        public Node(Key key, Value val, boolean color) {
            this.key = key;
            this.val = val;
            this.color = color;
            this.count = 1;
        }
    }

    private boolean isRed(Node x) {
        if (x == null) return false;
        return x.color == RED;
    }

    // Balances and makes red black only-left-leaning-reds property remain true.
    private Node rotateLeft(Node h) {
        assert isRed(h.right);
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    // Reverts the rotateLeft operation, used temporarily for balance.
    private Node rotateRight(Node h) {
        assert isRed(h.left);
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    // Makes a black node with left and right red-links red, and both red-links black (flips colors)
    private void flipColors(Node h) {
        assert !isRed(h);
        assert isRed(h.left);
        assert isRed(h.right);
        h.color = RED;
        h.left.color = BLACK;
        h.right.color = BLACK;
    }

    public Value get(Key key) {
        Node x = root;
        while (x != null) {
            int compare = key.compareTo(x.key);
            if (compare < 0)
                x = x.left;
            else if (compare > 0)
                x = x.right;
            else
                return x.val;
        }
        return null;
    }

    private Node put(Node h, Key key, Value val) {
        if (h == null)
            return new Node(key, val, RED);
        int compare = key.compareTo(h.key);
        if (compare < 0) h.left = put(h.left, key, val);
        else if (compare > 0) h.right = put(h.right, key, val);
        else h.val = val; // Key already exists, substitute it simply

        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        h.count = 1 + size(h.left) + size(h.right); // Keeps track of its size. A given tree's size is itself (-1), plus the size of each child tree.
        return h;
    }

    public boolean isEmpty(Key key) {
        return size() == 0;
    }

    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        return x.count;
    }
}
