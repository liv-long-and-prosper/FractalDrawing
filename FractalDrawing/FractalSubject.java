package FractalDrawing;

public interface FractalSubject {
  import java.util.ArrayList;

public interface FractalSubject {
    public void attach(FractalObserver observer);

    public void detach(FractalObserver observer);

    public void notifyObservers();

    public ArrayList<FractalElement> getData();

    public void setOptions();
}
}
