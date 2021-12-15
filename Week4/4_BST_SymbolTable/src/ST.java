import edu.princeton.cs.algs4.Queue;

// BST-based implementation of Symbol Tables
public class ST<Key extends Comparable<Key>, Value> {
    private Node root;

    private class Node {
        private Key key;
        private Value val;
        private Node left, right;
        private int count;

        public Node(Key key, Value val, int size) {
            this.key = key;
            this.val = val;
            this.count = size;
        }
    }

    public ST() {
        root = null;
    }

    public void put(Key key, Value val) {
        root = put(root, key, val);
    }

    // Traverses the tree, finding the correct position to place the new key. Returns x (itself) at the end for already existing nodes so it won't modify non-null pointers.
    private Node put(Node x, Key key, Value val) {
        if (x == null)
            return new Node(key, val, 1);
        int compare = key.compareTo(x.key);
        if (compare < 0)
            x.left = put(x.left, key, val);
        else if (compare > 0)
            x.right = put(x.right, key, val);
        else
            x.val = val; // Key already exists, substitute it simply
        x.count = 1 + size(x.left) + size(x.right); // Keeps track of its size. A given tree's size is itself (-1), plus the size of each child tree.
        return x;
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

    public void delete(Key key) {
        root = delete(root, key);
    }

    private Node delete(Node x, Key key) {
        if (x == null)
            return null;
        int compare = key.compareTo(x.key);
        if (compare < 0)
            x.left = delete(x.left, key);
        else if (compare > 0)
            x.right = delete(x.right, key);
        else {
            if (x.right == null)
                return x.left;
            if (x.left == null)
                return x.right;

            // In case it has both children, replace with successor
            Node t = x;
            x = min(t.right);
            x.right = deleteMin(t.right);
            x.left = t.left;
        }
        x.count = 1 + size(x.left) + size(x.right);
        return x;
    }

    public void deleteMin() {
        root = deleteMin(root);
    }

    private Node deleteMin(Node x) {
        if (x.left == null)
            return x.right;
        x.left = deleteMin(x.left);
        x.count = 1 + size(x.left) + size(x.right);
        return x;
    }

    public void deleteMax() {
        root = deleteMax(root);
    }

    private Node deleteMax(Node x) {
        if (x.right == null)
            return x.left;
        x.right = deleteMax(x.right);
        x.count = 1 + size(x.left) + size(x.right);
        return x;
    }

    public boolean contains(Key key) {
        return get(key) != null;
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

    public Key min() {
        return min(root).key;
    }

    private Node min(Node x) {
        if (x.left == null)
            return x;
        return min(x.left);
    }

    public Key max() {
        return max(root).key;
    }

    private Node max(Node x) {
        if (x.right == null)
            return x;
        return max(x.right);
    }

    public Key floor(Key key) {
        Node x = floor(root, key);
        if (x == null) return null;
        return x.key;
    }

    private Node floor(Node x, Key key) {
        if (x == null)
            return null;
        int compare = key.compareTo(x.key);
        if (compare == 0) // Equal
            return x;
        if (compare < 0) // Left
            return floor(x.left, key);
        Node t = floor(x.right, key); // Right
        if (t != null) // Checks if there is a valid floor (<= key) on the right branch of the tree.
            return t; // If there is, return it
        else
            return x; // Else, its the root itself.
    }

    public Key ceiling(Key key) {
        Node x = ceiling(root, key);
        if (x == null) return null;
        return x.key;
    }

    private Node ceiling(Node x, Key key) {
        if (x == null)
            return null;
        int compare = key.compareTo(x.key);
        if (compare == 0) // Equal
            return x;
        if (compare > 0) // Right
            return ceiling(x.right, key);

        Node t = ceiling(x.left, key); // Left
        if (t != null) // Checks if there is a valid floor (>= key) on the left branch of the tree.
            return t; // If there is, return it
        else
            return x; // Else, its the root itself.
    }

    public int rank(Key key) {
        return rank(key, root);
    }

    private int rank(Key key, Node x) {
        if (x == null)
            return 0;
        int compare = key.compareTo(x.key);
        if (compare < 0) // Amount of keys less than key is a subgroup of the left side, therefore a recursive call must be made to find it.
            return rank(key, x.left);
        else if (compare > 0) // If a key is greater than the current node, then it means all left-side nodes, including the current one, is less than key, so they all must be counted, plus all that are less than key on the right handside (hence the recursive call)
            return 1 + size(x.left) + rank(key, x.right);
        else // Perfect (easy) scenario where given key is the current node, therefore, by definition, all keys that are less than key are on the leftside of the tree.
            return size(x.left);
    }


    public Iterable<Key> keys() {
        Queue<Key> q = new Queue<Key>();
        inorder(root, q);
        return q;
    }

    private void inorder(Node x, Queue<Key> q) {
        if (x == null)
            return;
        inorder(x.left, q);
        q.enqueue(x.key);
        inorder(x.right, q);
    }

    public static void main(String[] args) {
        ST<String, Integer> st = new ST<>();


        st.put("apple", 5);
        st.put("carrot", 9);
        System.out.println("Size : " + st.size());

        st.put("dragonfruit", 7);
        st.put("banana", 3);
        System.out.println("Size : " + st.size());


        System.out.println("Value of dragonfruit : " + st.get("dragonfruit"));
        System.out.println("Value of banana : " + st.get("banana"));
        System.out.println("Value of avocado : " + st.get("avocado"));

        System.out.println("Min : " + st.min());
        System.out.println("Max : " + st.max());


        System.out.println("Floor of dandellion   : " + st.floor("dandellion"));
        System.out.println("Ceiling of avocado : " + st.ceiling("avocado"));

        System.out.println("Rank of carrot    : " + st.rank("dragonfruit"));

        System.out.println("Inorder : ");
        for (String s : st.keys()) {
            System.out.println(s);
        }

        st.delete("carrot");
        System.out.println("Size : " + st.size());
        System.out.println("Inorder : ");
        for (String s : st.keys()) {
            System.out.println(s);
        }

    }
}
