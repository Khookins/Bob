package bobpack.Patterns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Observable {
    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer)
    {
        observers.add(observer);
    }

    public void removeObserver(Observer observer)
    {
        observers.remove(observer);
    }

    public void notifyObservers() throws IOException {
        for(Observer observer : observers)
        {
            observer.update(this);
        }
    }
}
