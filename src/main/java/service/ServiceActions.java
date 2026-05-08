package service;

import validatori.ValidatorCard;
import validatori.ValidatorPersoana;
import validatori.ValidatorRata;

import java.sql.SQLException;
import java.util.List;

public interface ServiceActions<T> {
    default String valideaza(String[] components) {
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
    boolean adauga(T o);
    boolean contine(T o);
    boolean sterge(T o);
    default T getById(Long id){return null;}
    List<T> utilizatori() throws SQLException;
}
