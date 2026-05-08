module Seminar6.main {
    requires java.sql;
    requires javafx.fxml;
    requires javafx.controls;

    opens ui to javafx.fxml;
    exports ui;
}