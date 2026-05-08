package service;

import domain.Event;
import domain.User;
import observer.CRUDActions;
import observer.Observable;
import observer.ObservableSupport;
import observer.ObserverGUI;
import repository.DBRepoEvents;
import repository.EventRepoActions;
import validatori.ValidatorEvent;

import java.util.List;
import java.util.NoSuchElementException;

public class DBServiceEvents implements EventRepoActions<Event, User>,
        Observable<ObserverGUI, CRUDActions> {

    protected DBRepoEvents repo;
    private final ObservableSupport obs = new ObservableSupport();

    public DBServiceEvents(DBRepoEvents repo) {
        this.repo = repo;
    }

    @Override
    public void addObserver(ObserverGUI o)                            { obs.addObserver(o); }
    @Override
    public void removeObserver(ObserverGUI o)                         { obs.removeObserver(o); }
    @Override
    public void notifyObservers(CRUDActions action, Object payload)   { obs.notifyObservers(action, payload); }

    public String valideaza(String[] components) {
        return new ValidatorEvent(components).valideaza();
    }

    public Event getById(Long id) {
        try {
            return utilizatori().stream().filter(p -> p.getId().equals(id)).findFirst().get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public List<User> abonati(Event o) { return this.repo.abonati(o); }

    @Override
    public boolean gaseste(Event o) { return repo.gaseste(o); }

    @Override
    public boolean adauga(Event o) {
        boolean ok = repo.adauga(o);
        // payload = evenimentul adaugat
        if (ok) notifyObservers(CRUDActions.EVENT_ADDED, o);
        return ok;
    }

    @Override
    public List<Event> utilizatori() { return repo.utilizatori(); }

    @Override
    public boolean inscrieParticipant(Event o, User p) {
        boolean ok = repo.inscrieParticipant(o, p);
        if (ok) notifyObservers(CRUDActions.EVENT_ADDED, o);
        return ok;
    }

    @Override
    public boolean retrageParticipant(Event o, User p) {
        boolean ok = repo.retrageParticipant(o, p);
        if (ok) notifyObservers(CRUDActions.EVENT_REMOVED, o);
        return ok;
    }

    @Override
    public boolean aboneaza(Event o, User p) {
        boolean ok = repo.aboneaza(o, p);
        if (ok) notifyObservers(CRUDActions.EVENT_ADDED, o);
        return ok;
    }

    @Override
    public boolean dezaboneaza(Event o, User p) {
        boolean ok = repo.dezaboneaza(o, p);
        if (ok) notifyObservers(CRUDActions.EVENT_REMOVED, o);
        return ok;
    }

    @Override
    public List<User> gasesteMembrii(Event o, Network network) {
        return repo.gasesteMembrii(o, network);
    }
}
