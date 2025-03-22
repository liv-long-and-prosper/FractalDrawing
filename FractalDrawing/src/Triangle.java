package FractalDrawing.src;

import java.awt.*;
import java.util.Random;

public record Triangle(int[] xCoordinates, int[] yCoordinates, Color[] colors, int colorIdx) implements FractalElement{
    private static final Random random = new Random(42);

    public Triangle(int[] xCoordinates, int[] yCoordinates, Color[] colors) {
        int idx = 0;
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
