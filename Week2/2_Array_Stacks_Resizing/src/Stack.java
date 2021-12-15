public class Stack {
    String[] s;
    int n = 0;

    public Stack() {
        s = new String[1];
    }

    public void push(String item) {
        if (s.length == n)
            resize(2 * s.length);
        s[n++] = item;
    }

    public String pop() {
        if (n == 0)
            return null;

        String item = s[--n];
        s[n] = null;
        if (n > 0 && n == s.length / 4)
            resize(s.length / 2);

        return item;

    }

    private void resize(int size) {
        String[] newarray = new String[size];
        for (int i = 0; i < n; i++)
            newarray[i] = s[i];
        s = newarray;
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
