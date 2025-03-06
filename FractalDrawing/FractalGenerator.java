import java.util.ArrayList;

public class FractalGenerator implements FractalSubject{

    private ArrayList<FractalObserver> observers;

    public FractalGenerator() {
        observers = new ArrayList<>();
    }

    /**
     * @param observer
     */
    @Override
    public void attach(FractalObserver observer) {
        observers.add(observer);

    }

    /**
     * @param observer
     */
    @Override
    public void detach(FractalObserver observer) {

    }

    /**
     *
     */
    @Override
    public void notifyObservers() {
        for (FractalObserver observer : observers) {
            observer.update();
        }

    }

    /**
     * @return
     */
    @Override
    public ArrayList<FractalElement> getData() {
        return null;
    }

    /**
     *
     */
    @Override
    public void setOptions() {
        notifyObservers();

    }
}
