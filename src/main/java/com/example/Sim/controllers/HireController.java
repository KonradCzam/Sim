// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.controllers;

import java.util.ArrayList;
import com.example.Sim.Exceptions.ImageNotFound;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import javafx.collections.ObservableList;
import java.util.Collection;
import javafx.collections.FXCollections;
import javafx.util.Callback;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import java.util.ResourceBundle;
import java.net.URL;
import javafx.stage.WindowEvent;
import javafx.event.EventHandler;
import com.example.Sim.Model.NPC.Npc;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.Utilities.ImageHandler;
import com.example.Sim.Services.PlayerService;
import javax.annotation.Resource;
import com.example.Sim.Services.NpcService;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Service;
import com.example.Sim.FXML.DialogController;
import javafx.fxml.Initializable;

@Service
public class HireController implements Initializable, DialogController
{
    @FXML
    private Label hireStatsLabel;
    @FXML
    private Label hireSkillsLabel;
    @FXML
    private ImageView hireImage;
    @FXML
    private TableView hireSkillsTable;
    @FXML
    private TableView hireTraitsTable;
    @FXML
    private TableView hireStatsTable;
    @FXML
    private TableView hireTable;
    @FXML
    private Button hireBuy;
    @Resource
    private NpcService npcService;
    @Resource
    private PlayerService playerService;
    @Resource
    private ImageHandler imageHandler;
    private ScreensConfiguration screens;
    private FXMLDialog dialog;
    private Npc currentNpc;
    EventHandler<WindowEvent> onShownEventHandler;
    
    public HireController(final ScreensConfiguration screens) {
        this.onShownEventHandler = (EventHandler)new HireController$1(this);
        this.screens = screens;
    }
    
    public void initialize(final URL location, final ResourceBundle resources) {
        this.setHiraTableData();
        this.initializeTables();
        this.dialog.setOnShown(this.onShownEventHandler);
    }
    
    public void initializeTables() {
        this.initializeHireTable();
        this.initializeSkillsTable();
        this.initializeStatsTable();
        this.initializeTraitsTable();
    }
    
    public void initializeHireTable() {
        TableColumn tableColumn = (TableColumn)this.hireTable.getColumns().get(0);
        tableColumn.setCellValueFactory((Callback)new PropertyValueFactory("name"));
        tableColumn = (TableColumn)this.hireTable.getColumns().get(1);
        tableColumn.setCellValueFactory((Callback)new PropertyValueFactory("price"));
        this.hireTable.getSelectionModel().selectFirst();
    }
    
    public void initializeSkillsTable() {
        TableColumn tableColumn = (TableColumn)this.hireSkillsTable.getColumns().get(0);
        tableColumn.setCellValueFactory((Callback)new PropertyValueFactory("name"));
        tableColumn = (TableColumn)this.hireSkillsTable.getColumns().get(1);
        tableColumn.setCellValueFactory((Callback)new PropertyValueFactory("value"));
    }
    
    public void initializeStatsTable() {
        TableColumn tableColumn = (TableColumn)this.hireStatsTable.getColumns().get(0);
        tableColumn.setCellValueFactory((Callback)new PropertyValueFactory("name"));
        tableColumn = (TableColumn)this.hireStatsTable.getColumns().get(1);
        tableColumn.setCellValueFactory((Callback)new PropertyValueFactory("value"));
    }
    
    public void initializeTraitsTable() {
        TableColumn tableColumn = (TableColumn)this.hireTraitsTable.getColumns().get(0);
        tableColumn.setCellValueFactory((Callback)new PropertyValueFactory("name"));
        tableColumn = (TableColumn)this.hireTraitsTable.getColumns().get(1);
        tableColumn.setCellValueFactory((Callback)new PropertyValueFactory("effect"));
    }
    
    public void setDialog(final FXMLDialog dialog) {
        this.dialog = dialog;
    }
    
    public void setHiraTableData() {
        final ObservableList data = FXCollections.observableArrayList((Collection)this.npcService.getHirableNpcsList(Integer.valueOf(5)));
        this.hireTable.setItems(data);
    }
    
    public void buy() {
        if (this.playerService.checkIfCanAfford(this.currentNpc.getPrice())) {
            this.npcService.hireNpc(this.currentNpc);
            this.hireTable.getItems().remove((Object)this.currentNpc);
            if (this.hireTable.getItems().isEmpty()) {
                this.hireBuy.setDisable(true);
            }
            else {
                this.hireBuy.setDisable(false);
                this.hireTable.getSelectionModel().selectFirst();
                this.currentNpc = (Npc)this.hireTable.getSelectionModel().getSelectedItem();
            }
            this.playerService.changeGold(Integer.valueOf(-this.currentNpc.getPrice()));
        }
        else {
            final Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You cant afford this slave.", new ButtonType[0]);
            alert.showAndWait();
        }
    }
    
    public void tableRowSelected() {
        this.currentNpc = (Npc)this.hireTable.getSelectionModel().getSelectedItem();
        try {
            this.imageHandler.setImage(this.hireImage, this.currentNpc.getPath(), "profile", false);
        }
        catch (ImageNotFound e) {
            final Alert alert = new Alert(Alert.AlertType.CONFIRMATION, e.getTextMessage(), new ButtonType[0]);
            alert.showAndWait();
        }
        this.refreshNpcSelect(this.currentNpc);
    }
    
    public void refreshNpcSelect(final Npc npc) {
        this.currentNpc = npc;
        this.hireSkillsLabel.setText(this.currentNpc.getName() + "'s base skills");
        this.hireStatsLabel.setText(this.currentNpc.getName() + "'s  base stats");
        ObservableList data = FXCollections.observableArrayList((Collection)new ArrayList(npc.getSkills().values()));
        this.hireSkillsTable.setItems(data);
        data = FXCollections.observableArrayList((Collection)new ArrayList(npc.getStats().values()));
        this.hireStatsTable.setItems(data);
        data = FXCollections.observableArrayList((Collection)npc.getTraits());
        this.hireTraitsTable.setItems(data);
    }
    
    public void goToHub() {
        this.dialog.close();
        this.screens.hubDialog().show();
    }
}
