package FractalDrawing.src;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class FractalGui extends JFrame {
    private final int STARTING_X = 25;
    private final int RECURSION_MIN = 1;
    private final int RECURSION_MAX = 8;
    private final int RECURSION_DEFAULT = 5;
    private final int OPACITY_MIN = 0;
    private final int OPACITY_MAX = 100;
    private final int OPACITY_DEFAULT = 60;
    private final int OPACITY_SLIDER_INCREMENTS = 10;
    private final int MAIN_PANEL_WIDTH = 400;
    private final int MAIN_PANEL_HEIGHT = 400;
    private final int SLIDER_PANEL_WIDTH = 350;
    private final int SLIDER_PANEL_HEIGHT = 100;
    private final Color DEFAULT_COLOR = new Color(255, 183, 206);

    private JPanel recursionSliderPanel;
    private JLabel recursionLabel;
    private JSlider recursionSlider;
    private int recursionVal;

    private JPanel opacitySliderPanel;
    private JLabel opacityLabel;
    private JSlider opacitySlider;
    private int opacityVal;

    private JButton fractalColor;
    private Color currentColor;

    private JButton drawFractal;

    private FractalSubject subj;

    public FractalGui(FractalSubject subj) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        this.subj = subj;

        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

        setTitle("Fractal Settings");
        setSize(MAIN_PANEL_WIDTH,MAIN_PANEL_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(null);
        getContentPane().add(mainPanel);

        recursionSliderPanel = new JPanel(new GridLayout(0,1));
        recursionSliderPanel.setBounds(STARTING_X,25, SLIDER_PANEL_WIDTH,SLIDER_PANEL_HEIGHT);
        mainPanel.add(recursionSliderPanel);

        recursionLabel = new JLabel("Recursion depth");
        recursionLabel.setHorizontalAlignment((int)0.5f);
        recursionLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        recursionSliderPanel.add(recursionLabel);

        recursionSlider = createSlider(RECURSION_MIN, RECURSION_MAX, RECURSION_DEFAULT, 1, false);
        recursionSliderPanel.add(recursionSlider);
        recursionSlider.addChangeListener(e -> {
            JSlider source = (JSlider)e.getSource();
            if(!source.getValueIsAdjusting()){
                recursionVal = source.getValue();
            }
        });

        opacitySliderPanel = new JPanel(new GridLayout(0,1));
        opacitySliderPanel.setBounds(STARTING_X,recursionSliderPanel.getY()+recursionSliderPanel.getHeight(),SLIDER_PANEL_WIDTH,SLIDER_PANEL_HEIGHT);
        mainPanel.add(opacitySliderPanel);

        opacityLabel = new JLabel("Circle opacity");
        opacityLabel.setHorizontalAlignment((int)0.5f);
        opacityLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        opacitySliderPanel.add(opacityLabel);

        opacitySlider = createSlider(OPACITY_MIN, OPACITY_MAX, OPACITY_DEFAULT, OPACITY_SLIDER_INCREMENTS, true);
        opacitySliderPanel.add(opacitySlider);
        opacitySlider.addChangeListener(e -> {
            JSlider source = (JSlider)e.getSource();
            if(!source.getValueIsAdjusting()){
                opacityVal = source.getValue();
            }
        });

        currentColor = DEFAULT_COLOR;
        JPanel chosenColor = new JPanel(null);
        chosenColor.setBounds(STARTING_X+100,opacitySliderPanel.getY()+opacitySliderPanel.getHeight()+25, 50, 20);
        chosenColor.setBackground(currentColor);
        mainPanel.add(chosenColor);

        fractalColor = new JButton("Fractal color...");
        fractalColor.setBounds(STARTING_X+100, chosenColor.getY()+chosenColor.getHeight(), 150, 30);
        mainPanel.add(fractalColor);
        fractalColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color initialColor = currentColor;
                Color currentColor = JColorChooser.showDialog(mainPanel, "Select a color", initialColor);
                chosenColor.setBackground(currentColor);
            }});

        drawFractal = new JButton("DRAW THE FRACTAL!");
        drawFractal.setBounds(STARTING_X+75,fractalColor.getY()+fractalColor.getHeight()+25,200,30);
        mainPanel.add(drawFractal);
        drawFractal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                subj.setOptions();
            }
        });

        setVisible(true);
    }

    public Color getCurrentColor(){
        return currentColor;
    }

    public int getRecursionVal(){
        return recursionVal;
    }

    public int getOpacityVal(){
        return opacityVal;
    }

    public JSlider createSlider(int sliderMin, int sliderMax, int sliderDefault, int spacing, boolean createStandardLabels){
        JSlider slider = new JSlider(sliderMin, sliderMax, sliderDefault);
        if(createStandardLabels){
            slider.createStandardLabels(spacing);
        }
        slider.setMajorTickSpacing(spacing);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);

        return slider;
    }
}