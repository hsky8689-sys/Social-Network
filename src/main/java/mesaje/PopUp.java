package mesaje;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class PopUp {
    public static void popMessage(Stage owner, Alert.AlertType type,String header,String text){
        Alert message = new Alert(type);
        message.setHeaderText(header);
        message.setContentText(text);
        message.initOwner(owner);
        message.showAndWait();
    }
    public static void popError(Stage owner,String text){
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.initOwner(owner);
        message.setTitle("EROARE");
        message.setContentText(text);
        message.showAndWait();
    }
}
