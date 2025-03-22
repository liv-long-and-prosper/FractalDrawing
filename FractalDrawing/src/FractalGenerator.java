package FractalDrawing.src;

import java.awt.*;
import java.util.ArrayList;

/**
 * The type FractalGenerator.
 */
public class FractalGenerator implements FractalSubject{
    private ArrayList<FractalObserver> observers;
    private int recursionDepth;
    private int opacity;
    private Color[] colors;
    private int[] xPoints;
    private int[] yPoints;
    private int[] circleCoordinates;


    /**
     * Creates a new FractalGenerator.
     */
    public FractalGenerator() {
        observers = new ArrayList<>();
    }

    @Override
    public void add(FractalObserver obs) {
        if (!observers.contains(obs)) {
            observers.add(obs);
        }
    }

    @Override
    public void remove(FractalObserver obs) {
        observers.remove(obs);
    }

    @Override
    public void notifyObservers() {
        for (FractalObserver obs : observers){
            obs.update();
        }
    }

    @Override
    public void setOptions(int recursionDepth, int opacity, Color[] colors) {
        this.recursionDepth = recursionDepth;
        this.opacity = opacity;
        this.colors = colors;
        notifyObservers();
    }


    @Override
    public ArrayList<FractalElement> getFractalData(int width, int height) {
        xPoints = new int[3];
        yPoints = new int[3];
        calculateTriangleCoordinates(width, height);
        ArrayList<FractalElement> fractalData = new ArrayList<>();
        drawRecursiveFractal(xPoints, yPoints, colors, recursionDepth, fractalData);
        return fractalData;
    }

    /**
     * DrawRecursiveFractal recursively creates fractal elements based on the given coordinates and recursion depth
     * Each recursive loop should create 4 triangles and 4 circles in the middle of said triangles.
     *
     * @param xPoints Array of x-coordinates for the current triangle x-points
     * @param yPoints Array of y-coordinates for the current triangle y-points
     * @param colors Array of colors to be used for drawing the fractal elements
     * @param recursionDepth The recursion depth
     * @param fractalData ArrayList to store the fractal elements
     */
    private void drawRecursiveFractal(int[] xPoints, int[] yPoints, Color[] colors, int recursionDepth, ArrayList<FractalElement> fractalData) {
        if (recursionDepth == 0) {
            return;
        } else {
            fractalData.add(new Triangle(xPoints, yPoints, colors));

            int[][] midPoints = new int[3][2];
            midPoints[0][0] = (xPoints[0] + xPoints[1]) / 2;
            midPoints[0][1] = (yPoints[0] + yPoints[1]) / 2;
            midPoints[1][0] = (xPoints[1] + xPoints[2]) / 2;
            midPoints[1][1] = (yPoints[1] + yPoints[2]) / 2;
            midPoints[2][0] = (xPoints[2] + xPoints[0]) / 2;
            midPoints[2][1] = (yPoints[2] + yPoints[0]) / 2;

            double[] circumcenter = calculateCircumcenter(
                    midPoints[0][0], midPoints[0][1],
                    midPoints[1][0], midPoints[1][1],
                    midPoints[2][0], midPoints[2][1]
            );

            double side1 = calculateDistance(midPoints[0][0], midPoints[0][1], midPoints[1][0], midPoints[1][1]);
            double side2 = calculateDistance(midPoints[1][0], midPoints[1][1], midPoints[2][0], midPoints[2][1]);
            double side3 = calculateDistance(midPoints[2][0], midPoints[2][1], midPoints[0][0], midPoints[0][1]);

            double sideAvg = (side1 + side2 + side3) / 2;

            double area = Math.sqrt(sideAvg * (sideAvg - side1) * (sideAvg - side2) * (sideAvg - side3));

            int radius = (int)Math.round(area / sideAvg);

            fractalData.add(new Circle((int) circumcenter[0], (int) circumcenter[1], radius, colors));

            int[][] t1Coordinates = {
                    {xPoints[0], midPoints[0][0], midPoints[2][0]},
                    {yPoints[0], midPoints[0][1], midPoints[2][1]}};
            int[][] t2Coordinates = {
                    {midPoints[0][0], xPoints[1], midPoints[1][0]},
                    {midPoints[0][1], yPoints[1], midPoints[1][1]}
            };
            int[][] t3Coordinates = {
                    {midPoints[2][0], midPoints[1][0], xPoints[2]},
                    {midPoints[2][1], midPoints[1][1], yPoints[2]}
            };

            drawRecursiveFractal(t1Coordinates[0], t1Coordinates[1], colors, recursionDepth - 1, fractalData);
            drawRecursiveFractal(t2Coordinates[0], t2Coordinates[1], colors, recursionDepth - 1, fractalData);
            drawRecursiveFractal(t3Coordinates[0], t3Coordinates[1], colors, recursionDepth - 1, fractalData);
        }
    }

    /**
     * CalculateCircumcenter calculates the circumcenter of a triangle to be used to position a circle within the triangle
     * If the three points are in a straight line, it finds the center point between them instead.
     *
     * @param x1 x-coordinate of the first point
     * @param y1 y-coordinate of the first point
     * @param x2 x-coordinate of the second point
     * @param y2 y-coordinate of the second point
     * @param x3 x-coordinate of the third point
     * @param y3 y-coordinate of the third point
     * @return An array of length 2 containing the x & y coordinates of the circumcenter
     */
    private double[] calculateCircumcenter(int x1, int y1, int x2, int y2, int x3, int y3) {
        double[] circumCenterCoordinates = new double[2];

        double distance1 = (x1 * x1) + (y1 * y1);
        double distance2 = (x2 * x2) + (y2 * y2);
        double distance3 = (x3 * x3) + (y3 * y3);

        double total = x1 * (y2 - y3) - y1 * (x2 - x3) + x2 * y3 - x3 * y2;

        if (Math.abs(total) < 0.001) {
            circumCenterCoordinates[0] = (x1 + x2 + x3) / 3.0;
            circumCenterCoordinates[1] = (y1 + y2 + y3) / 3.0;
        }else {
            double x = distance1 * (y2 - y3) + distance2 * (y3 - y1) + distance3 * (y1 - y2);
            double y = distance1 * (x3 - x2) + distance2 * (x1 - x3) + distance3 * (x2 - x1);

            circumCenterCoordinates[0] = x / (2 * total);
            circumCenterCoordinates[1] = y / (2 * total);
        }
        return circumCenterCoordinates;
    }

    /**
     * CalculateDistance calculates the distance between two points.
     *
     * @param x1 First x position
     * @param y1 First y position
     * @param x2 Second x position
     * @param y2 Second y position
     * @return the distance
     */
    private double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    /**
     * CalculateTriangleCoordinates generates the inital triangle's x & y coordinates
     *
     * @param width How wide the main panel is
     * @param height How tall the main panel is
     */
    private void calculateTriangleCoordinates(int width, int height){
        int topCenterX = width/2;
        int topCenterY = 0;

        xPoints[0] = topCenterX;
        yPoints[0] = topCenterY;

        xPoints[1] = topCenterX - width/2;
        yPoints[1] = topCenterY + height;

        xPoints[2] = topCenterX + width/2;
        yPoints[2] = topCenterY + height;
    }

}
