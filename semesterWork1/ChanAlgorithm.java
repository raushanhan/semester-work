import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.ArrayList;

public class ChanAlgorithm {

    private static Scanner sc;

    private static int mm;

    private static double startTime;

    private static double finishTime;
    private static int iterationsCount;

    public static void main(String[] args) throws IOException {
        File f = new File("D:\\aisd\\SemesterWork\\src\\main\\java\\results.txt");
        File supposed = new File("D:\\aisd\\SemesterWork\\src\\main\\java\\supposedResults.txt");
        try (FileWriter fw = new FileWriter("D:\\aisd\\SemesterWork\\src\\main\\java\\results.txt")) {
            try (FileWriter supFW = new FileWriter("D:\\aisd\\SemesterWork\\src\\main\\java\\supposedResults.txt")) {
                for (int i = 100; i <= 10000; i = i + 100) {
                    PointsSet s = start(i);
                    fw.write(i + " " + iterationsCount + " " + (int) (finishTime - startTime) + "\n");
                    supFW.write(i + " " +  (int) (i * Math.log(s.size)) + "\n");
                }
            }

        }
    }

    public static PointsSet start(int n) {
        try {
            sc = new Scanner(new File("D:\\aisd\\SemesterWork\\src\\main\\java\\generatedData.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        iterationsCount = 0;
        PointsSet ps = createListOfPoints(n);
        startTime = System.currentTimeMillis();

        return chanAlg1(ps);
    }

    public static PointsSet chanAlg1(PointsSet ps) {
        int n = ps.ps.length;
        int m;

        for (int t = 1; t < n; t++) {
            iterationsCount = 0;
            startTime = System.currentTimeMillis();
            m = (int) Math.min(t * 2, n);
            PointsSet L = chanAlg2(ps, m);
            if (L != null) {
                return L;
            }
        }
        return null;
    }

    public static PointsSet chanAlg2(PointsSet ps, int m) {

        PointsSet[] psArr = breakIntoGroups(ps, m);

        for (int i = 0; i < psArr.length; i++) {
            iterationsCount++;
            psArr[i] = grahamScan(psArr[i]);
        }

        return JarvisAlg(psArr, ps, m);
    }

    public static PointsSet[] breakIntoGroups(PointsSet ps, int m) {
        int r = ps.size / m;

        PointsSet[] result;
        int leftOvers = ps.size - m * r;
        boolean noPointsLeft = leftOvers == 0;
        if (!noPointsLeft) {
            result = new PointsSet[r + 1];
        } else {
            result = new PointsSet[r];
        }

        int j = 0;
        for (int i = 0; i < r; i++) {
            //iterationsCount++;
            result[i] = new PointsSet(m);
            while (j < m * (i + 1)) {
                //iterationsCount++;
                result[i].add(ps.ps[j]);
                j++;
            }
        }

        if (!noPointsLeft) {
            result[result.length - 1] = new PointsSet(leftOvers);
            while (j < ps.size) {
                //iterationsCount++;
                result[result.length - 1].add(ps.ps[j]);
                j++;
            }
        }

        return result;
    }

    public static PointsSet createListOfPoints(int n) {
        PointsSet ps;

        ps = new PointsSet(n);

        for (int i = 0; i < n; i++) {
            double x = Double.parseDouble(sc.next());
            double y = Double.parseDouble(sc.next());
            sc.nextLine();
            ps.add(x, y);
        }

        return ps;
    }

    public static PointsSet grahamScan(PointsSet ps) {
        Point[] A = ps.ps;

        int n = A.length;
        int[] P = IntStream.range(0, n).toArray();

        for (int i = 1; i < n; i++) {
            iterationsCount++;
            if (A[P[i]].x < A[P[0]].y) {
                int temp = P[i];
                P[i] = P[0];
                P[0] = temp;
            }
        }

        for (int i = 2; i < n; i++) {
            iterationsCount++;
            int j = i;
            while (j > 1 && rotate(A[P[0]], A[P[j - 1]], A[P[j]]) < 0) {
                iterationsCount++;
                int temp = P[j];
                P[j] = P[j - 1];
                P[j - 1] = temp;
                j--;
            }
        }

        List<Point> S = new ArrayList<>();
        S.add(A[P[0]]);
        S.add(A[P[1]]);
        for (int i = 2; i < n; i++) {
            iterationsCount++;
            while (rotate(S.get(S.size() - 2), S.get(S.size() - 1), A[P[i]]) < 0) {
                iterationsCount++;
                S.remove(S.size() - 1);
            }
            S.add(A[P[i]]);
        }

        return new PointsSet(S);
    }

    public static double rotate(Point first, Point second, Point third) {
        return (second.x - first.x) * (third.y - second.y) - (second.y - first.y) * (third.x - second.x);
    }

    public static double cosOfAScalarProduct(Point first, Point second, Point third) {
        if (second.x == third.x) return 1;
        return ((first.x - second.x) * (third.x - second.x) + (first.y - second.y) * (third.y - second.y)) /
                Math.sqrt((first.x - second.x) * (first.x - second.x) + (first.y - second.y) * (first.y - second.y)) / // поделил на длину первого
                Math.sqrt((third.x - second.x) * (third.x - second.x) + (third.y - second.y) * (third.y - second.y)); // поделил на длину второго
    }

    public static PointsSet JarvisAlg(PointsSet[] psArr, PointsSet ps, int m) {

        Point p0 = ps.p0;

        List<Point> result = new ArrayList<>();
        result.add(p0);
        Point prev_p = new Point(0, p0.y);
        Point curr_p = p0;

        for (int i = 0; i < m; i++) {
            iterationsCount++;
            Point q = result.get(result.size() - 1); // eto budet tochka q s maksimalnym uglom
            for (int j = 0; j < psArr.length; j++) {
                iterationsCount++;
                for (Point elem : psArr[j].ps) {
                    iterationsCount++;
                    if (!q.equals(elem)) {
                        if (cosOfAScalarProduct(prev_p, curr_p, elem) <= cosOfAScalarProduct(prev_p, curr_p, q)) {
                            q = elem;
                        }
                    }
                }
            }
            if (q.equals(p0)) {
                finishTime = System.currentTimeMillis();
                mm = m;
                return new PointsSet(result);
            }
            result.add(q);
            prev_p = curr_p;
            curr_p = q;
        }

        return null;
    }
}
