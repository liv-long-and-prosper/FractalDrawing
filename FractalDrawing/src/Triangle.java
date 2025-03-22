package FractalDrawing.src;

import java.awt.*;
import java.util.Random;

/**
 * The Triangle class
 *
 * @param xCoordinates Array of x-coordinates for the three x-points of the triangle
 * @param yCoordinates Array of y-coordinates for the three y-points of the triangle
 * @param colors Array of colors that can be used for drawing the triangle
 * @param colorIdx The index of the color to use from the colors array; this is calculated based on the triangle's position
 */
public record Triangle(int[] xCoordinates, int[] yCoordinates, Color[] colors, int colorIdx) implements FractalElement{
    // generate a random seed to maintain results
    private static final Random random = new Random(42);

    /**
     * Generates a Triangle with the color being based upon the triangle's position
     *
     * @param xCoordinates Array of x-coordinates for the three x-points of the triangle
     * @param yCoordinates Array of y-coordinates for the three y-points of the triangle
     * @param colors Array of colors that can be used for drawing the triangle
     */
    public Triangle(int[] xCoordinates, int[] yCoordinates, Color[] colors) {
        int idx = 0;
        // if the colors array has more than one color in it, choose a color based upon where the triangle is located
        if (colors.length > 1) {
            idx = Math.abs(xCoordinates[0] + yCoordinates[0]) % colors.length;
        }
        this(xCoordinates, yCoordinates, colors, idx);
    }

    public Triangle{}

    @Override
    public void draw(Graphics g) {
        g.setColor(colors[colorIdx]);
        g.drawPolygon(xCoordinates, yCoordinates, 3);
    }
}
