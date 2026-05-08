package ui;
import controller.ControllerRate;
import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import repository.DBRepoRate;
import service.DBServiceRate;

import java.net.URL;

public class RateUI extends Application{
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Interfata rate");
        URL pathname = getClass().getResource("/rate-view.xml");
        if (pathname == null) {
            System.err.println("Eroare critica: Resursa FXML /rate-view.xml nu a fost gasita.");
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(pathname);
        Parent root = fxmlLoader.load();
        ControllerRate controllerRate = fxmlLoader.getController();
        DBRepoRate repoRate = new DBRepoRate("jdbc:postgresql://localhost:5432/socialnetwork","postgres","Locdedatcucapu2005");
        DBServiceRate serviceRate = new DBServiceRate(repoRate);
        if (controllerRate != null) {
            // setService va apela initModel() in interior
            controllerRate.setService(serviceRate);
        } else {
            System.err.println("Eroare: Controller-ul nu a putut fi extras dupa load.");
            return;
        }
        serviceRate.addSubscriber(controllerRate);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args){
        launch();
    }
}
