package observer;

import java.util.HashSet;
import java.util.Set;

/**
 * Clasa helper — o includ ca field in servicii (composition) sau
 * extind din ea (daca ierarhia permite).
 * Tine lista de ObserverGUI si face notificarea thread-safe pe FX thread.
 */
public class ObservableSupport implements Observable<ObserverGUI, CRUDActions> {

    private final Set<ObserverGUI> observers = new HashSet<>();

    @Override
    public void addObserver(ObserverGUI observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ObserverGUI observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(CRUDActions action, Object payload) {
        // Notificam pe JavaFX Application Thread daca suntem deja pe el,
        // altfel postam pe el — important pentru operatii async viitoare
        if (javafx.application.Platform.isFxApplicationThread()) {
            observers.forEach(o -> o.update(action, payload));
        } else {
            javafx.application.Platform.runLater(
                () -> observers.forEach(o -> o.update(action, payload))
            );
        }
    }
}
