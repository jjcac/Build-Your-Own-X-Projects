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

                // Rendering magic will happen here
            }
        };
        pane.add(renderPanel, BorderLayout.CENTER);

        frame.setSize(800, 800);
        frame.setVisible(true);
    }
}