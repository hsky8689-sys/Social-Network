package service;
import domain.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class Network {
    private DBServicePersoane adminPersoane;
    private DBServiceRate adminRate;
    private DBServicePrietenii adminPrietenii;
    private DBServiceMesaje adminMesaje;
    private DBServiceCarduri adminCarduri;
    private DBServiceEvents adminEvents;
    private DBServiceRaces adminRaces;
    public Network(DBServicePersoane adminPersoane, DBServiceRate adminRate, DBServicePrietenii adminPrietenii, DBServiceMesaje adminMesaje,DBServiceCarduri adminCarduri, DBServiceEvents adminEvents,DBServiceRaces adminRaces) {
        this.adminPersoane = adminPersoane;
        this.adminRate = adminRate;
        this.adminPrietenii = adminPrietenii;
        this.adminMesaje = adminMesaje;
        this.adminCarduri = adminCarduri;
        this.adminEvents = adminEvents;
        this.adminRaces = adminRaces;
    }
    public User<Long> tryLogin(String user, String pass) throws SQLException {
       try {
           java.util.Optional<Persoana> persoana = getAdminPersoane().utilizatori().stream()
                   .filter(p -> p.getPassword().equals(pass) && p.getUsername().equals(user))
                   .findFirst();
           if (persoana.isPresent()) logged_in = persoana.get();
           else {
               Optional<Rata> rata = getAdminRate().utilizatori().stream()
                       .filter(r -> r.getPassword().equals(pass) && r.getUsername().equals(user))
                       .findFirst();
               if (rata.isPresent()) logged_in = rata.get();
           }
       } catch (RuntimeException e) {
           e.printStackTrace();
           System.err.println(e.getMessage());
       }
        return logged_in;
    }
    public DBServicePersoane getAdminPersoane() {
        return adminPersoane;
    }
    public DBServiceRate getAdminRate() {
        return adminRate;
    }
    public DBServicePrietenii getAdminPrietenii() {
        return adminPrietenii;
    }
    public DBServiceMesaje getAdminMesaje() {
        return adminMesaje;
    }
    public DBServiceCarduri getAdminCarduri() {
        return adminCarduri;
    }
    public DBServiceEvents getAdminEvents() {
        return adminEvents;
    }
    public DBServiceRaces getAdminRaces() {
        return adminRaces;
    }
}
