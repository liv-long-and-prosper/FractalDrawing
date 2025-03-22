package FractalDrawing.src;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.Collections;
import javax.swing.*;

public class FractalGui extends JFrame {
    private final int STARTING_X = 25;
    private final int RECURSION_MIN = 1;
    private final int RECURSION_MAX = 8;
    private final int RECURSION_DEFAULT = 5;
    private final int OPACITY_MIN = 0;
    private final int OPACITY_MAX = 100;
    private static final int OPACITY_DEFAULT = 60;
    private final int OPACITY_SLIDER_INCREMENTS = 10;
    private final int MAIN_PANEL_WIDTH = 400;
    private final int MAIN_PANEL_HEIGHT = 550;
    private final int SLIDER_PANEL_WIDTH = 350;
    private final int SLIDER_PANEL_HEIGHT = 100;
    private final int THEME_SELECTOR_HEIGHT = 35;
    private static final Color DEFAULT_COLOR = new Color(255, 183, 206, OPACITY_DEFAULT);

    private JPanel recursionSliderPanel;
    private JLabel recursionLabel;
    private JSlider recursionSlider;
    private int recursionVal;

    private JPanel opacitySliderPanel;
    private JLabel opacityLabel;
    private JSlider opacitySlider;
    private int opacityVal;

    private JComboBox<String> themeSelector;
    private JLabel themeSelectorLabel;
    private String selectedTheme = "Solid";

    private JPanel colorPreviewPanel;
    private ArrayList<JPanel> themeColorPanels = new ArrayList<>();

    private JButton fractalColor;
    private Color currentColor;

    private JButton drawFractal;

    private FractalSubject subj;

    private static HashMap<String, Color[]> colorThemes;
    private static String[] themeNames;

    static {
        colorThemes = new HashMap<>();

        try {
            File colorData = new File("FractalDrawing/src/Themes.txt");
            Scanner scanner = new Scanner(colorData);
            themeNames = getColorThemes(scanner);
            scanner.close();
        } catch (Exception e) {
            System.err.println("Problem loading theme file: " + e.getMessage());
            themeNames = new String[]{"Solid"};
        }

        if (!colorThemes.containsKey("Solid")) {
            colorThemes.put("Solid", new Color[]{DEFAULT_COLOR});
        }
    }

    public FractalGui(FractalSubject subj) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        this.subj = subj;
        this.recursionVal = RECURSION_DEFAULT;
        this.opacityVal = OPACITY_DEFAULT;
        this.currentColor = DEFAULT_COLOR;

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
                recursionVal = recursionSlider.getValue();
                subj.setOptions(recursionVal, opacityVal, colorThemes.get(selectedTheme));
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
            if(!opacitySlider.getValueIsAdjusting()) {
                opacityVal = opacitySlider.getValue();
                int alphaVal = (opacityVal * 255) / 100;

                for(String theme : colorThemes.keySet()){
                    Color[] colors = colorThemes.get(theme);
                    if (colors != null){
                        for (int i = 0; i < colors.length; i++){
                            Color current = colors[i];
                            colors[i] = new Color(current.getRed(),current.getGreen(),current.getBlue(), alphaVal);
                        }
                    }
                }

                currentColor = new Color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), alphaVal);
                colorThemes.put("Solid", new Color[]{currentColor});

                updateColorPreview();
//                chosenColor.setBackground(currentColor);
                subj.setOptions(recursionVal, opacityVal, colorThemes.get(selectedTheme));
            }
        });

        if(colorThemes != null && !colorThemes.isEmpty() && themeNames.length > 1) {
            themeSelectorLabel = new JLabel("Color theme");
            themeSelectorLabel.setHorizontalAlignment(0);
            themeSelectorLabel.setBounds(STARTING_X, opacitySliderPanel.getY() + opacitySliderPanel.getHeight() + 25, SLIDER_PANEL_WIDTH, THEME_SELECTOR_HEIGHT);
            mainPanel.add(themeSelectorLabel);

            themeSelector = new JComboBox<>(themeNames);
            themeSelector.setBounds(STARTING_X, themeSelectorLabel.getY() + themeSelectorLabel.getHeight(), SLIDER_PANEL_WIDTH, THEME_SELECTOR_HEIGHT);
            mainPanel.add(themeSelector);
            themeSelector.setSelectedItem("Solid");

            themeSelector.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(themeSelector.getSelectedItem() != null){
                        selectedTheme = (String) themeSelector.getSelectedItem();
                        fractalColor.setEnabled(selectedTheme.equals("Solid"));
                    }
                    updateColorPreview();
                    subj.setOptions(recursionVal, opacityVal, colorThemes.get(selectedTheme));
                }
            });
        }

        colorPreviewPanel = new JPanel();
        colorPreviewPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));
        mainPanel.add(colorPreviewPanel);

        currentColor = DEFAULT_COLOR;
        fractalColor = new JButton("Fractal color...");
        fractalColor.setBounds(STARTING_X+100, colorPreviewPanel.getY()+colorPreviewPanel.getHeight(), 150, 30);
        fractalColor.setEnabled(selectedTheme.equals("Solid"));
        mainPanel.add(fractalColor);

        fractalColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color initialColor = currentColor;
                currentColor = JColorChooser.showDialog(mainPanel, "Select a color", initialColor);

                if(currentColor != null) {
                    int alphaVal = (opacityVal * 255) / 100;
                    currentColor = new Color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), alphaVal);

                    Color[] solidColorArray = new Color[]{currentColor};
                    colorThemes.put("Solid", solidColorArray);

                    updateColorPreview();
                    subj.setOptions(recursionVal, opacityVal, solidColorArray);
                }
            }});

        drawFractal = new JButton("DRAW THE FRACTAL!");
        drawFractal.setBounds(STARTING_X+75,fractalColor.getY()+fractalColor.getHeight()+25,200,30);
        mainPanel.add(drawFractal);
        drawFractal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Random rand = new Random();

                int randomDepth = rand.nextInt(1,9);
                recursionSlider.setValue(randomDepth);

                int randomOpacity = rand.nextInt(0,101);
                opacitySlider.setValue(randomOpacity);

                int randomAlpha = (randomOpacity*255)/100;
                currentColor = new Color(rand.nextInt(0,256), rand.nextInt(0,256), rand.nextInt(0,256), randomAlpha);

                if (currentColor != null) {
                    Color[] randomColorArray = new Color[]{currentColor};
                    colorThemes.put("Solid", randomColorArray);

                    selectedTheme = "Solid";
                    if (themeSelector != null) {
                        themeSelector.setSelectedItem(selectedTheme);
                    }

                    fractalColor.setEnabled(true);
                    updateColorPreview();
                    subj.setOptions(randomDepth, randomOpacity, randomColorArray);
                }
            }
        });

        updateColorPreview();
        subj.setOptions(RECURSION_DEFAULT, OPACITY_DEFAULT, colorThemes.get(selectedTheme));
        setVisible(true);
    }

    private static String[] getColorThemes(Scanner scanner){
        scanner.nextLine();

        ArrayList<String> themeNames = new ArrayList<>();
        HashMap<String, ArrayList<Color>> themes = new HashMap<>();

        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] fields = line.split("~");

            if (fields.length >= 3) {
                String themeName = fields[0];
                String rgb = fields[2];

                String[] rgbVals = rgb.split(",");
                if (rgbVals.length >= 3) {
                    int r = Integer.parseInt(rgbVals[0].trim());
                    int g = Integer.parseInt(rgbVals[1].trim());
                    int b = Integer.parseInt(rgbVals[2].trim());

                    Color color = new Color(r, g, b);
                    if (!themes.containsKey(themeName)) {
                        themeNames.add(themeName);
                        themes.put(themeName, new ArrayList<>());
                    }
                    themes.get(themeName).add(color);
                }
            }
        }

        colorThemes = new HashMap<>();
        for(String theme : themes.keySet()){
            Color[] colors = themes.get(theme).toArray(new Color[0]);
            colorThemes.put(theme, colors);
        }

        Collections.sort(themeNames);
        themeNames.add(0,"Solid");
        return themeNames.toArray(new String[0]);
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

    private void updateColorPreview(){
        colorPreviewPanel.removeAll();
        themeColorPanels.clear();
        JPanel colorPanel;

        if( selectedTheme == null || selectedTheme.equals("Solid")){
            colorPanel = new JPanel();
            colorPanel.setPreferredSize(new Dimension(50, 30));
            colorPanel.setBackground(currentColor);
            colorPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            colorPreviewPanel.add(colorPanel);
            themeColorPanels.add(colorPanel);
        }else{
            Color[] themeColors = colorThemes.get(selectedTheme);

            if(themeColors != null){
                for(Color color : themeColors){
                    colorPanel = new JPanel();
                    colorPanel.setPreferredSize(new Dimension(30, 30));
                    colorPanel.setBackground(color);
                    colorPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    colorPreviewPanel.add(colorPanel);
                    themeColorPanels.add(colorPanel);
                }
            }
        }

        int rows = Math.ceilDiv(themeColorPanels.size(), 6);
        int height = rows*35;
        int y;
        if ( themeSelector != null ){
            y = themeSelector.getY()+themeSelector.getHeight()+25;
        }else{
            y = opacitySlider.getY()+opacitySlider.getHeight()+25;
        }
        colorPreviewPanel.setBounds(STARTING_X, y, SLIDER_PANEL_WIDTH, height);
        colorPreviewPanel.revalidate();
        colorPreviewPanel.repaint();

        repositionComponents();
    }

    private void repositionComponents(){
        fractalColor.setBounds(STARTING_X+100, colorPreviewPanel.getY()+colorPreviewPanel.getHeight()+10, 150, 30);
        drawFractal.setBounds(STARTING_X+75, fractalColor.getY()+fractalColor.getHeight()+25, 200,30);

        int minMainPanelHeight = drawFractal.getY()+drawFractal.getHeight()+50;
        if (minMainPanelHeight > MAIN_PANEL_HEIGHT){
            setSize(MAIN_PANEL_WIDTH, minMainPanelHeight);
        }
    }

    private JSlider createSlider(int sliderMin, int sliderMax, int sliderDefault, int spacing, boolean createStandardLabels){
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