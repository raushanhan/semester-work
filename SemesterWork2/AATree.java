import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class AATree {

    private static int operationsCount;
    private static double startTime;
    private static double finishTime;
    private static int[] numbers = new int[10000];;
    private static AATree myTree;
    private Node head;

    public static void main(String[] args) throws IOException{
        myTree = addingTest();

        searchingTest();

        deletingTest();
    }

    public static AATree addingTest() throws IOException {
        try (FileWriter fw = new FileWriter("D:\\aisd\\SemesterWork2\\src\\main\\java\\addingResults.txt")) {
            AATree newTree = new AATree();
            int[] numbers = readTheFile();

            for (int num : numbers) {
                operationsCount = 0;
                startTime = System.nanoTime();

                newTree.add(num);

                finishTime = System.nanoTime();
                fw.write(operationsCount + "\t" + (int) (finishTime - startTime) + "\n");
            }
            return newTree;
        }
    }

    public static void searchingTest() throws IOException {
        try (FileWriter fw = new FileWriter("D:\\aisd\\SemesterWork2\\src\\main\\java\\searchingResults.txt")) {
            for (int i = 0; i < 100; i ++) {
                operationsCount = 0;
                startTime = System.nanoTime();

                myTree.find(numbers[rn()], myTree.getHead());

                finishTime = System.nanoTime();
                fw.write(operationsCount + "\t" + (int) (finishTime - startTime) + "\n");
            }
        }
    }

    public static int rn() {
        return (int) (Math.random() * 10000);
    }

    public static void deletingTest() throws IOException {
        try (FileWriter fw = new FileWriter("D:\\aisd\\SemesterWork2\\src\\main\\java\\deletingResults.txt")) {
            for (int i = 0; i < 1000; i ++) {
                operationsCount = 0;
                startTime = System.nanoTime();

                myTree.delete(numbers[rn()]);

                finishTime = System.nanoTime();
                fw.write(operationsCount + "\t" + (int) (finishTime - startTime) + "\n");
            }
        }

    }

    public static int[] readTheFile() {
        try {
            Scanner sc = new Scanner(new File("D:\\aisd\\SemesterWork2\\src\\main\\java\\generatedData.txt"));
            for (int i = 0; i < 10000; i++) {
                numbers[i] = sc.nextInt();
            }
            return numbers;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public AATree() {
        head = null;
    }

    private Node skew(Node t, boolean isTheHead) {
        operationsCount++;
        if (t == null) {
            return t;
        } else if (t.left == null) {
            return t;
        } else if (t.left.level == t.level) {
            Node res = new Node(t.left.value, t.left.level, t.left.left,
                    new Node(t.value, t.level, t.left.right, t.right));
            if (isTheHead) {
                head = res;
            }
            return res;
        } else return t;
    }

    private Node split(Node t, boolean isTheHead) {
        operationsCount++;
        if (t == null) {
            return null;
        } else if (t.right == null || t.right.right == null) {
            return t;
        } else if (t.level == t.right.right.level) {
            Node res = new Node(t.right.value, t.right.level + 1,
                    new Node(t.value, t.level, t.left, t.right.left),
                    t.right.right);
            if (isTheHead) {
                head = res;
            }
            return res;
        } else return t;
    }

    private Node insert(int x, Node t, boolean isTheHead) {
        operationsCount++;
        if (t == null) {
            Node node = new Node(x, 1, null, null);
            if (isTheHead) {
                head = node;
            }
            return node;
        } else if (x < t.value) {
            t.left = insert(x, t.left, false);
        } else if (x > t.value) {
            t.right = insert(x, t.right, false);
        }
        t = skew(t, isTheHead);
        t = split(t, isTheHead);
        return t;
    }

    public void add(int x) {
        insert(x, head, true);
    }

    public void delete(int x) {
        delete(x, head, true);
    }

    private Node decreaseLevel(Node t) {
        int leftLevel;
        int rightLevel;
        if (t.left == null) {
            leftLevel = t.level + 1;
        } else {
            leftLevel = t.left.level;
        }
        if (t.right == null) {
            rightLevel = t.level;
        } else {
            rightLevel = t.right.level;
        }
        int neededLevel = Math.min(leftLevel, rightLevel) + 1;
        if (neededLevel < t.level) {
            t.level = neededLevel;
            if (neededLevel < rightLevel) {
                if (t.right != null) {
                    t.right.level = neededLevel;
                }

            }
        }
        return t;
    }

    private Node delete(int x, Node t, boolean isTheHead) {
        if (t == null) {
            return t;
        } else if (x > t.value) {
            t.right = delete(x, t.right, false);
        } else if (x < t.value) {
            t.left = delete(x, t.left, false);
        } else {
            if (t.left == null && t.right == null) {
                return null;
            } else if (t.left == null) {
                Node l = successor(t.right);
                t.right = delete(l.value, t.right, false);
                t.value = l.value;
            } else {
                Node l = predecessor(t.left);
                t.left = delete(l.value, t.left, false);
                t.value = l.value;
            }
        }
        t = decreaseLevel(t);
        t = skew(t, isTheHead);
        t.right = skew(t.right, false);
        if (t.right != null) {
            t.right.right = skew(t.right.right, false);
        }
        t = split(t, isTheHead);
        t.right = split(t.right, false);
        return t;
    }

    public Node predecessor(Node t) {
        operationsCount++;
        if (t == null) {
            return null;
        } else if (t.left == null) {
            return t;
        } else {
            return predecessor(t.left);
        }
    }

    public Node successor(Node t) {
        operationsCount++;
        if (t == null) {
            return null;
        } else if (t.right == null) {
            return t;
        } else {
            return predecessor(t.right);
        }
    }

    public boolean find(int x, Node node) {
        operationsCount++;
        if (node == null) {
            return false;
        } else if (node.value == x) {
            return true;
        } else if (x > node.value) {
            return find(x, node.right);
        } else if (x < node.value) {
            return find(x, node.left);
        }
        return false;
    }

    public void print(Node node) {
        if (node != null) {
            System.out.println(node.value + " " + node.level);
            print(node.left);
            print(node.right);
        }
    }

    public Node getHead() {
        return head;
    }

}
