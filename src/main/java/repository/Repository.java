package repository;

import domain.Message;
import domain.Persoana;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Repository<T> implements RepoActions<T>{
    protected File file;
    protected List<T> membri;
    public Repository(String fileName) {
        this.file = new File(fileName);
    }

    public boolean updateFile() {
        return false;
    }
}
