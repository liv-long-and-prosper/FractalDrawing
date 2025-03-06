import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class FractalDrawing extends JFrame implements FractalObserver{

    private FractalSubject subject;
    private JPanel mainPanel;

    public FractalDrawing(FractalSubject subject){
        this.subject = subject;
        subject.attach(this);

        setSize();
        setTitle("Fractal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.WHITE);
        getContentPane().add(mainPanel);
        setVisible(true);
    }
    /**
     *
     */
    @Override
    public void update() {
        ArrayList<FractalElement> data = subject.getData();
        repaint();


    }
    private class DrawArea extends JPanel{
        Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Random rand = new Random();
            g.fillOval();
        }
    }
}
