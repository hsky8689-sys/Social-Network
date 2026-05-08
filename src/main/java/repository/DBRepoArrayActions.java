package repository;
import domain.Card;
import domain.Event;
import service.Network;

import java.util.List;

public interface DBRepoArrayActions<T,E>{
    boolean adaugaMembru(T o,E p);
    boolean stergeMembru(T o,E p);
    List<T> gasesteDupaMembru(E p);
    boolean stergeMembruDinToate(E p);
    List<E> gasesteMembrii(T o, Network network);
}
