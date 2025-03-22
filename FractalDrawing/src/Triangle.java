package FractalDrawing.src;

import java.awt.*;
import java.util.Random;

public record Triangle(int[] xCoordinates, int[] yCoordinates, Color[] colors) implements FractalElement{
    public Triangle{}

    @Override
    public void draw(Graphics g) {
        Color color;

        if(colors.length > 1) {
            Random random = new Random();
            color = colors[random.nextInt(colors.length)];
        } else {
            color = colors[0];
        }

        g.setColor(color);
        g.drawPolygon(xCoordinates, yCoordinates, 3);
    }
}
