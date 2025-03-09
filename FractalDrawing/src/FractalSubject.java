package FractalDrawing.src;

import java.awt.*;
import java.util.ArrayList;

public interface FractalSubject {

    public void add(FractalObserver obs);
    public void remove(FractalObserver obs);
    public void notifyObservers();
    public void setOptions(int depth, int opacity, Color color);
    public ArrayList<FractalElement> getFractalData(int width, int height);

}