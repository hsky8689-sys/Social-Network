package repository;

import domain.Event;

import java.util.HashSet;
import java.util.List;

public abstract class DBRepoArray<T,E> implements DBRepoActions<T>,DBRepoArrayActions<T,E>{
    protected String url,username,password;
    public DBRepoArray(String url,String username,String password){
        this.url=url;
        this.username=username;
        this.password=password;
    }
}
