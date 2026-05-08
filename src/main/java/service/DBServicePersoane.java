package service;

import domain.Persoana;
import observer.CRUDActions;
import observer.Observable;
import observer.ObservableSupport;
import observer.ObserverGUI;
import repository.DBRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

public class DBServicePersoane extends DBService<Persoana> implements Observable<ObserverGUI, CRUDActions> {

    private final ObservableSupport obs = new ObservableSupport();

    public DBServicePersoane(DBRepository<Persoana> repo) {
        super(repo);
    }

    @Override
    public void addObserver(ObserverGUI o) { obs.addObserver(o); }
    @Override
    public void removeObserver(ObserverGUI o) { obs.removeObserver(o); }
    @Override
    public void notifyObservers(CRUDActions action, Object payload) { obs.notifyObservers(action, payload); }

    @Override
    public boolean adauga(Persoana o) {
        boolean ok = repo.adauga(o);
        if (ok) notifyObservers(CRUDActions.add, o);
        return ok;
    }

    @Override
    public boolean contine(Persoana o) { return repo.gaseste(o); }

    @Override
    public boolean sterge(Persoana o) {
        boolean ok = repo.sterge(o);
        if (ok) notifyObservers(CRUDActions.delete, o);
        return ok;
    }

    @Override
    public Persoana getById(Long id) {
        try {
            return utilizatori().stream().filter(p -> p.getId().equals(id)).findFirst().get();
        } catch (SQLException | NoSuchElementException e) {
            return null;
        }
    }

    public Persoana getByUsername(String username) {
        try {
            return utilizatori().stream().filter(p -> p.getUsername().equals(username)).findFirst().get();
        } catch (SQLException | NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public List<Persoana> utilizatori() throws SQLException { return repo.utilizatori(); }
}
