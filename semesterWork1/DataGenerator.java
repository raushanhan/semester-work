import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

public class DataGenerator {

    public static void main(String[] args) throws IOException {
        File f = new File("D:\\aisd\\SemesterWork\\src\\main\\java\\generatedData.txt");
        try (FileWriter fw = new FileWriter("D:\\aisd\\SemesterWork\\src\\main\\java\\generatedData.txt")) {
            for (int i = 0; i < 10000; i++) {
                fw.write((Math.random() * 200 - 100) + " " + (Math.random() * 200 - 100) + "\n");
            }
        }
    }
}
