package controller;

import domain.Message;
import domain.Observer;
import domain.Rata;
import domain.tipRata;
import exceptii.NullEntityException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mesaje.PopUp;
import observer.CRUDActions;
import observer.ObserverGUI;
import service.DBServiceRate;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ControllerRate implements ObserverGUI {
    private DBServiceRate serviceRate;
    private ObservableList<Rata> rate = FXCollections.observableArrayList();
    @FXML
    private TableView<Rata> tableView;
    @FXML
    private TableColumn<Rata,Long> tableColumnId;
    @FXML
    private TableColumn<Rata,String> tableColumnUsername;
    @FXML
    private TableColumn<Rata,String> tableColumnTip;
    @FXML
    private TableColumn<Rata,Long> tableColumnViteza;
    @FXML
    private TableColumn<Rata,Long> tableColumnRezistenta;
    @FXML
    private Button deleteButton;
    @FXML
    private ComboBox<tipRata> comboBox;
    public void setService(DBServiceRate service){
        this.serviceRate=service;
        try{
            initModel();
        }catch (SQLException e){
            PopUp.popError(null,"Eroare la incarcarea datelor:"+e.getMessage());
        }
    }
    @FXML
    public void initialize() throws SQLException {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        tableColumnViteza.setCellValueFactory(new PropertyValueFactory<>("viteza"));
        tableColumnTip.setCellValueFactory(new PropertyValueFactory<>("tip"));
        tableColumnRezistenta.setCellValueFactory(new PropertyValueFactory<>("rezistenta"));
        comboBox.getItems().clear();
        comboBox.getItems().addAll(Arrays.stream(tipRata.values()).toList());
        tableView.setItems(rate);
    }
    public void initModel() throws SQLException {
        rate.clear();
        List<Rata> listaRate = serviceRate.utilizatori();
        rate.setAll(listaRate);
        tableView.setItems(rate);
    }
    public void onFilter(){
        try {
            tipRata tipCerut = comboBox.getValue();
            rate.clear();
            List<Rata> listaRate = serviceRate.filtreazaDupaTip(tipCerut);
            rate.setAll(listaRate);
            tableView.setItems(rate);
        } catch (RuntimeException|SQLException e) {
            System.err.println(e.getMessage());
            return;
        }
    }
    public void onDelete(ActionEvent actionEvent){
        Rata rata = tableView.getSelectionModel().getSelectedItem();
        try {
            if(rata==null)
                throw new NullEntityException("");
            if (serviceRate.sterge(rata)) {
                PopUp.popMessage(null,Alert.AlertType.CONFIRMATION,"Stergere","Rata a fost stearsa");
            }
            else throw new NullEntityException("");
        } catch (NullEntityException e) {
            PopUp.popError(null,e.getMessage());
        }
    }
    @Override
    public void update(CRUDActions action,Object payload) {
        try {
            switch (action) {
                case add, delete -> {
                    initModel();
                    break;
                }
                case filter -> {
                    onFilter();
                    break;
                }
                case null, default -> {
                    PopUp.popError(null, "S-a cerut o actiune necunoscuta");
                    break;
                }
            }
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }
}
