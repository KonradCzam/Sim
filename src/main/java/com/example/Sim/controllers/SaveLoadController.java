package com.example.Sim.controllers;

import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.FXML.DialogController;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Model.Npc;
import com.example.Sim.Model.SaveSlot;
import com.example.Sim.Services.NpcService;
import com.example.Sim.Utilities.ImageHandler;
import com.example.Sim.Utilities.SaveAndLoadUtility;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.WindowEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URL;
import java.util.ResourceBundle;

@Service
public class SaveLoadController implements Initializable, DialogController {
    @FXML
    public TableView loadGameTable;
    @FXML
    private TableColumn<SaveSlot, String> saveNameColumn;
    @FXML
    private TableColumn<SaveSlot, String> saveTurnColumn;
    @FXML
    private TableColumn<SaveSlot, String> saveDateColumn;




    @Resource
    SaveAndLoadUtility saveAndLoadUtility;

    private ScreensConfiguration screens;
    private FXMLDialog dialog;


    public SaveLoadController(ScreensConfiguration screens) {
        this.screens = screens;
    }
    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeSaveLoadTable();
        dialog.setOnShown(onShownEventHandler);

    }
    EventHandler<WindowEvent> onShownEventHandler =
            new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    refreshTable();
                    checkLastElement();
                }
            };
    private void initializeSaveLoadTable() {
        saveNameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getSaveData().getName()));

        saveDateColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getSaveData().getDate()));

        saveTurnColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getSaveData().getTurn().toString()));
        TableColumn tableColumn = (TableColumn) loadGameTable.getColumns().get(3);
        tableColumn.setCellValueFactory(new PropertyValueFactory<SaveSlot,String>("saveButton"));
        tableColumn = (TableColumn) loadGameTable.getColumns().get(4);
        tableColumn.setCellValueFactory(new PropertyValueFactory<SaveSlot,String>("loadButton"));
    }
    private void checkLastElement() {
        if(loadGameTable.getItems().isEmpty()){
            createLastElement();
            return;
        }
        SaveSlot saveSlot = (SaveSlot) loadGameTable.getItems().get(loadGameTable.getItems().size()-1);
        if("Override".equals(saveSlot.getSaveButton().getText())){
            createLastElement();
        }
    }
    private void createLastElement() {
        loadGameTable.getItems().add(saveAndLoadUtility.createEmptySlot());
    }
    public void refreshTable() {

        ObservableList data = FXCollections.observableArrayList(saveAndLoadUtility.getSavedGames());
        loadGameTable.getItems().remove(0,loadGameTable.getItems().size());
        loadGameTable.setItems(data);
    }


    public void goBack() {
        dialog.close();
        screens.hubDialog().show();
    }

    public void goToMain() {
        dialog.close();
        screens.startDialog().show();
    }

}
