package FractalDrawing.src;

import java.awt.*;

public record Circle(int x, int y, int radius, Color color) implements FractalElement{
    public Circle{}

    @Override
    public void draw(Graphics g) {
        int diameter = radius*2;
        g.setColor(color);
        g.fillOval(x-(radius), y-(radius), diameter, diameter);
    }

}
