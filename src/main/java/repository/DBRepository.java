package repository;
import domain.Persoana;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
public abstract class DBRepository<T> implements DBRepoActions<T>{
    protected String url,username,password;
    public DBRepository(String url,String username,String password){
        this.url=url;
        this.username=username;
        this.password=password;
    }
    public boolean gaseste(T o) {
        return false;
    }
}
