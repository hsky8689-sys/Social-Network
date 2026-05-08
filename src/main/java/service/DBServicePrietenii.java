package service;

import domain.Friendship;
import domain.User;
import observer.CRUDActions;
import observer.Observable;
import observer.ObservableSupport;
import observer.ObserverGUI;
import repository.DBRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DBServicePrietenii extends DBService<Friendship>
        implements Observable<ObserverGUI, CRUDActions> {

    private static int maxComunitati = 10000;
    private List<List<User>> comunitati;
    private final ObservableSupport obs = new ObservableSupport();

    public DBServicePrietenii(DBRepository<Friendship> repo) {
        super(repo);
    }

    @Override
    public void addObserver(ObserverGUI o)                            { obs.addObserver(o); }
    @Override
    public void removeObserver(ObserverGUI o)                         { obs.removeObserver(o); }
    @Override
    public void notifyObservers(CRUDActions action, Object payload)   { obs.notifyObservers(action, payload); }

    @Override
    public boolean adauga(Friendship o) {
        boolean ok = repo.adauga(o);
        // payload = Friendship adaugata — UI-ul poate sti cine a trimis cererea
        if (ok) notifyObservers(CRUDActions.FRIEND_ADDED, o);
        return ok;
    }

    @Override
    public boolean contine(Friendship o) { return repo.gaseste(o); }

    @Override
    public boolean sterge(Friendship o) {
        boolean ok = repo.sterge(o);
        if (ok) notifyObservers(CRUDActions.FRIEND_REMOVED, o);
        return ok;
    }

    @Override
    public List<Friendship> utilizatori() throws SQLException { return repo.utilizatori(); }

    // ---- DFS pentru comunitati (neatins) ----
    private int dfs(User u, List<User> visited, int cc, int drum, Network where) throws SQLException {
        visited.add(u);
        if (!comunitati.get(cc).contains(u)) comunitati.get(cc).add(u);
        List<User> prieteni = utilizatori().stream()
                .filter(p -> p.getFirst().equals(u.getId()) || p.getSecond().equals(u.getId()))
                .map(p -> {
                    if (p.getFirst().equals(u.getId())) {
                        User cine = where.getAdminPersoane().getById(p.getSecond());
                        if (cine == null) cine = where.getAdminRate().getById(p.getSecond());
                        return cine;
                    } else {
                        User cine = where.getAdminPersoane().getById(p.getFirst());
                        if (cine == null) cine = where.getAdminRate().getById(p.getFirst());
                        return cine;
                    }
                }).toList();
        int maxDepth = drum;
        for (User p : prieteni) {
            if (!visited.contains(p)) {
                int newDepth = dfs(p, visited, cc, drum + 1, where);
                if (newDepth > maxDepth) maxDepth = newDepth;
            }
        }
        return maxDepth;
    }

    private List<User> dfsSetup(Network where) throws SQLException {
        List<User> useri = new ArrayList<>();
        where.getAdminPersoane().utilizatori().forEach(useri::add);
        where.getAdminRate().utilizatori().forEach(useri::add);
        return useri;
    }

    public int dfsMain(Network where, boolean vreiComunitati) throws SQLException {
        comunitati = new ArrayList<>();
        for (int i = 0; i < maxComunitati; i++) comunitati.add(new ArrayList<>());
        List<User> useri = dfsSetup(where);
        List<User> visited = new ArrayList<>();
        AtomicInteger result = new AtomicInteger(0);
        AtomicInteger cc = new AtomicInteger(-1);
        AtomicInteger drumCrt = new AtomicInteger(0);
        useri.forEach(u -> {
            if (!visited.contains(u)) {
                cc.getAndAdd(1);
                drumCrt.set(0);
                try { dfs(u, visited, cc.get(), drumCrt.get(), where); }
                catch (SQLException e) { e.printStackTrace(); }
                result.getAndAdd(1);
            }
        });
        if (vreiComunitati) return result.get();
        else return IndexDiametruMax(where);
    }

    private int IndexDiametruMax(Network where) throws SQLException {
        List<User> useri = dfsSetup(where);
        AtomicInteger max = new AtomicInteger(-1);
        AtomicInteger index = new AtomicInteger(-1);
        AtomicInteger cc = new AtomicInteger(-1);
        AtomicInteger drum = new AtomicInteger(0);
        comunitati.stream().forEach(c -> {
            if (!c.isEmpty()) {
                List<User> visited = new ArrayList<>();
                c.forEach(u -> {
                    if (!visited.contains(u)) {
                        cc.getAndAdd(1);
                        drum.set(0);
                        try { drum.set(dfs(u, visited, cc.get(), drum.get(), where)); }
                        catch (SQLException e) { e.printStackTrace(); }
                        if (drum.get() > max.get()) { max.set(drum.get()); index.set(cc.get()); }
                    }
                });
            }
        });
        return index.get();
    }

    public List<List<User>> comunitati() { return comunitati; }
}
