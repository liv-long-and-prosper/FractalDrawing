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

/**
 * The type Fractal GUI.
 */
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

    /**
     * Creates the settings window with sliders for controlling the recursion depth and opacity as well as
     * a dropdown for color themes, and buttons for selecting the fractal color and generating random fractals
     *
     * @param subj The subject that will generate the fractal elements
     * @throws UnsupportedLookAndFeelException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
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

        // Create the root panel to add components to
        JPanel mainPanel = new JPanel(null);
        getContentPane().add(mainPanel);

        // Create a panel for the slider to be used for recursion depth
        recursionSliderPanel = new JPanel(new GridLayout(0,1));
        recursionSliderPanel.setBounds(STARTING_X,25, SLIDER_PANEL_WIDTH,SLIDER_PANEL_HEIGHT);
        mainPanel.add(recursionSliderPanel);

        recursionLabel = new JLabel("Recursion depth");
        recursionLabel.setHorizontalAlignment((int)0.5f);
        recursionLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        recursionSliderPanel.add(recursionLabel);

        // Create the recursion slider
        recursionSlider = createSlider(RECURSION_MIN, RECURSION_MAX, RECURSION_DEFAULT, 1, false);
        recursionSliderPanel.add(recursionSlider);
        recursionSlider.addChangeListener(e -> {
                recursionVal = recursionSlider.getValue();
                subj.setOptions(recursionVal, opacityVal, colorThemes.get(selectedTheme));
        });

        // Create a panel to be used for opacity level
        opacitySliderPanel = new JPanel(new GridLayout(0,1));
        opacitySliderPanel.setBounds(STARTING_X,recursionSliderPanel.getY()+recursionSliderPanel.getHeight(),SLIDER_PANEL_WIDTH,SLIDER_PANEL_HEIGHT);
        mainPanel.add(opacitySliderPanel);

        opacityLabel = new JLabel("Circle opacity");
        opacityLabel.setHorizontalAlignment((int)0.5f);
        opacityLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        opacitySliderPanel.add(opacityLabel);

        // Create opacity slider
        opacitySlider = createSlider(OPACITY_MIN, OPACITY_MAX, OPACITY_DEFAULT, OPACITY_SLIDER_INCREMENTS, true);
        opacitySliderPanel.add(opacitySlider);
        opacitySlider.addChangeListener(e -> {
            if(!opacitySlider.getValueIsAdjusting()) {
                // Get the desired opacity level & calculate the corresponding alpha value
                opacityVal = opacitySlider.getValue();
                int alphaVal = (opacityVal * 255) / 100;

                // For each theme, loop through the colors in the theme and set the opacity level
                for(String theme : colorThemes.keySet()){
                    Color[] colors = colorThemes.get(theme);
                    if (colors != null){
                        for (int i = 0; i < colors.length; i++){
                            Color current = colors[i];
                            colors[i] = new Color(current.getRed(),current.getGreen(),current.getBlue(), alphaVal);
                        }
                    }
                }

                // Set the current solid color's opacity
                currentColor = new Color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), alphaVal);
                colorThemes.put("Solid", new Color[]{currentColor});

                updateColorPreview();
                subj.setOptions(recursionVal, opacityVal, colorThemes.get(selectedTheme));
            }
        });

        // Create and add the themeSelector if colorThemes isn't empty or null and themeNames has a length greater than 1
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
                    // Change selectedTheme to the desired theme if it is not null
                    if(themeSelector.getSelectedItem() != null){
                        selectedTheme = (String) themeSelector.getSelectedItem();
                        fractalColor.setEnabled(selectedTheme.equals("Solid")); // disable/enable color chooser button depending on selectedTheme
                    }
                    updateColorPreview();
                    subj.setOptions(recursionVal, opacityVal, colorThemes.get(selectedTheme));
                }
            });
        }

        // Create panel to display current colors being used
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
                // Change the currentColor to be the chosen color
                Color initialColor = currentColor;
                currentColor = JColorChooser.showDialog(mainPanel, "Select a color", initialColor);

                // Set currentColor to the desired color with the selected opacity
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
        // Generate a random fractal whenever this button is pressed
        // Will only generate a random fractal with the solid theme
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
        });

        updateColorPreview();
        subj.setOptions(RECURSION_DEFAULT, OPACITY_DEFAULT, colorThemes.get(selectedTheme));
        setVisible(true);
    }

    /**
     * GetColorThemes reads color theme data from a file and generates an array of theme names
     * and a hashmap of String objects for theme names as keys and corresponding Color[] of colors per theme as values
     *
     * @param scanner a scanner object
     * @return a String array of theme names
     */ 
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

    /**
     * UpdateColorPreview updates the color preview panel to show the current colors.
     * In the case of the Solid theme, it shows just one color.
     * For other themes, it shows all the colors corresponding to that theme.
     */
    private void updateColorPreview(){
        JPanel colorPanel;

        // clears the panel
        colorPreviewPanel.removeAll();
        themeColorPanels.clear();

        /* if selectedTheme has not been chosen in the case of the Themes.txt file being unreadable
            or if the selectedTheme is solid - then the colorPreviewPanel will only display the one color
        */
        if( selectedTheme == null || selectedTheme.equals("Solid")){
            colorPanel = new JPanel();
            colorPanel.setPreferredSize(new Dimension(50, 30));
            colorPanel.setBackground(currentColor);
            colorPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            colorPreviewPanel.add(colorPanel);
            themeColorPanels.add(colorPanel);
        }else{
            /*
            * otherwise add each color that corresponds to the selectedTheme
            * */
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

        // Each row should have a maximum of 6 panels
        int rows = Math.ceilDiv(themeColorPanels.size(), 6);
        int height = rows*35;
        int y;

        // calculate the starting y coordinate based upon whether the themeSelector exists or not
        if ( themeSelector != null ){
            y = themeSelector.getY()+themeSelector.getHeight()+25;
        }else{
            y = opacitySlider.getY()+opacitySlider.getHeight()+25;
        }

        colorPreviewPanel.setBounds(STARTING_X, y, SLIDER_PANEL_WIDTH, height);
        colorPreviewPanel.revalidate();
        colorPreviewPanel.repaint();

        // reposition components added after the colorPreviewPanel
        repositionComponents();
    }

    /**
     * RepositionComponents adjusts the position of the other components when needed to ensure they do not overlap
     */
    private void repositionComponents(){
        fractalColor.setBounds(STARTING_X+100, colorPreviewPanel.getY()+colorPreviewPanel.getHeight()+10, 150, 30);
        drawFractal.setBounds(STARTING_X+75, fractalColor.getY()+fractalColor.getHeight()+25, 200,30);

        // adjust the main panel height to fit the drawFractal button in case the colorPreviewPanel gets too large
        int minMainPanelHeight = drawFractal.getY()+drawFractal.getHeight()+50;
        if (minMainPanelHeight > MAIN_PANEL_HEIGHT){
            setSize(MAIN_PANEL_WIDTH, minMainPanelHeight);
        }
    }

    /**
     * CreateSlider creates a slider with the given specifications; helper method to avoid code replication
     *
     * @param sliderMin The minimum value
     * @param sliderMax The maximum value
     * @param sliderDefault The starting value
     * @param spacing The distance between tick marks
     * @param createStandardLabels Whether number labels need to be specified or not
     * @return a JSlider
     */
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