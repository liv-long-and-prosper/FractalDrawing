package FractalDrawing.src;

import java.util.ArrayList;

public interface FractalSubject {

    public void add(FractalObserver obs);
    public void remove(FractalObserver obs);
    public void notifyObservers();
    public void setOptions();
    public ArrayList<FractalElement> getFractalData();

}