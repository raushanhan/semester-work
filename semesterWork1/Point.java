public class Point {
    public double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point() {
        this(0, 0);
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public boolean equals(Point other) {
        return (x == other.x) && (y == other.y);
    }
}
