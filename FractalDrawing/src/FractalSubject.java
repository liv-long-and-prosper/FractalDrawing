package FractalDrawing.src;

import java.awt.*;
import java.util.ArrayList;

/**
 * The FractalSubject interface
 */
public interface FractalSubject {

    /**
     * Add adds the given observer
     *
     * @param obs the observer
     */
    public void add(FractalObserver obs);

    /**
     * Remove removes the given observer
     *
     * @param obs the observer
     */
    public void remove(FractalObserver obs);

    /**
     * Notifies observers there has been a status change
     */
    public void notifyObservers();

    /**
     * SetOptions sets the values for the recursion depth, color opacity, and list of colors to use.
     *
     * @param depth   the depth
     * @param opacity the opacity
     * @param colors  the colors
     */
    public void setOptions(int depth, int opacity, Color[] colors);

    /**
     * GetFractalData returns an ArrayList of fractal elements
     *
     * @param width  the width
     * @param height the height
     * @return the fractal data
     */
    public ArrayList<FractalElement> getFractalData(int width, int height);

}