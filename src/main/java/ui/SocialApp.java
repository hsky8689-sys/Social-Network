package ui;

import controller.LoginController;
import domain.Message;
import domain.User;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.*;
import service.*;

public class SocialApp extends Application {
    private static final String URL = "jdbc:postgresql://localhost:5432/socialnetwork";
    private static final String DB_USER = "postgres";
    private static final String DB_PASS = "Locdedatcucapu2005";

    public static Network buildNetwork() throws Exception {
        DBRepoPersoane rp = new DBRepoPersoane(URL, DB_USER, DB_PASS);
        DBServicePersoane adminPersoane = new DBServicePersoane(rp);

        DBRepoRate rr = new DBRepoRate(URL, DB_USER, DB_PASS);
        DBServiceRate adminRate = new DBServiceRate(rr);

        DBRepoCarduri rc = new DBRepoCarduri(URL, DB_USER, DB_PASS);
        DBServiceCarduri carduri = new DBServiceCarduri(rc);

        DBRepoArray<Message, User> rm = new DBRepoMesaje(URL, DB_USER, DB_PASS);
        DBServiceMesaje mesaje = new DBServiceMesaje(rm);

        DBRepoPrietenii rpi = new DBRepoPrietenii(URL, DB_USER, DB_PASS);
        DBServicePrietenii prietenii = new DBServicePrietenii(rpi);

        DBRepoEvents re = new DBRepoEvents(URL, DB_USER, DB_PASS);
        DBServiceEvents events = new DBServiceEvents(re);

        DBRepoRaces rev = new DBRepoRaces(URL, DB_USER, DB_PASS);
        DBServiceRaces raceEvents = new DBServiceRaces(rev);

        return new Network(adminPersoane, adminRate, prietenii, mesaje, carduri, events, raceEvents);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        LoginController loginCtrl = new LoginController(primaryStage);
        loginCtrl.setNetwork(SocialApp.buildNetwork());
        Scene scene = new Scene(loginCtrl.getView(), 400, 320);
        primaryStage.setTitle("Social Network - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
