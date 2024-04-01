import java.util.List;

public class PointsSet {

    public Point[] ps;
    public int size;

    public Point p0;

    public PointsSet(int length) {
        ps = new Point[length];
        size = 0;
    }

    public PointsSet(List<Point> arr) {
        ps = new Point[arr.size()];
        arr.toArray(ps);
        size = arr.size();
    }

    public void add(double x, double y) {
        Point newP = new Point(x, y);
        if (p0 == null) {
            p0 = newP;
        } else if (newP.y < p0.y || (newP.y == p0.y && newP.x < p0.x)) {
            p0 = newP;
        }
        ps[size] = newP;
        size++;
    }

    public void add(Point p) {
        ps[size] = p;
        size++;
    }

    public String toString() {
        String res = "{";
        boolean firstPrinting = true;
        for (Point p : ps) {
            if (!firstPrinting) {
                res += ", ";
            }
            res += p;
        }
        res += "}";
        return res;
    }

}
