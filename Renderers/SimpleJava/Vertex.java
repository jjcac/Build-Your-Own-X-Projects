/**
 * @author John Caceres
 * 
 * A class to represent a point in 3-dimensional space.
 * 
 * The x-coordinate represents left-right movement, y up-down,
 * and z depth, i.e. positive z represents moving towards the observer.
 */

public class Vertex {
    private double x;
    private double y;
    private double z;

    public Vertex(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }
}