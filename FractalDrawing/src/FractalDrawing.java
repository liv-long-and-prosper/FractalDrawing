package FractalDrawing.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

/**
 * The type FractalDrawing.
 */
public class FractalDrawing extends JFrame implements FractalObserver {
    private int panelWidth;
    private int panelHeight;
    private FractalSubject subj;
    private ArrayList<FractalElement> fractalData;
    private JPanel mainPanel;

    /**
     * Creates the window where the fractal will be displayed
     *
     * @param subj The subject to observe
     * @throws UnsupportedLookAndFeelException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public FractalDrawing(FractalSubject subj) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        this.subj = subj;
        subj.add(this);

        setPanelWidth(600);
        setPanelHeight(600);

        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

        setTitle("Fractal Drawing");
        setSize(panelWidth,panelHeight);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setBackground(Color.BLACK);

        mainPanel = new DrawPanel();
        mainPanel.setLayout(null);
        mainPanel.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                setPanelWidth(mainPanel.getWidth());
                setPanelHeight(mainPanel.getHeight());
                update();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
                mainPanel.setVisible(true);
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                mainPanel.setVisible(false);
            }
        });
        getContentPane().add(mainPanel);

        setVisible(true);
    };

    @Override
    public void update() {
        fractalData = subj.getFractalData(panelWidth,panelHeight);
        mainPanel.repaint();
    }

    /**
     * Get panel width int.
     *
     * @return the int
     */
    public int getPanelWidth(){
        return panelWidth;
    }

    /**
     * Get panel height int.
     *
     * @return the int
     */
    public int getPanelHeight(){
        return panelHeight;
    }

    /**
     * Set panel width.
     *
     * @param width the width
     */
    public void setPanelWidth(int width){
        this.panelWidth = width;
    }

    /**
     * Set panel height.
     *
     * @param height the height
     */
    public void setPanelHeight(int height){
        this.panelHeight = height;
    }

    private class DrawPanel extends JPanel{
        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            mainPanel.setBackground(Color.black);
            for(FractalElement fractalElement : fractalData){
                fractalElement.draw(g);
            }
        }
    }
}
