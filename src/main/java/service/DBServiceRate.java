package service;

import controller.ControllerRate;
import domain.Rata;
import domain.tipRata;
import observer.CRUDActions;
import observer.Observable;
import observer.ObservableSupport;
import observer.ObserverGUI;
import repository.DBRepository;

import java.sql.SQLException;
import java.util.*;

public class DBServiceRate extends DBService<Rata>
        implements Observable<ObserverGUI, CRUDActions> {

    // Pastram si observerul vechi pentru ControllerRate (FXML al ratelor)
    private final HashSet<ControllerRate> legacyObservers = new HashSet<>();
    private final ObservableSupport obs = new ObservableSupport();

    public DBServiceRate(DBRepository<Rata> repo) {
        super(repo);
    }

    // --- Observable<ObserverGUI> ---
    @Override
    public void addObserver(ObserverGUI o)                            { obs.addObserver(o); }
    @Override
    public void removeObserver(ObserverGUI o)                         { obs.removeObserver(o); }
    @Override
    public void notifyObservers(CRUDActions action, Object payload)   { obs.notifyObservers(action, payload); }

    // --- Compatibilitate cu ControllerRate (vechiul observer) ---
    public void addSubscriber(ControllerRate c)  { legacyObservers.add(c); }
    public void notifySubscribers(CRUDActions a) { legacyObservers.forEach(c -> c.update(a,null)); }

    @Override
    public boolean adauga(Rata o) {
        boolean ok = repo.adauga(o);
        if (ok) { notifySubscribers(CRUDActions.add); notifyObservers(CRUDActions.add, o); }
        return ok;
    }

    @Override
    public boolean contine(Rata o) { return repo.gaseste(o); }

    @Override
    public boolean sterge(Rata o) {
        boolean ok = repo.sterge(o);
        if (ok) { notifySubscribers(CRUDActions.delete); notifyObservers(CRUDActions.delete, o); }
        return ok;
    }

    @Override
    public Rata getById(Long id) {
        try {
            return utilizatori().stream().filter(p -> p.getId().equals(id)).toList().getFirst();
        } catch (Exception e) { return null; }
    }

    public Rata getByUsername(String username) {
        try {
            return utilizatori().stream().filter(p -> p.getUsername().equals(username)).findFirst().get();
        } catch (SQLException | NoSuchElementException e) { return null; }
    }

    public List<Rata> filtreazaDupaTip(tipRata tip) throws SQLException {
        List<Rata> rate = repo.utilizatori();
        List<Rata> rezultat = new ArrayList<>();
        try { rate.stream().filter(r -> r.getTip().equals(tip)).forEach(rezultat::add); }
        catch (RuntimeException e) { return null; }
        return rezultat;
    }

    @Override
    public List<Rata> utilizatori() throws SQLException { return repo.utilizatori(); }
}
