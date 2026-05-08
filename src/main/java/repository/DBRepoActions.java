package repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public interface DBRepoActions <T>{
    boolean gaseste(T o);
    boolean adauga(T o);
    boolean sterge(T o);
    List<T> utilizatori() throws SQLException;
}
