package observer;

/**
 * Interfata generica pentru serviciile observabile.
 * T = tipul observerului (ObserverGUI)
 * A = tipul actiunii (CRUDActions)
 */
public interface Observable<T, A> {
    void addObserver(T observer);
    void removeObserver(T observer);
    void notifyObservers(A action, Object payload);
}