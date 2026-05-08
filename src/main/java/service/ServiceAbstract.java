package service;

import repository.Repository;
import domain.User;

public abstract class ServiceAbstract<T> implements ServiceActions<T>{
    protected User loggedUserId;
    protected Repository repo;
    public ServiceAbstract(Repository<T> repo) {
        this.repo=repo;
        this.loggedUserId=null;
    }
}
