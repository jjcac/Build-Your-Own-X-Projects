/**
 * @author John Caceres
 * 
 * A class to represent any 3x3 matrix, used for Vertex (vector) transformation.
 * 
 * The 'matrix' is actually implemented and handled as an array of double
 * values, where the first 3 values represent the first row, the next 3 
 * represent the 2nd row, and the final 3 represent the 3rd row.
 * 
 * For matrix multiplication, the input matrix is assumed to be multipled on the right,
 * with the instance matrix calling the method assumed to be on the left of the multiplication.
 * 
 * For Vertex transformation, the vertex is assumed to be on the left.
 * 
 * You can think of the instance Matrix as being between a Vertex on the left and a matrix
 * on the right, where the transformation of a Vertex occurs on the left, so any subsequent
 * transformations must occur after the current one, from the right.
 */
public class Matrix3x3 {
    private double[] values;

    public Matrix3x3(double[] values) {
        this.values = values;
    }

    public Matrix3x3 multiply(Matrix3x3 right) {
        double[] result = new double[9];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                for (int i = 0; i < 3; i++) {
                    result[row * 3 + col] += this.values[row * 3 + i] * right.values[i * 3 + col];
                }
            }
        }
        return new Matrix3x3(result);
    }

    public Vertex transform(Vertex left) {
        return new Vertex(
            left.getX() * values[0] + left.getY() * values[3] + left.getZ() * values[6],
            left.getX() * values[1] + left.getY() * values[4] + left.getZ() * values[7],
            left.getX() * values[2] + left.getY() * values[5] + left.getZ() * values[8]);
    }

    public double[] getValues() {
        return values;
    }
}
