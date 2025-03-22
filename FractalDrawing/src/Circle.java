package FractalDrawing.src;

import java.awt.*;
import java.util.Random;

public record Circle(int x, int y, int radius, Color[] colors) implements FractalElement{
    public Circle{}

    @Override
    public void draw(Graphics g) {
        Color color;

        if(colors.length > 1) {
            Random random = new Random();
            color = colors[random.nextInt(colors.length)];
        } else {
            color = colors[0];
        }

        int diameter = radius*2;
        g.setColor(color);
        g.fillOval(x-(radius), y-(radius), diameter, diameter);
    }

}
