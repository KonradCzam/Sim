package com.example.Sim.controllers;

import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.Exceptions.ImageNotFound;
import com.example.Sim.FXML.DialogController;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Model.Jobs.Job;
import com.example.Sim.Model.Jobs.Task;
import com.example.Sim.Model.NPC.Npc;
import com.example.Sim.Model.NPC.Stat;
import com.example.Sim.Services.EndTurnService;
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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Service
public class HubController {

    @FXML
    public TableView hubTable;
    @FXML
    public ImageView hubImage;

    @FXML
    private TableColumn<Npc, String> dayJobColumn;
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

    @FXML
    private TabPane jobTabs;
    @FXML
    private TableView taskTable;

    @Resource
    ImageHandler imageHandler;
    @Resource
    JobService jobService;
    @Resource
    NpcService npcService;
    @Resource
    PlayerService playerService;
    @Resource
    EndTurnService endTurnService;
    Npc selectedHubNpc = null;
    List<ProgressBar> dayProgressBars = new ArrayList<>();
    List<Label> dayProgressLabels = new ArrayList<>();
    List<ProgressBar> nightProgressBars = new ArrayList<>();
    List<Label> nightProgressLabels = new ArrayList<>();
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
    private String currentJobName;

    public HubController() {
    }

    public HubController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }


    public void initialize(URL location, ResourceBundle resources) {
        npcService.createNpcs();
        npcService.shuffleHirable();
        initiateTable();
        initJobTab();
        initiateJobTabs();
        jobButtonPane.setDisable(true);
        jobTabs.getSelectionModel().select(0);
        tabSelected();
    }

    public void initiateTable() {
        hubTable.getSelectionModel().selectedItemProperty().addListener((obs) -> {
            tableRowSelected();
        });
        TableColumn tableColumn = (TableColumn) hubTable.getColumns().get(0);
        tableColumn.setCellValueFactory(new PropertyValueFactory("name"));

        dayJobColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTask().getName()));
        tirednessColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(new ArrayList<Stat>(cellData.getValue().getStats().values()).stream()
                .filter(stat -> "Tiredness".equals(stat.getName()))
                .map(Stat::getEffectiveValue)
                .findAny()
                .orElse(0).toString()));
        healthColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(new ArrayList<Stat>(cellData.getValue().getStats().values()).stream()
                .filter(stat -> "Health".equals(stat.getName()))
                .map(Stat::getEffectiveValue)
                .findAny()
                .orElse(0).toString()));
        skillTableColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(jobService.calculateAverageProficiencyScore(cellData.getValue())));
        hubTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
    public void initiateJobTabs(){
        jobService.getJobList().forEach(job -> {
            Tab tab = new Tab();
            tab.setText(job.getName());
            jobTabs.getTabs().add(tab);
        });
        jobTabs.getSelectionModel().selectedItemProperty().addListener((obs) -> {
            tabSelected();
        });
        jobTabs.getSelectionModel().select(0);
    }

    private void tabSelected() {
        currentJobName = jobTabs.getSelectionModel().getSelectedItem().getText();
        updateJobTab();
    }



    private void updateJobTab() {
        Job currentJob = jobService.getJobByName(currentJobName);
        ObservableList data = FXCollections.observableArrayList((currentJob.getTasks()));
        taskTable.getItems().remove(0, taskTable.getItems().size());
        taskTable.setItems(data);


    }




    private void initJobTab() {
        TableColumn tableColumn = (TableColumn)taskTable.getColumns().get(0);
        tableColumn.setCellValueFactory(new PropertyValueFactory("name"));
        taskTable.getSelectionModel().selectedItemProperty().addListener((obs) -> {
            taskSelected();
        });
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
        Job selectedJob = (Job) jobService.getJobByName(currentJobName);
        ObservableList selectedIndices = hubTable.getSelectionModel().getSelectedItems();

        for (int i = 0; i < selectedIndices.size(); i++) {
            Integer index = npcService.getHiredNpcs().indexOf(selectedIndices.get(i));
            if (index != -1) {
                if (day)
                    npcService.getHiredNpcs().get(index).setTask(selectedTask);

            }
        }
        updateTable(npcService.getHiredNpcs());

    }

    public void tableRowSelected() {
        selectedHubNpc = (Npc) hubTable.getSelectionModel().getSelectedItem();
        if (selectedHubNpc != null) {
            goToNpcDetails.setDisable(false);
            taskTable.getSelectionModel().select(selectedHubNpc.getTask());
            updateJobButtonPane();
            try {
                imageHandler.setImage(hubImage, selectedHubNpc.getPath(), "profile", false);
            } catch (ImageNotFound e) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, e.getTextMessage());
                alert.showAndWait();
            }
        }
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

    public void sellNpcs(){
        ObservableList selectedNpcs = hubTable.getSelectionModel().getSelectedItems();
        npcService.sellNpcs(selectedNpcs);
        updateTable(npcService.getHiredNpcs());
    }
    //------------------------------------Navigation
    public void goToGallery() {
       screens.activate("gallery");
    }

    public void goToNpcDetails() {
        Npc selectedNpc = ((Npc) hubTable.getSelectionModel().getSelectedItem());
        npcService.setCurrentNpc(selectedNpc);

        screens.activate("npcDetails");
    }

    public void goToHire() {
        screens.activate("hire");
    }

    public void goToPlayer() {
        screens.activate("playerDetails");
    }

    public void goToSaveLoad() {
        screens.activate("saveLoad");
    }
    public void gotoToLibrary() {
        screens.activate("library");
    }
    public void goToOldEndTurn() {
        endTurnService.setPresentOld(true);
        screens.activate("endTurn");
    }
    public void endTurn() {
        screens.activate("endTurn");
    }

}

