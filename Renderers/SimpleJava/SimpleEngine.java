/**
 * @author John Caceres
 * 
 * A runnable class that serves as the main class for a basic 3D render engine.
 * 
 * This project covers basic 3D rendering with orthographic projection,
 * simple triangle rasterization, z-buffering, and flat shading.
 * 
 * For more complex engines, OpenGL is recommended, and there are
 * Java libraries that allow OpenGL to be worked with.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SimpleEngine {
    
    // To start, our GUI wrapper is handled in the main method.
    public static void main(String[] args) {
        // Setup frame and pane
        JFrame frame = new JFrame();
        Container pane = frame.getContentPane();
        pane.setLayout(new BorderLayout());

        // Slider to control horizontal rotation
        JSlider headingSlider = new JSlider(-180, 180, 0);
        pane.add(headingSlider, BorderLayout.SOUTH);

        // Slider to control vertical rotation
        JSlider pitchSlider = new JSlider(SwingConstants.VERTICAL, -90, 90, 0);
        pane.add(pitchSlider, BorderLayout.EAST);

        // Panel to display render results
        JPanel renderPanel = new JPanel() {
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Rendering magic happens below.
                
                // Start adding triangles:
                ArrayList<Triangle> tris = new ArrayList<>();
                tris.add(new Triangle(new Vertex(100, 100, 100),
                                      new Vertex(-100, -100, 100),
                                      new Vertex(-100, 100, -100),
                                      Color.WHITE));
                tris.add(new Triangle(new Vertex(100, 100, 100),
                                      new Vertex(-100, -100, 100),
                                      new Vertex(100, -100, -100),
                                      Color.RED));
                tris.add(new Triangle(new Vertex(-100, 100, -100),
                                      new Vertex(100, -100, -100),
                                      new Vertex(100, 100, 100),
                                      Color.GREEN));
                tris.add(new Triangle(new Vertex(-100, 100, -100),
                                      new Vertex(100, -100, -100),
                                      new Vertex(-100, -100, 100),
                                      Color.BLUE));

                // Use matrices to transform view with sliders
                // Heading slider functionality:
                double heading = Math.toRadians(headingSlider.getValue());
                Matrix3x3 headingTransformer = new Matrix3x3(new double[] {
                    Math.cos(heading), 0, -Math.sin(heading),
                    0, 1, 0,
                    Math.sin(heading), 0, Math.cos(heading)
                });
                // Pitch slider functionality:
                double pitch = Math.toRadians(pitchSlider.getValue());
                Matrix3x3 pitchTransformer = new Matrix3x3(new double[] {
                    1, 0, 0,
                    0, Math.cos(pitch), Math.sin(pitch),
                    0, -Math.sin(pitch), Math.cos(pitch)
                });
                // Combine matrices
                Matrix3x3 combinedTransformer = headingTransformer.multiply(pitchTransformer);

                // Create image to be specified/filled
                BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

                double[] zBuffer = new double[image.getWidth() * image.getHeight()];
                // Initialize with extremely far away depths
                for (int i = 0; i < zBuffer.length; i++) {
                    zBuffer[i] = Double.NEGATIVE_INFINITY;
                }
                // Draw triangles
                for (Triangle t : tris) {
                    // Transform first to set up corrected image
                    Vertex v1 = combinedTransformer.transform(t.getV1());
                    Vertex v2 = combinedTransformer.transform(t.getV2());
                    Vertex v3 = combinedTransformer.transform(t.getV3());

                    // To fill in the triangles, we will rasterize (convert to a list of pixels)
                    // via barycentric coordinates. Real 3d engines use hardware rasterization,
                    // which is much more efficient, but we can't access the graphic card here,
                    // so we will do it manually.
                    
                    // Since we are manually assessing each triangle, we must manually
                    // translate the vertices to be centered first.
                    v1.setX(v1.getX() + getWidth() / 2);
                    v1.setY(v1.getY() + getHeight() / 2);
                    v2.setX(v2.getX() + getWidth() / 2);
                    v2.setY(v2.getY() + getHeight() / 2);
                    v3.setX(v3.getX() + getWidth() / 2);
                    v3.setY(v3.getY() + getHeight() / 2);

                    // Compute rectangular bounds for triangle
                    int minX = (int) Math.max(0, Math.ceil(Math.min(v1.getX(), Math.min(v2.getX(), v3.getX()))));
                    int maxX = (int) Math.min(image.getWidth() - 1, Math.floor(Math.max(v1.getX(), Math.max(v2.getX(), v3.getX()))));
                    int minY = (int) Math.max(0, Math.ceil(Math.min(v1.getY(), Math.min(v2.getY(), v3.getY()))));
                    int maxY = (int) Math.min(image.getHeight() - 1, Math.floor(Math.max(v1.getY(), Math.max(v2.getY(), v3.getY()))));

                    double triangleArea = (v1.getY() - v3.getY()) * (v2.getX() - v3.getX())
                        + (v2.getY() - v3.getY()) * (v3.getX() - v1.getX());

                    // Color pixels of visible triangles
                    for (int y = minY; y <= maxY; y++) {
                        for (int x = minX; x <= maxX; x++) {
                            double b1 = ((y - v3.getY()) * (v2.getX() - v3.getX()) + (v2.getY() - v3.getY()) * (v3.getX() - x)) / triangleArea;
                            double b2 = ((y - v1.getY()) * (v3.getX() - v1.getX()) + (v3.getY() - v1.getY()) * (v1.getX() - x)) / triangleArea;
                            double b3 = ((y - v2.getY()) * (v1.getX() - v2.getX()) + (v1.getY() - v2.getY()) * (v2.getX() - x)) / triangleArea;
                            if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
                                // Handle z-buffer
                                double depth = b1 * v1.getZ() + b2 * v2.getZ() + b3 * v3.getZ();
                                int zIndex = y * image.getWidth() + x;
                                if (zBuffer[zIndex] < depth) {
                                    image.setRGB(x, y, t.getColor().getRGB());
                                    zBuffer[zIndex] = depth;
                                }
                            }
                        }
                    }
                }

                g2.drawImage(image, 0, 0, null);
            }
        };
        pane.add(renderPanel, BorderLayout.CENTER);

        // Add listeners for sliders
        // NOTE TO SELF: The arrow operator is used for lambda expressions.
        // In this case, the lambda expression accepts some parameter e but doesn't necessarily use it.
        headingSlider.addChangeListener(e -> renderPanel.repaint());
        pitchSlider.addChangeListener(e -> renderPanel.repaint());

        frame.setSize(800, 800);
        frame.setVisible(true);
    }
}