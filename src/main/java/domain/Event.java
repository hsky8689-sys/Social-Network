package domain;
import service.Network;

import java.time.LocalDate;
import java.util.*;
public class Event implements Observer{
    protected Long id,idHost;
    protected String nume;
    public Event(Long id, String nume,Long idHost) {
        this.id=id;
        this.nume=nume;
        this.idHost=idHost;
    }
    public Long getIdHost(){return this.idHost;}
    public String getNume() {
        return nume;
    }
    @Override
    public boolean subscribe(User user) {
/*
!!!!
* */
        return true;
    }
    @Override
    public boolean unsubscribe(User user){
        return true;
    }
    @Override
    public boolean notifyAllSubscribers(Network network) {
        List<User> subscribers = network.getAdminEvents().abonati(new Event(id,nume,idHost));
        String mesaj = "S-a petrecut evenimentul "+getNume();
        User host = network.getAdminPersoane().getById(getIdHost());
        if(host==null) host = network.getAdminRate().getById(getIdHost());
        if(host==null) return false;
        for(User u:subscribers){
            network.getAdminMesaje().adauga(new Message(0L,getIdHost(),(Long)u.getId(),mesaj,new Date()));
        }
        return true;
    }
    @Override
    public void runEvent(Network network) {
        notifyAllSubscribers(network);
    }
    @Override
    public String toString() {
        String rez = id.toString();
        rez += "|"+nume+"|";
        return rez;
    }
    public Long getId() {
        return this.id;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, nume);
    }
}