// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.controllers;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import java.util.List;
import java.util.Collection;
import javafx.collections.FXCollections;
import java.util.concurrent.TimeUnit;
import java.util.Optional;
import javafx.scene.control.TextInputDialog;
import javafx.util.Callback;
import java.util.ResourceBundle;
import java.net.URL;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.Model.SaveSlot;
import javafx.scene.control.TableColumn;
import javafx.stage.WindowEvent;
import javafx.event.EventHandler;
import com.example.Sim.Services.EndTurnService;
import com.example.Sim.Utilities.SaveAndLoadUtility;
import com.example.Sim.Services.PlayerService;
import javax.annotation.Resource;
import com.example.Sim.Services.NpcService;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import org.springframework.stereotype.Service;
import com.example.Sim.FXML.DialogController;
import javafx.fxml.Initializable;

@Service
public class SaveLoadController implements Initializable, DialogController
{
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
    EventHandler<WindowEvent> onShownEventHandler;
    @FXML
    private TableColumn<SaveSlot, String> saveNameColumn;
    @FXML
    private TableColumn<SaveSlot, String> saveTurnColumn;
    @FXML
    private TableColumn<SaveSlot, String> saveDateColumn;
    private ScreensConfiguration screens;
    private FXMLDialog dialog;
    
    public SaveLoadController(final ScreensConfiguration screens) {
        this.onShownEventHandler = (EventHandler)new SaveLoadController$1(this);
        this.screens = screens;
    }
    
    public void setDialog(final FXMLDialog dialog) {
        this.dialog = dialog;
    }
    
    public void initialize(final URL location, final ResourceBundle resources) {
        this.initializeSaveLoadTable();
        this.dialog.setOnShown(this.onShownEventHandler);
    }
    
    private void initializeSaveLoadTable() {
        this.saveNameColumn.setCellValueFactory(SaveLoadController::lambda$initializeSaveLoadTable$0);
        this.saveDateColumn.setCellValueFactory(SaveLoadController::lambda$initializeSaveLoadTable$1);
        this.saveTurnColumn.setCellValueFactory(SaveLoadController::lambda$initializeSaveLoadTable$2);
        TableColumn tableColumn = (TableColumn)this.loadGameTable.getColumns().get(3);
        tableColumn.setCellFactory((Callback)new SaveLoadController$2(this));
        tableColumn = (TableColumn)this.loadGameTable.getColumns().get(4);
        tableColumn.setCellFactory((Callback)new SaveLoadController$3(this));
    }
    
    public void save() {
        final TextInputDialog dialog = new TextInputDialog("save name");
        dialog.setTitle("Name your save");
        dialog.setHeaderText("Enter save name, or use default value.");
        final Optional<String> name = (Optional<String>)dialog.showAndWait();
        this.save(name);
    }
    
    private void save(final Optional<String> name) {
        final SaveSlot saveSlot = this.saveAndLoadUtility.createSaveSlot((Optional)name);
        this.saveAndLoadUtility.saveGame(saveSlot);
        try {
            TimeUnit.SECONDS.sleep(1L);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.refreshTable();
    }
    
    public void refreshTable() {
        final List<SaveSlot> saveSlots = (List<SaveSlot>)this.saveAndLoadUtility.getSavedGames();
        final ObservableList data = FXCollections.observableArrayList((Collection)saveSlots);
        this.loadGameTable.getItems().remove(0, this.loadGameTable.getItems().size());
        this.loadGameTable.setItems(data);
    }
    
    public void goBack() {
        this.dialog.close();
        this.screens.hubDialog().show();
    }
    
    public void goToMain() {
        this.dialog.close();
        this.screens.startDialog().show();
    }
    
    private class ButtonCell extends TableCell<SaveSlot, SaveSlot>
    {
        private Button cellButton;
        private String customText;
        
        ButtonCell(final SaveLoadController this$0, final String type) {
            this.this$0 = this$0;
            this.cellButton = new Button();
            this.customText = type;
            if ("Overwrite".equals(type)) {
                this.cellButton.setOnAction((EventHandler)new ButtonCell.SaveLoadController$ButtonCell$1(this, this$0));
            }
            else {
                this.cellButton.setOnAction((EventHandler)new ButtonCell.SaveLoadController$ButtonCell$2(this, this$0));
            }
        }
        
        protected void updateItem(final SaveSlot record, final boolean empty) {
            super.updateItem((Object)record, empty);
            if (!empty) {
                this.cellButton.setText(this.customText);
                final SaveSlot saveSlot = (SaveSlot)this.getTableView().getItems().get(this.getIndex());
                if ("Empty slot".equals(saveSlot.getSaveData().getName())) {
                    this.cellButton.setDisable(true);
                }
                this.setGraphic((Node)this.cellButton);
            }
            else {
                this.setGraphic((Node)null);
            }
        }
    }
}
