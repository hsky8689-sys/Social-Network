package domain;
import service.Network;

import java.util.*;

public class RaceEvent extends Event implements Observer{
   private int nr_culoare;
    private double timpMinim;
    public RaceEvent(Long id, String nume,Long idHost,int nr_culoare) {
        super(id, nume,idHost);
        this.nr_culoare=nr_culoare;
        //setUp();
    }
    public int getNrCuloare(){return this.nr_culoare;}
    @Override
    public boolean subscribe(User user) {
        if(user.getClass()!= RataInotatoare.class && user.getClass()!= RataFullStack.class)return false;
        //rate.add((Rata) user);
        return super.subscribe(user);
    }
    @Override
    public boolean unsubscribe(User user) {
        //rate.remove((Rata)user);
        return super.unsubscribe(user);
    }
    @Override
    public boolean notifyAllSubscribers(Network network) {
        //if (participanti.isEmpty()) return false;
        return true;
    }
    @Override
    public void runEvent(Network network) {
        //super.runEvent(network);
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RaceEvent raceEvent = (RaceEvent) o;
        return Objects.equals(getId(), raceEvent.getId());
    }
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }
    @Override
    public String toString() {
        String rez="";
        rez+=id+"|";
        rez+=nume+"|";
        return rez;
    }
}
