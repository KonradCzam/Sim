package com.example.Sim.controllers;

import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.Exceptions.ImageNotFound;
import com.example.Sim.FXML.DialogController;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Model.NPC.Npc;
import com.example.Sim.Model.NPC.Skill;
import com.example.Sim.Model.NPC.Stat;
import com.example.Sim.Services.NpcService;
import com.example.Sim.Services.PlayerService;
import com.example.Sim.Utilities.ImageHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.WindowEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Service
public class HireController implements Initializable, DialogController {

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
    EventHandler<WindowEvent> onShownEventHandler =
            new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    setHiraTableData();
                    if (hireTable.getItems().isEmpty()) {
                        hireBuy.setDisable(true);
                    } else {
                        hireBuy.setDisable(false);
                        hireTable.getSelectionModel().selectFirst();
                        currentNpc = (Npc) hireTable.getSelectionModel().getSelectedItem();
                        tableRowSelected();
                    }

                }
            };

    public HireController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setHiraTableData();
        initializeTables();
        dialog.setOnShown(onShownEventHandler);

    }

    public void initializeTables() {
        initializeHireTable();
        initializeSkillsTable();
        initializeStatsTable();
        initializeTraitsTable();
    }

    public void initializeHireTable() {

        TableColumn tableColumn = (TableColumn) hireTable.getColumns().get(0);
        tableColumn.setCellValueFactory(new PropertyValueFactory("name"));
        tableColumn = (TableColumn) hireTable.getColumns().get(1);
        tableColumn.setCellValueFactory(new PropertyValueFactory("price"));

        hireTable.getSelectionModel().selectFirst();
    }

    public void initializeSkillsTable() {
        TableColumn tableColumn = (TableColumn) hireSkillsTable.getColumns().get(0);
        tableColumn.setCellValueFactory(new PropertyValueFactory("name"));
        tableColumn = (TableColumn) hireSkillsTable.getColumns().get(1);
        tableColumn.setCellValueFactory(new PropertyValueFactory("value"));
    }

    public void initializeStatsTable() {
        TableColumn tableColumn = (TableColumn) hireStatsTable.getColumns().get(0);
        tableColumn.setCellValueFactory(new PropertyValueFactory("name"));
        tableColumn = (TableColumn) hireStatsTable.getColumns().get(1);
        tableColumn.setCellValueFactory(new PropertyValueFactory("value"));
    }

    public void initializeTraitsTable() {
        TableColumn tableColumn = (TableColumn) hireTraitsTable.getColumns().get(0);
        tableColumn.setCellValueFactory(new PropertyValueFactory("name"));
        tableColumn = (TableColumn) hireTraitsTable.getColumns().get(1);
        tableColumn.setCellValueFactory(new PropertyValueFactory("effect"));
    }

    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }

    public void setHiraTableData() {

        ObservableList data = FXCollections.observableArrayList(npcService.getHirableNpcsList(5));
        hireTable.setItems(data);
    }

    public void buy() {
        if(playerService.checkIfCanAfford(currentNpc.getPrice())){
            npcService.hireNpc(currentNpc);
            hireTable.getItems().remove(currentNpc);
            if (hireTable.getItems().isEmpty()) {
                hireBuy.setDisable(true);
            } else {
                hireBuy.setDisable(false);
                hireTable.getSelectionModel().selectFirst();
                currentNpc = (Npc) hireTable.getSelectionModel().getSelectedItem();
            }
            playerService.changeGold(-currentNpc.getPrice());
        }
        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You cant afford this slave.");
            alert.showAndWait();
        }


    }

    public void tableRowSelected() {
        currentNpc = (Npc) hireTable.getSelectionModel().getSelectedItem();
        try {
            imageHandler.setImage(hireImage, currentNpc.getPath(), "profile", false);
        } catch (ImageNotFound e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, e.getTextMessage());
            alert.showAndWait();
        }
        refreshNpcSelect(currentNpc);
    }

    public void refreshNpcSelect(Npc npc) {
        currentNpc = npc;
        hireSkillsLabel.setText(currentNpc.getName() + "'s base skills");
        hireStatsLabel.setText(currentNpc.getName() + "'s  base stats");
        ObservableList data = FXCollections.observableArrayList(new ArrayList<Skill>(npc.getSkills().values()));
        hireSkillsTable.setItems(data);
        data = FXCollections.observableArrayList(new ArrayList<Stat>(npc.getStats().values()));
        hireStatsTable.setItems(data);
        data = FXCollections.observableArrayList(npc.getTraits());
        hireTraitsTable.setItems(data);

    }

    public void goToHub() {
        dialog.close();
        screens.hubDialog().show();
    }


}
