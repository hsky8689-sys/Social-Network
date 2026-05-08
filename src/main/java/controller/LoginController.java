package controller;

import domain.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import service.Network;
import ui.SocialApp;

import javax.net.ssl.SSLException;
import java.sql.SQLException;

public class LoginController {
    private final Stage loginStage;
    private Network network;
    private VBox view;
    TextField userField;
    PasswordField passField;
    Label errorLabel;

    public LoginController(Stage loginStage) {
        this.loginStage = loginStage;
        buildView();
    }

    private void buildView() {
        Label title = new Label("Social Network");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label userLabel = new Label("Username:");
        userField = new TextField();
        userField.setPromptText("username");
        userField.setMaxWidth(250);

        Label passLabel = new Label("Parola:");
        passField = new PasswordField();
        passField.setPromptText("parola");
        passField.setMaxWidth(250);

        errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: red;");

        Button loginBtn = new Button("Login");
        loginBtn.setDefaultButton(true);
        loginBtn.setMinWidth(100);

        loginBtn.setOnAction(e -> {
            String user = userField.getText().trim();
            String pass = passField.getText().trim();
            if (user.isEmpty() || pass.isEmpty()) {
                errorLabel.setText("Completati username si parola.");
                return;
            }
            try {
                Network network = SocialApp.buildNetwork();
                User<Long> logged = network.tryLogin(user, pass);
                if (logged!=null) {
                    openMainWindow();
                    userField.clear();
                    passField.clear();
                    errorLabel.setText("");
                } else {
                    errorLabel.setText("Username sau parola incorecte.");
                }
            } catch (Exception ex) {
                errorLabel.setText("Eroare: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        view = new VBox(12, title, userLabel, userField, passLabel, passField, loginBtn, errorLabel);
        view.setAlignment(Pos.CENTER);
        view.setPadding(new Insets(30));
    }

    private void openMainWindow() {
        Stage mainStage = new Stage();
        User<Long> logged = null;
        try {
            logged = network.tryLogin(userField.getText(), passField.getText());
            if(logged == null)throw new SQLException();
            mainStage.setTitle("Social Network - " + logged.getUsername());
        } catch (SQLException e) {
            errorLabel.setText("Username sau parola incorecte.");
        }

        MainWindowController mainCtrl = new MainWindowController(network,mainStage,logged);

        Scene scene = new Scene(mainCtrl.getView(), 900, 600);
        mainStage.setScene(scene);
        mainStage.show();
    }

    public Parent getView() {
        return view;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }
}
