package repository;

import java.util.List;

public interface RepoActions<T> {
    boolean adauga(T o);
    boolean gaseste(T o);
    boolean sterge(T o);
    List<T> utilizatori();
}
