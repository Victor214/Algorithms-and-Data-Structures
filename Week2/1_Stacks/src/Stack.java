public class Stack {
    private Node first = null;

    private class Node {
        String value;
        Node next;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public void push(String value) {
        Node newfirst = new Node();
        newfirst.value = value;
        newfirst.next = first;
        first = newfirst;
    }

    public String pop() {
        Node oldfirst = first;
        first = first.next;
        return oldfirst.value;
    }

    public static void main(String[] args) {
        Stack stack = new Stack();
        for (String arg : args) {
            if (arg.equals("-"))
                System.out.println(stack.pop());
            else
                stack.push(arg);
        }
    }
}
