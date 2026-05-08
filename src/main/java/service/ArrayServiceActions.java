package service;

import validatori.ValidatorCard;

import java.util.List;

public interface ArrayServiceActions<T,E> {
    default public String valideaza(String[] componente){
        switch (componente.length){
                case 3->{
                    ValidatorCard validatorCard = new ValidatorCard(componente);
                    return validatorCard.valideaza();
                }

        }
        return "";
    }
    public boolean adaugaInComunitate(T o,E p);
    public boolean stergeDinComunitate(T o,E p);
    public List<T> gasesteDupaMembru(E p);
}
