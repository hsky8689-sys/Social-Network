package domain;
import java.util.List;
import java.util.Objects;

public class Card {
    private Long id;
    private String numeCard;
    private List<Rata> membri;
    private String tipCard;
    public Card(Long id,String numeCard,List<Rata> rate,String tipCard) {
        this.id=id;
        this.numeCard=numeCard;
        this.membri = rate;
        this.tipCard=tipCard;
    }
    public String getTipCard(){return this.tipCard;}
    public double getPerformantaMedie() {
        if(this.membri.isEmpty())return -1;
        double rez=0;
        for(Rata r:this.membri)
            rez+=r.getViteza()+r.getRezistenta();
        return (double)rez/this.membri.size();
    }
    @Override
    public String toString() {
        String rez = id+"|"+numeCard+"|";
        for(Rata r:membri)
            rez+=r.toString()+"|";
        return rez;
    }
    public Long getId() {
        return id;
    }
    public String getNumeCard() {
        return numeCard;
    }
    public List<Rata> getMembri() {
        return membri;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(id, card.id);
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
