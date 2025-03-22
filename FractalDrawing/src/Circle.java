package FractalDrawing.src;

import java.awt.*;
import java.util.Random;

public record Circle(int x, int y, int radius, Color[] colors, int colorIdx) implements FractalElement{
    private static final Random random = new Random(42);

    public Circle(int x, int y, int radius, Color[] colors){
        int idx = 0;
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
