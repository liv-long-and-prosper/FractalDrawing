package FractalDrawing.src;

import java.util.ArrayList;

public class FractalGenerator implements FractalSubject{
    private ArrayList<FractalObserver> observers;


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
        if(observers.contains(obs)) {
            observers.remove(obs);
        }
    }

    @Override
    public void notifyObservers() {
        for (FractalObserver obs : observers){
            obs.update();
        }
    }

    @Override
    public void setOptions() {
        notifyObservers();
    }

    @Override
    public ArrayList<FractalElement> getFractalData() {
        //TODO: add width and height parameters
        //TODO: hella shit
        return null;
    }
}
