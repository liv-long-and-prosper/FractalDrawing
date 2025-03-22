package FractalDrawing.src;

import java.awt.*;
import java.util.Random;

/**
 * The Circle class
 *
 * @param x The x-coordinate of the circle
 * @param y The y-coordinate of the circle
 * @param radius The radius of the circle
 * @param colors Array of colors that can be used for drawing the circle
 * @param colorIdx The index of the color to use from the colors array; this is calculated based on position
 */
public record Circle(int x, int y, int radius, Color[] colors, int colorIdx) implements FractalElement{
    // generate a random seed to maintain results
    private static final Random random = new Random(42);

    /**
     * Generates a Circle object with the color based on its position
     *
     * @param x The x-coordinate of the circle
     * @param y The y-coordinate of the circle
     * @param radius The radius of the circle
     * @param colors Array of colors that can be used for drawing the circle
     */
    public Circle(int x, int y, int radius, Color[] colors){
        int idx = 0;
        // if the colors array has more than one color in it, choose a color based upon where the circle is located
        if (colors.length > 1) {
            idx = Math.abs(x + y) % colors.length;
        }
        this(x, y, radius, colors, idx);
    }

    public Circle{}

    @Override
    public void draw(Graphics g) {
        int diameter = radius*2;
        g.setColor(colors[colorIdx]);
        g.fillOval(x-(radius), y-(radius), diameter, diameter);
    }

}
