public class Node {

    public int value;
    public int level;
    public Node left, right;
    public Node (int value, int level, Node left, Node right) {
        this.value = value;
        this.level = level;
        this.left = left;
        this.right = right;
    }

    public String toString() {
        return value + " " + level;
    }

}
