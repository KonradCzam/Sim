package com.example.Sim.controllers;

import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.Exceptions.ImageNotFound;
import com.example.Sim.FXML.DialogController;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Model.Jobs.Job;
import com.example.Sim.Model.Jobs.JobStat;
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
public class HubController implements Initializable, DialogController {

    @FXML
    public TableView hubTable;
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
    @FXML
    private ProgressBar cleanConcern;
    @FXML
    private ProgressBar progDay1;
    @FXML
    private ProgressBar progDay2;
    @FXML
    private ProgressBar progDay3;
    @FXML
    private ProgressBar progDay4;
    @FXML
    private ProgressBar progDay5;
    @FXML
    private ProgressBar progDay6;
    @FXML
    private ProgressBar progDay7;
    @FXML
    private ProgressBar progNight1;
    @FXML
    private ProgressBar progNight2;
    @FXML
    private ProgressBar progNight3;
    @FXML
    private ProgressBar progNight4;
    @FXML
    private ProgressBar progNight5;
    @FXML
    private ProgressBar progNight6;
    @FXML
    private ProgressBar progNight7;
    @FXML
    private Label progDayLabel1;
    @FXML
    private Label progDayLabel2;
    @FXML
    private Label progDayLabel3;
    @FXML
    private Label progDayLabel4;
    @FXML
    private Label progDayLabel5;
    @FXML
    private Label progDayLabel6;
    @FXML
    private Label progDayLabel7;
    @FXML
    private Label progNightLabel1;
    @FXML
    private Label progNightLabel2;
    @FXML
    private Label progNightLabel3;
    @FXML
    private Label progNightLabel4;
    @FXML
    private Label progNightLabel5;
    @FXML
    private Label progNightLabel6;
    @FXML
    private Label progNightLabel7;
    @FXML
    private Label nightPopLabel;
    @FXML
    private Label dayPopLabel;

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

                    displayProgBars();
                    setPopLabels();
                }
            };
    private ScreensConfiguration screens;
    private FXMLDialog dialog;
    private String currentJobName;
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
        initJobTab();
        initiateJobTabs();
        createProgBarsList();
        jobButtonPane.setDisable(true);
        jobTabs.getSelectionModel().select(0);
        tabSelected();
    }

    private void createProgBarsList() {
        
        dayProgressBars.add(progDay1);
        dayProgressBars.add(progDay2);
        dayProgressBars.add(progDay3);
        dayProgressBars.add(progDay4);
        dayProgressBars.add(progDay5);
        dayProgressBars.add(progDay6);
        dayProgressBars.add(progDay7);

        dayProgressLabels.add(progDayLabel1);
        dayProgressLabels.add(progDayLabel2);
        dayProgressLabels.add(progDayLabel3);
        dayProgressLabels.add(progDayLabel4);
        dayProgressLabels.add(progDayLabel5);
        dayProgressLabels.add(progDayLabel6);
        dayProgressLabels.add(progDayLabel7);
        
        nightProgressBars.add(progNight1);
        nightProgressBars.add(progNight2);
        nightProgressBars.add(progNight3);
        nightProgressBars.add(progNight4);
        nightProgressBars.add(progNight5);
        nightProgressBars.add(progNight6);
        nightProgressBars.add(progNight7);

        nightProgressLabels.add(progNightLabel1);
        nightProgressLabels.add(progNightLabel2);
        nightProgressLabels.add(progNightLabel3);
        nightProgressLabels.add(progNightLabel4);
        nightProgressLabels.add(progNightLabel5);
        nightProgressLabels.add(progNightLabel6);
        nightProgressLabels.add(progNightLabel7);
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
        displayProgBars();
        setPopLabels();
        updateJobTab();
    }

    private void displayProgBars() {
        if(currentJobName != null) {
                updateProgBars();
        }
    }

    private void updateJobTab() {
        Job currentJob = jobService.getJobByName(currentJobName);
        ObservableList data = FXCollections.observableArrayList((currentJob.getTasks()));
        taskTable.getItems().remove(0, taskTable.getItems().size());
        taskTable.setItems(data);


    }

    private void updateProgBars() {
        Job job = jobService.getJobByName(currentJobName);
        List<JobStat> jobStats = job.getJobStats();

        for(int i = 0; i<jobStats.size();i++){
            dayProgressBars.get(i).setProgress(jobStats.get(i).getDayValue()/100);
            dayProgressLabels.get(i).setText(jobStats.get(i).getStatName());
            nightProgressBars.get(i).setProgress(jobStats.get(i).getNightValue()/100);
            nightProgressLabels.get(i).setText(jobStats.get(i).getStatName());
            dayProgressBars.get(i).setVisible(true);
            dayProgressLabels.get(i).setVisible(true);
            nightProgressBars.get(i).setVisible(true);
            nightProgressLabels.get(i).setVisible(true);
        };
        for (int i = jobStats.size(); i < 7; i++) {
            dayProgressBars.get(i).setVisible(false);
            dayProgressLabels.get(i).setVisible(false);
            nightProgressBars.get(i).setVisible(false);
            nightProgressLabels.get(i).setVisible(false);
        }
        if(jobStats == null || jobStats.size() == 0){
            progNightLabel4.setText("No stats for this job");
            progNightLabel4.setVisible(true);
            progDayLabel4.setText("No stats for this job");
            progDayLabel4.setVisible(true);
        }


    }

    private void setPopLabels() {
        Job job = jobService.getJobByName(currentJobName);
        dayPopLabel.setText("Day popularity: " + job.getPopularityDayLow() + " | " + job.getPopularityDayMid()  + " | " + job.getPopularityDayHigh());
        nightPopLabel.setText("Night popularity: " + job.getPopularityNightLow() + " | " + job.getPopularityNightMid()  + " | " + job.getPopularityNightHigh());
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
                    npcService.getHiredNpcs().get(index).setDayShift(selectedTask);
                if (night)
                    npcService.getHiredNpcs().get(index).setNightShift(selectedTask);
            }
        }
        updateTable(npcService.getHiredNpcs());
        jobService.calculateJobAllStats(selectedJob);
        updateProgBars();

    }

    public void tableRowSelected() {
        selectedHubNpc = (Npc) hubTable.getSelectionModel().getSelectedItem();
        if (selectedHubNpc != null) {
            goToNpcDetails.setDisable(false);
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
    public void goToOldEndTurn() {
        endTurnService.setPresentOld(true);
        dialog.close();
        screens.endTurnDialog().show();
    }
    public void endTurn() {
        dialog.close();
        screens.endTurnDialog().show();
    }

}

