package com.example.Sim.controllers;

import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.FXML.DialogController;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Model.SaveData;
import com.example.Sim.Model.SaveSlot;
import com.example.Sim.Services.EndTurnService;
import com.example.Sim.Services.NpcService;
import com.example.Sim.Services.PlayerService;
import com.example.Sim.Utilities.SaveAndLoadUtility;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Service
public class SaveLoadController implements Initializable, DialogController {
    @FXML
    public TableView loadGameTable;
    @Resource
    NpcService npcService;
    @Resource
    PlayerService playerService;
    @Resource
    SaveAndLoadUtility saveAndLoadUtility;
    @Resource
    EndTurnService endTurnService;
    EventHandler<WindowEvent> onShownEventHandler =
            new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    refreshTable();

                }
            };
    @FXML
    private TableColumn<SaveSlot, String> saveNameColumn;
    @FXML
    private TableColumn<SaveSlot, String> saveTurnColumn;
    @FXML
    private TableColumn<SaveSlot, String> saveDateColumn;
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

    private void initializeSaveLoadTable() {
        saveNameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getSaveData().getName()));

        saveDateColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getSaveData().getDate()));

        saveTurnColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getSaveData().getTurn().toString()));
        TableColumn tableColumn = (TableColumn) loadGameTable.getColumns().get(3);
        tableColumn.setCellFactory(
                new Callback<TableColumn<SaveSlot, SaveSlot>, TableCell<SaveSlot, SaveSlot>>() {
                    @Override
                    public TableCell call(TableColumn<SaveSlot, SaveSlot> p) {
                        return new ButtonCell("Overwrite");
                    }
                }
        );
        tableColumn = (TableColumn) loadGameTable.getColumns().get(4);
        tableColumn.setCellFactory(
                new Callback<TableColumn<SaveSlot, SaveSlot>, TableCell<SaveSlot, SaveSlot>>() {
                    @Override
                    public TableCell call(TableColumn<SaveSlot, SaveSlot> p) {
                        return new ButtonCell("Load");
                    }
                }
        );
    }



    public void save() {
        TextInputDialog dialog = new TextInputDialog("save name");
        dialog.setTitle("Name your save");
        dialog.setHeaderText("Enter save name, or use default value.");

        Optional<String> name = dialog.showAndWait();
        save(name);
    }
    private void save(Optional<String> name) {
        SaveSlot saveSlot = saveAndLoadUtility.createSaveSlot(name);
        saveAndLoadUtility.saveGame(saveSlot);
        refreshTable();
    }

    public void refreshTable() {
        List<SaveSlot> saveSlots = saveAndLoadUtility.getSavedGames();
        ObservableList data = FXCollections.observableArrayList(saveSlots);
        loadGameTable.getItems().remove(0, loadGameTable.getItems().size());
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

    private class ButtonCell extends TableCell<SaveSlot, SaveSlot> {
        private Button cellButton;
        private String customText;

        ButtonCell(String type) {
            cellButton = new Button();
            customText = type;
            if ("Overwrite".equals(type)) {
                cellButton.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent t) {
                        SaveSlot currentSaveSlot = getTableView().getItems().get(getIndex());
                        String name = currentSaveSlot.getSaveData().getName();
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Overfile");
                        String s = "Are you sure you want to overwrite your save?";
                        alert.setContentText(s);

                        Optional<ButtonType> result = alert.showAndWait();

                        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                            save(Optional.of(name));
                        }


                    }
                });
            } else {


                cellButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        SaveSlot currentSaveSlot = getTableView().getItems().get(getIndex());
                        String name = currentSaveSlot.getSaveData().getName();

                        SaveSlot saveSlot = getTableView().getItems().get(getIndex());

                        SaveData saveData = saveAndLoadUtility.readSaveFile(saveSlot.getSaveData().getName());
                        playerService.setPlayer(saveData.getPlayer());
                        npcService.setHirableNpcs(saveData.getHirableNpcs());
                        npcService.setHiredNpcs(saveData.getHiredNpcs());
                        npcService.setHired(saveData.getHired());
                        endTurnService.setTurn(saveData.getTurn());
                        dialog.close();
                        screens.hubDialog().show();


                    }
                });
            }

        }

        @Override
        protected void updateItem(SaveSlot record, boolean empty) {
            super.updateItem(record, empty);
            if (!empty) {
                cellButton.setText(customText);
                SaveSlot saveSlot = getTableView().getItems().get(getIndex());
                if ("Empty slot".equals(saveSlot.getSaveData().getName())) {
                    cellButton.setDisable(true);
                }
                setGraphic(cellButton);
            } else {
                setGraphic(null);
            }
        }
    }

}
