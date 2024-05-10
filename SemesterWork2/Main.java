import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        AATree myTree = new AATree();
        int[] numbers = new int[]{3, 6, 5, 8, 2, 7, 14, -3, 4};
        for (int num : numbers) {
            myTree.add(num);
        }
        myTree.print(myTree.getHead());

        System.out.println(myTree.find(3, myTree.getHead()));

        System.out.println(myTree.find(19, myTree.getHead()));
        myTree.print(myTree.getHead());

        myTree.delete(5);

        System.out.println();

        myTree.print(myTree.getHead());

    }
}
