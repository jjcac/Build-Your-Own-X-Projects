/**
 * @author John Caceres
 * 
 * A class to repreent a drawn triangle in 3-dimensional space,
 * i.e. three vertices and a color.
 */

 import java.awt.Color;

public class Triangle {
    private Vertex v1;
    private Vertex v2;
    private Vertex v3;
    private Color color;

    public Triangle(Vertex v1, Vertex v2, Vertex v3, Color color) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.color = color;
    }

    public Vertex getV1() {
        return this.v1;
    }

    public Vertex getV2() {
        return this.v2;
    }

    public Vertex getV3() {
        return this.v3;
    }

    public Color getColor() {
        return this.color;
    }
}