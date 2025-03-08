package FractalDrawing.src;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class FractalDrawing extends JFrame implements FractalObserver {
    private final int DEFAULT_PANEL_WIDTH = 600;
    private final int DEFAULT_PANEL_HEIGHT = 600;

    private FractalSubject subj;
    private JPanel mainPanel;

    public FractalDrawing(FractalSubject subj) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        this.subj = subj;
        subj.add(this);

        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

        setTitle("Fractal Drawing");
        setSize(DEFAULT_PANEL_WIDTH,DEFAULT_PANEL_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setBackground(Color.BLACK);

        mainPanel = new DrawPanel();
        mainPanel.setLayout(null);
        getContentPane().add(mainPanel);

        setVisible(true);

    };

    @Override
    public void update() {
        ArrayList<FractalElement> fractalData = subj.getFractalData();
        mainPanel.repaint();
    }


    private class DrawPanel extends JPanel{
        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            Random rand = new Random();
            g.setColor(new Color(rand.nextInt(100), rand.nextInt(200), rand.nextInt(150)));
            g.fillOval(rand.nextInt(550), rand.nextInt(550), 50,50);
        }
    }
}
