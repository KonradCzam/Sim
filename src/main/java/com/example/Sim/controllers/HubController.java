package com.example.Sim.controllers;

import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.Exceptions.ImageNotFound;
import com.example.Sim.FXML.DialogController;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Model.Jobs.Job;
import com.example.Sim.Model.Jobs.Task;
import com.example.Sim.Model.Npc;
import com.example.Sim.Model.Stat;
import com.example.Sim.Services.JobService;
import com.example.Sim.Services.NpcService;
import com.example.Sim.Services.PlayerService;
import com.example.Sim.Utilities.ImageHandler;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.WindowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

@Service
public class HubController implements Initializable, DialogController {

    @FXML
    public TableView hubTable;
    @FXML
    public TableView jobTable;
    @FXML
    public TableView taskTable;
    @FXML
    public ImageView hubImage;

    @FXML
    private TableColumn<Npc, String> dayJobColumn;
    @FXML
    private TableColumn<Npc, String> nightJobColumn;
    @FXML
    private TableColumn<Npc, String> tirednessColumn;
    @FXML
    private TableColumn<Npc, String> skillTableColumn;
    @FXML
    private TableColumn<Npc, String> healthColumn;

    @FXML
    private Pane jobButtonPane;
    @FXML
    private Button goToNpcDetails;

    @FXML
    private Label goldLabel;

    @Resource
    ImageHandler imageHandler;
    @Resource
    JobService jobService;
    @Resource
    NpcService npcService;
    @Resource
    PlayerService playerService;

    Npc selectedHubNpc = null;

    EventHandler<WindowEvent> onShownEventHandler =
            new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    setGoldLabel();
                    goToNpcDetails.setDisable(true);
                    updateTable(npcService.getHiredNpcs());
                }
            };
    private ScreensConfiguration screens;
    private FXMLDialog dialog;

    public HubController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        dialog.setOnShown(onShownEventHandler);
        initiateTable();
        initializeJobs();
        jobButtonPane.setDisable(true);
    }

    public void initializeJobs() {

        TableColumn tableColumn = (TableColumn) jobTable.getColumns().get(0);
        tableColumn.setCellValueFactory(new PropertyValueFactory("name"));
        tableColumn = (TableColumn) taskTable.getColumns().get(0);
        tableColumn.setCellValueFactory(new PropertyValueFactory("name"));

        ObservableList data = FXCollections.observableArrayList(jobService.getJobList());
        jobTable.setItems(data);
        jobTable.getSelectionModel().selectedItemProperty().addListener((obs) -> {
            jobSelected();
        });
        data = FXCollections.observableArrayList(jobService.getJobList().get(0).getTasks());
        taskTable.setItems(data);
        taskTable.getSelectionModel().selectedItemProperty().addListener((obs) -> {
            taskSelected();
        });
    }



    public void initiateTable() {
        hubTable.getSelectionModel().selectedItemProperty().addListener((obs) -> {
            tableRowSelected();
        });
        TableColumn tableColumn = (TableColumn) hubTable.getColumns().get(0);
        tableColumn.setCellValueFactory(new PropertyValueFactory("name"));

        dayJobColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDayShift().getName()));
        nightJobColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getNightShift().getName()));
        tirednessColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(new ArrayList<Stat>(cellData.getValue().getStats().values()).stream()
                .filter(stat -> "Tiredness".equals(stat.getName()))
                .map(Stat::getValue)
                .findAny()
                .orElse(0).toString()));
        healthColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(new ArrayList<Stat>(cellData.getValue().getStats().values()).stream()
                .filter(stat -> "Health".equals(stat.getName()))
                .map(Stat::getValue)
                .findAny()
                .orElse(0).toString()));
        skillTableColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().calculateAverageProficiencyScore(cellData.getValue())));
        hubTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void updateTable(List<Npc> list) {
        ObservableList data = FXCollections.observableArrayList(list);
        hubTable.getItems().remove(0, hubTable.getItems().size());
        hubTable.setItems(data);

    }


    public void setDayJob() {
        setJobs(true, false);
    }

    public void setNightJob() {
        setJobs(false, true);
    }

    public void setBothJobs() {
        setJobs(true, true);
    }

    public void setJobs(Boolean day, Boolean night) {
        Task selectedTask = (Task) taskTable.getSelectionModel().getSelectedItem();
        ObservableList selectedIndices = hubTable.getSelectionModel().getSelectedItems();

        for (int i = 0; i < selectedIndices.size(); i++) {
            Integer index = npcService.getHiredNpcs().indexOf(selectedIndices.get(i));
            if (index != -1) {
                if (day)
                    npcService.getHiredNpcs().get(index).setDayShift(selectedTask);
                if (night)
                    npcService.getHiredNpcs().get(index).setNightShift(selectedTask);
            }
        }
        updateTable(npcService.getHiredNpcs());


    }

    public void tableRowSelected() {
        goToNpcDetails.setDisable(false);
        selectedHubNpc = (Npc) hubTable.getSelectionModel().getSelectedItem();
        if (selectedHubNpc != null) {
            taskTable.getSelectionModel().select(selectedHubNpc.getDayShift());
            updateJobButtonPane();
            try {
                imageHandler.setImage(hubImage, selectedHubNpc.getPath(), "profile", false);
            } catch (ImageNotFound e) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, e.getTextMessage());
                alert.showAndWait();
            }
        }
    }
    private void jobSelected() {

        Job selectedJob = (Job)jobTable.getSelectionModel().getSelectedItem();
        Integer index = jobService.getJobList().indexOf(selectedJob);
        ObservableList data = FXCollections.observableArrayList(jobService.getJobList().get(index).getTasks());
        taskTable.getItems().remove(0, taskTable.getItems().size());
        taskTable.setItems(data);
    }
    private void taskSelected() {
        updateJobButtonPane();
    }
    public void updateJobButtonPane() {
        Task selectedTask = (Task) taskTable.getSelectionModel().getSelectedItem();
        ObservableList selectedIndices = hubTable.getSelectionModel().getSelectedItems();
        if (selectedTask != null && !selectedIndices.isEmpty())
            jobButtonPane.setDisable(false);
        else
            jobButtonPane.setDisable(true);
    }
    public void setGoldLabel(){
        goldLabel.setText("Gold: " + playerService.getPlayerGold());
    }
    //------------------------------------Navigation
    public void goToGallery() {
        dialog.close();
        screens.loginDialog().show();
    }

    public void goToNpcDetails() {
        Npc selectedNpc = ((Npc) hubTable.getSelectionModel().getSelectedItem());
        npcService.setCurrentNpc(selectedNpc);
        dialog.close();

        screens.npcDetailsDialog().show();
    }

    public void goToHire() {
        dialog.close();
        screens.hireDialog().show();
    }

    public void goToPlayer() {
        dialog.close();
        screens.playerDialog().show();
    }

    public void goToSaveLoad() {
        dialog.close();
        screens.saveLoadDialog().show();
    }
    public void gotoToLibrary() {
        dialog.close();
        screens.libraryDialog().show();
    }

    public void endTurn() {
        dialog.close();
        screens.endTurnDialog().show();
    }

}

