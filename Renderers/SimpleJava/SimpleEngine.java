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
                
                // Prepare to draw
                g2.translate(getWidth() / 2, getHeight() / 2);
                g2.setColor(Color.WHITE);

                // Draw triangles
                for (Triangle t : tris) {
                    Path2D path = new Path2D.Double();
                    path.moveTo(t.getV1().getX(), t.getV1().getY());
                    path.lineTo(t.getV2().getX(), t.getV2().getY());
                    path.lineTo(t.getV3().getX(), t.getV3().getY());
                    path.closePath();
                    g2.draw(path);
                }
            }
        };
        pane.add(renderPanel, BorderLayout.CENTER);

        frame.setSize(800, 800);
        frame.setVisible(true);
    }
}