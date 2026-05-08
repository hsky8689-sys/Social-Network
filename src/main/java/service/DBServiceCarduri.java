package service;
import domain.Card;
import domain.Rata;
import repository.DBRepoArray;
import repository.DBRepoCarduri;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

public class DBServiceCarduri extends DBArrayService<Card, Rata> implements Medie<Card>{
    public DBServiceCarduri(DBRepoCarduri repo) {
        super(repo);
    }
    public double performantaMedie(Card o,Network network){
        if(!contine(o))return -1;
        List<Rata> rate = new ArrayList<Rata>();
        rate=repo.gasesteMembrii(o,network);
        if(rate.isEmpty())return -1;
        AtomicReference<Double> rezultat= new AtomicReference<>((double) 0);
        rate.stream().forEach(r->rezultat.set(rezultat.get()+r.getViteza()+r.getRezistenta()));
        return rezultat.get()/ rate.size();
    }
    @Override
    public boolean adaugaInComunitate(Card o, Rata p){
        try {
            if (!contine(o)) return false;
            return repo.adaugaMembru(o, p);
        }catch (RuntimeException e){
            System.err.println(e.getMessage());
            return false;
        }
    }
    @Override
    public boolean stergeDinComunitate(Card o, Rata p) {
        try {
            if (!contine(o)) return false;
            return repo.stergeMembru(o, p);
        }catch (RuntimeException e){
            return false;
        }
    }
    @Override
    public List<Card> gasesteDupaMembru(Rata p) {
        return super.gasesteDupaMembru(p);
    }
    @Override
    public List<Card> utilizatori() throws SQLException {
        return super.utilizatori();
    }
    @Override
    public Card getById(Long id) {
        try {
            return utilizatori().stream()
                    .filter(p -> p.getId() == id)
                    .findFirst().get();
        }catch (SQLException | NoSuchElementException e){
            return null;
        }
    }
}
