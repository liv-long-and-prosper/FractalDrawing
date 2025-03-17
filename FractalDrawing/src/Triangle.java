package FractalDrawing.src;

import java.awt.*;

public record Triangle(int[] xCoordinates, int[] yCoordinates, Color color) implements FractalElement{
    public Triangle{}

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.drawPolygon(xCoordinates, yCoordinates, 3);
    }
}
