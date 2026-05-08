package service;

import domain.Persoana;
import repository.DBRepository;
import validatori.ValidatorPersoana;
import validatori.ValidatorRata;

public abstract class DBService<T> implements ServiceActions<T> {

    protected DBRepository<T> repo;
    public DBService(DBRepository<T> repo){
        this.repo=repo;
    }
    @Override
    public String valideaza(String[] components) {
        switch (components.length){
            case 8->{
                ValidatorRata validatorRata = new ValidatorRata(components);
                return validatorRata.valideaza();
            }
            case 7->{
                ValidatorPersoana validatorPersoana = new ValidatorPersoana(components);
                return validatorPersoana.valideaza();
            }
        }
        return "";
    }
    @Override
    public boolean adauga(T o) {
        return repo.adauga(o);
    }
    @Override
    public boolean contine(T o) {
        return repo.gaseste(o);
    }
    @Override
    public boolean sterge(T o) {
        return repo.sterge(o);
    }
}