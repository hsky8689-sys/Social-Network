package service;
import repository.DBRepoArray;
import validatori.ValidatorCard;
import validatori.ValidatorEvent;

import java.sql.SQLException;
import java.util.List;

public class DBArrayService<T,E> implements ServiceActions<T>,ArrayServiceActions<T,E>{
    protected DBRepoArray<T,E> repo;
    protected boolean aboneaza;
    public DBArrayService(DBRepoArray<T,E> repo) {
        this.repo=repo;
        aboneaza=false;
    }
    public String valideaza(String[] componente){
        switch (componente.length){
            case 3->{
                ValidatorCard validatorCard = new ValidatorCard(componente);
                return validatorCard.valideaza();
            }
            case 2->{
                ValidatorEvent validatorEvent = new ValidatorEvent(componente);
                return validatorEvent.valideaza();
            }
        }
        return "";
    }
    @Override
    public boolean adaugaInComunitate(T o, E p) {return repo.adaugaMembru(o,p);}
    @Override
    public boolean stergeDinComunitate(T o, E p) {return repo.stergeMembru(o,p);}
    public boolean adauga(T o) {return repo.adauga(o);}
    public List<T> gasesteDupaMembru(E p) {
        return repo.gasesteDupaMembru(p);
    }
    public boolean contine(T o) {
        return repo.gaseste(o);
    }
    public boolean sterge(T o) {
        return repo.gaseste(o);
    }
    public List<T> utilizatori() throws SQLException {return repo.utilizatori();}
}
