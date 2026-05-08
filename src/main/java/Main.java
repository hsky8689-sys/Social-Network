import domain.*;
import service.*;
import repository.*;
import ui.*;
import java.sql.SQLException;
public class Main {
    public static void main(String[] args) throws SQLException {
        DBRepoPersoane rp = new DBRepoPersoane("jdbc:postgresql://localhost:5432/socialnetwork","postgres","Locdedatcucapu2005");
        DBServicePersoane adminPersoane = new DBServicePersoane(rp);
        rp.utilizatori().forEach(System.out::println);
        DBRepoRate rr = new DBRepoRate("jdbc:postgresql://localhost:5432/socialnetwork","postgres","Locdedatcucapu2005");
        DBServiceRate adminRate = new DBServiceRate(rr);
        rr.utilizatori().forEach(System.out::println);
        DBRepoCarduri rc = new DBRepoCarduri("jdbc:postgresql://localhost:5432/socialnetwork","postgres","Locdedatcucapu2005");
        DBServiceCarduri carduri = new DBServiceCarduri(rc);
        DBRepoArray<Message,User> rm = new DBRepoMesaje("jdbc:postgresql://localhost:5432/socialnetwork","postgres","Locdedatcucapu2005");
        DBServiceMesaje mesaje = new DBServiceMesaje(rm);
        DBRepoPrietenii rpi = new DBRepoPrietenii("jdbc:postgresql://localhost:5432/socialnetwork","postgres","Locdedatcucapu2005");
        DBServicePrietenii prietenii = new DBServicePrietenii(rpi);
        DBRepoEvents re = new DBRepoEvents("jdbc:postgresql://localhost:5432/socialnetwork", "postgres", "Locdedatcucapu2005");
        DBServiceEvents events = new DBServiceEvents(re);
        DBRepoRaces rev = new DBRepoRaces("jdbc:postgresql://localhost:5432/socialnetwork", "postgres", "Locdedatcucapu2005");
        DBServiceRaces raceEvents = new DBServiceRaces(rev);
        Network instagram = new Network(adminPersoane,adminRate,prietenii,mesaje,carduri,events,raceEvents);
        Console cns = new Console(instagram);
        cns.Run();
    }
}