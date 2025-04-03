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
import java.util.ArrayList;

public class SimpleEngine {
    
    // To start, our GUI wrapper is handled in the main method.
    public static void main(String[] args) {
        // Setup frame and pane
        JFrame frame = new JFrame();
        Container pane = frame.getContentPane();
        pane.setLayout(new BorderLayout());

        // Slider to control horizontal rotation
        JSlider headingSlider = new JSlider(0, 360, 180);
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

                // Rendering magic will happen below.
                
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
                
                // Prepare to draw
                g2.translate(getWidth() / 2, getHeight() / 2);
                g2.setColor(Color.WHITE);

                // Draw triangles
                for (Triangle t : tris) {
                    // Transform first to set up corrected image
                    Vertex v1 = combinedTransformer.transform(t.getV1());
                    Vertex v2 = combinedTransformer.transform(t.getV2());
                    Vertex v3 = combinedTransformer.transform(t.getV3());
                    // Paint corrected image
                    Path2D path = new Path2D.Double();
                    path.moveTo(v1.getX(), v1.getY());
                    path.lineTo(v2.getX(), v2.getY());
                    path.lineTo(v3.getX(), v3.getY());
                    path.closePath();
                    g2.draw(path);
                }
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