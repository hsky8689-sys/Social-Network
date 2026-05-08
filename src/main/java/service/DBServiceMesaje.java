package service;

import domain.Message;
import domain.User;
import observer.CRUDActions;
import observer.Observable;
import observer.ObservableSupport;
import observer.ObserverGUI;
import repository.DBRepoArray;

import java.sql.SQLException;
import java.util.List;

public class DBServiceMesaje extends DBArrayService<Message, User> implements Observable<ObserverGUI, CRUDActions> {

    private final ObservableSupport obs = new ObservableSupport();

    public DBServiceMesaje(DBRepoArray<Message, User> repo) {
        super(repo);
    }

    @Override
    public void addObserver(ObserverGUI o){ obs.addObserver(o); }
    @Override
    public void removeObserver(ObserverGUI o){ obs.removeObserver(o); }
    @Override
    public void notifyObservers(CRUDActions action, Object payload) { obs.notifyObservers(action, payload); }

    @Override
    public boolean adauga(Message m) {
        boolean ok = repo.adauga(m);
        if (ok) notifyObservers(CRUDActions.MESSAGE_RECEIVED, m.getSender());
        return ok;
    }

    @Override
    public List<Message> gasesteDupaMembru(User p) {
        return repo.gasesteDupaMembru(p);
    }

    @Override
    public boolean contine(Message o) { return repo.gaseste(o); }

    @Override
    public boolean sterge(Message o)  { return repo.sterge(o); }

    @Override
    public List utilizatori() throws SQLException { return repo.utilizatori(); }
}
