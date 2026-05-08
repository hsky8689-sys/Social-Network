package domain;

import java.util.Objects;

public class Lane{
    private Rata rata;
    private Long id,idRata;
    private int distance;
    public Lane(Long ID,int distance) {
        this.id=ID;
        this.distance=distance;
        this.idRata=-1L;
    }
    public Long getId(){return this.id;}
    public void setRata(Rata newDuck) {
        this.rata=newDuck;
        this.idRata= newDuck.getId();
    }
    public void freeLane() {
        this.rata=null;
        this.idRata=-1L;
    }
    public int getDistance() {return this.distance;}
    public Rata getRata() {
        return rata;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Lane lane = (Lane) o;
        return Objects.equals(id, lane.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(rata, id, distance);
    }
    @Override
    public String toString() {
        String rez="";
        rez+=id+",";
        rez+=distance+",";
        return rez;
    }
}
