// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.controllers;

import java.util.function.Function;
import com.example.Sim.Model.NPC.Stat;
import java.util.function.Predicate;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.beans.Observable;
import com.example.Sim.Exceptions.ImageNotFound;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import com.example.Sim.Model.Jobs.Task;
import com.example.Sim.Model.Jobs.JobStat;
import javafx.collections.ObservableList;
import com.example.Sim.Model.Jobs.Job;
import java.util.Collection;
import javafx.collections.FXCollections;
import javafx.scene.control.Tab;
import java.util.function.Consumer;
import javafx.scene.control.SelectionMode;
import javafx.util.Callback;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.ResourceBundle;
import java.net.URL;
import java.util.ArrayList;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Config.ScreensConfiguration;
import javafx.stage.WindowEvent;
import javafx.event.EventHandler;
import java.util.List;
import com.example.Sim.Services.EndTurnService;
import com.example.Sim.Services.PlayerService;
import com.example.Sim.Services.NpcService;
import com.example.Sim.Services.JobService;
import javax.annotation.Resource;
import com.example.Sim.Utilities.ImageHandler;
import javafx.scene.control.TabPane;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import com.example.Sim.Model.NPC.Npc;
import javafx.scene.control.TableColumn;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import org.springframework.stereotype.Service;
import com.example.Sim.FXML.DialogController;
import javafx.fxml.Initializable;

@Service
public class HubController implements Initializable, DialogController
{
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
    Npc selectedHubNpc;
    List<ProgressBar> dayProgressBars;
    List<Label> dayProgressLabels;
    List<ProgressBar> nightProgressBars;
    List<Label> nightProgressLabels;
    EventHandler<WindowEvent> onShownEventHandler;
    private ScreensConfiguration screens;
    private FXMLDialog dialog;
    private String currentJobName;
    
    public HubController(final ScreensConfiguration screens) {
        this.selectedHubNpc = null;
        this.dayProgressBars = new ArrayList();
        this.dayProgressLabels = new ArrayList();
        this.nightProgressBars = new ArrayList();
        this.nightProgressLabels = new ArrayList();
        this.onShownEventHandler = (EventHandler)new HubController$1(this);
        this.screens = screens;
    }
    
    public void setDialog(final FXMLDialog dialog) {
        this.dialog = dialog;
    }
    
    public void initialize(final URL location, final ResourceBundle resources) {
        this.dialog.setOnShown(this.onShownEventHandler);
        this.initiateTable();
        this.initJobTab();
        this.initiateJobTabs();
        this.createProgBarsList();
        this.jobButtonPane.setDisable(true);
        this.jobTabs.getSelectionModel().select(0);
        this.tabSelected();
    }
    
    private void createProgBarsList() {
        this.dayProgressBars.add(this.progDay1);
        this.dayProgressBars.add(this.progDay2);
        this.dayProgressBars.add(this.progDay3);
        this.dayProgressBars.add(this.progDay4);
        this.dayProgressBars.add(this.progDay5);
        this.dayProgressBars.add(this.progDay6);
        this.dayProgressBars.add(this.progDay7);
        this.dayProgressLabels.add(this.progDayLabel1);
        this.dayProgressLabels.add(this.progDayLabel2);
        this.dayProgressLabels.add(this.progDayLabel3);
        this.dayProgressLabels.add(this.progDayLabel4);
        this.dayProgressLabels.add(this.progDayLabel5);
        this.dayProgressLabels.add(this.progDayLabel6);
        this.dayProgressLabels.add(this.progDayLabel7);
        this.nightProgressBars.add(this.progNight1);
        this.nightProgressBars.add(this.progNight2);
        this.nightProgressBars.add(this.progNight3);
        this.nightProgressBars.add(this.progNight4);
        this.nightProgressBars.add(this.progNight5);
        this.nightProgressBars.add(this.progNight6);
        this.nightProgressBars.add(this.progNight7);
        this.nightProgressLabels.add(this.progNightLabel1);
        this.nightProgressLabels.add(this.progNightLabel2);
        this.nightProgressLabels.add(this.progNightLabel3);
        this.nightProgressLabels.add(this.progNightLabel4);
        this.nightProgressLabels.add(this.progNightLabel5);
        this.nightProgressLabels.add(this.progNightLabel6);
        this.nightProgressLabels.add(this.progNightLabel7);
    }
    
    public void initiateTable() {
        this.hubTable.getSelectionModel().selectedItemProperty().addListener(this::lambda$initiateTable$0);
        final TableColumn tableColumn = (TableColumn)this.hubTable.getColumns().get(0);
        tableColumn.setCellValueFactory((Callback)new PropertyValueFactory("name"));
        this.dayJobColumn.setCellValueFactory(HubController::lambda$initiateTable$1);
        this.nightJobColumn.setCellValueFactory(HubController::lambda$initiateTable$2);
        this.tirednessColumn.setCellValueFactory(HubController::lambda$initiateTable$4);
        this.healthColumn.setCellValueFactory(HubController::lambda$initiateTable$6);
        this.skillTableColumn.setCellValueFactory(this::lambda$initiateTable$7);
        this.hubTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
    
    public void initiateJobTabs() {
        this.jobService.getJobList().forEach(this::lambda$initiateJobTabs$8);
        this.jobTabs.getSelectionModel().selectedItemProperty().addListener(this::lambda$initiateJobTabs$9);
        this.jobTabs.getSelectionModel().select(0);
    }
    
    private void tabSelected() {
        this.currentJobName = ((Tab)this.jobTabs.getSelectionModel().getSelectedItem()).getText();
        this.displayProgBars();
        this.setPopLabels();
        this.updateJobTab();
    }
    
    private void displayProgBars() {
        if (this.currentJobName != null) {
            this.updateProgBars();
        }
    }
    
    private void updateJobTab() {
        final Job currentJob = this.jobService.getJobByName(this.currentJobName);
        final ObservableList data = FXCollections.observableArrayList((Collection)currentJob.getTasks());
        this.taskTable.getItems().remove(0, this.taskTable.getItems().size());
        this.taskTable.setItems(data);
    }
    
    private void updateProgBars() {
        final Job job = this.jobService.getJobByName(this.currentJobName);
        final List<JobStat> jobStats = (List<JobStat>)job.getJobStats();
        for (int i = 0; i < jobStats.size(); ++i) {
            this.dayProgressBars.get(i).setProgress(jobStats.get(i).getDayValue() / 100.0);
            this.dayProgressLabels.get(i).setText(jobStats.get(i).getStatName());
            this.nightProgressBars.get(i).setProgress(jobStats.get(i).getNightValue() / 100.0);
            this.nightProgressLabels.get(i).setText(jobStats.get(i).getStatName());
            this.dayProgressBars.get(i).setVisible(true);
            this.dayProgressLabels.get(i).setVisible(true);
            this.nightProgressBars.get(i).setVisible(true);
            this.nightProgressLabels.get(i).setVisible(true);
        }
        for (int i = jobStats.size(); i < 7; ++i) {
            this.dayProgressBars.get(i).setVisible(false);
            this.dayProgressLabels.get(i).setVisible(false);
            this.nightProgressBars.get(i).setVisible(false);
            this.nightProgressLabels.get(i).setVisible(false);
        }
        if (jobStats == null || jobStats.size() == 0) {
            this.progNightLabel4.setText("No stats for this job");
            this.progNightLabel4.setVisible(true);
            this.progDayLabel4.setText("No stats for this job");
            this.progDayLabel4.setVisible(true);
        }
    }
    
    private void setPopLabels() {
        final Job job = this.jobService.getJobByName(this.currentJobName);
        this.dayPopLabel.setText("Day popularity: " + job.getPopularityDayLow() + " | " + job.getPopularityDayMid() + " | " + job.getPopularityDayHigh());
        this.nightPopLabel.setText("Night popularity: " + job.getPopularityNightLow() + " | " + job.getPopularityNightMid() + " | " + job.getPopularityNightHigh());
    }
    
    private void initJobTab() {
        final TableColumn tableColumn = (TableColumn)this.taskTable.getColumns().get(0);
        tableColumn.setCellValueFactory((Callback)new PropertyValueFactory("name"));
        this.taskTable.getSelectionModel().selectedItemProperty().addListener(this::lambda$initJobTab$10);
    }
    
    public void updateTable(final List<Npc> list) {
        final ObservableList data = FXCollections.observableArrayList((Collection)list);
        this.hubTable.getItems().remove(0, this.hubTable.getItems().size());
        this.hubTable.setItems(data);
    }
    
    public void setDayJob() {
        this.setJobs(true, false);
    }
    
    public void setNightJob() {
        this.setJobs(false, true);
    }
    
    public void setBothJobs() {
        this.setJobs(true, true);
    }
    
    public void setJobs(final Boolean day, final Boolean night) {
        final Task selectedTask = (Task)this.taskTable.getSelectionModel().getSelectedItem();
        final Job selectedJob = this.jobService.getJobByName(this.currentJobName);
        final ObservableList selectedIndices = this.hubTable.getSelectionModel().getSelectedItems();
        for (int i = 0; i < selectedIndices.size(); ++i) {
            final Integer index = this.npcService.getHiredNpcs().indexOf(selectedIndices.get(i));
            if (index != -1) {
                if (day) {
                    this.npcService.getHiredNpcs().get(index).setDayShift(selectedTask);
                }
                if (night) {
                    this.npcService.getHiredNpcs().get(index).setNightShift(selectedTask);
                }
            }
        }
        this.updateTable(this.npcService.getHiredNpcs());
        this.jobService.calculateJobAllStats(selectedJob);
        this.updateProgBars();
    }
    
    public void tableRowSelected() {
        this.selectedHubNpc = (Npc)this.hubTable.getSelectionModel().getSelectedItem();
        if (this.selectedHubNpc != null) {
            this.goToNpcDetails.setDisable(false);
            this.taskTable.getSelectionModel().select((Object)this.selectedHubNpc.getDayShift());
            this.updateJobButtonPane();
            try {
                this.imageHandler.setImage(this.hubImage, this.selectedHubNpc.getPath(), "profile", false);
            }
            catch (ImageNotFound e) {
                final Alert alert = new Alert(Alert.AlertType.CONFIRMATION, e.getTextMessage(), new ButtonType[0]);
                alert.showAndWait();
            }
        }
    }
    
    private void taskSelected() {
        this.updateJobButtonPane();
    }
    
    public void updateJobButtonPane() {
        final Task selectedTask = (Task)this.taskTable.getSelectionModel().getSelectedItem();
        final ObservableList selectedIndices = this.hubTable.getSelectionModel().getSelectedItems();
        if (selectedTask != null && !selectedIndices.isEmpty()) {
            this.jobButtonPane.setDisable(false);
        }
        else {
            this.jobButtonPane.setDisable(true);
        }
    }
    
    public void setGoldLabel() {
        this.goldLabel.setText("Gold: " + this.playerService.getPlayerGold());
    }
    
    public void sellNpcs() {
        final ObservableList selectedNpcs = this.hubTable.getSelectionModel().getSelectedItems();
        this.npcService.sellNpcs((List)selectedNpcs);
        this.updateTable(this.npcService.getHiredNpcs());
    }
    
    public void goToGallery() {
        this.dialog.close();
        this.screens.loginDialog().show();
    }
    
    public void goToNpcDetails() {
        final Npc selectedNpc = (Npc)this.hubTable.getSelectionModel().getSelectedItem();
        this.npcService.setCurrentNpc(selectedNpc);
        this.dialog.close();
        this.screens.npcDetailsDialog().show();
    }
    
    public void goToHire() {
        this.dialog.close();
        this.screens.hireDialog().show();
    }
    
    public void goToPlayer() {
        this.dialog.close();
        this.screens.playerDialog().show();
    }
    
    public void goToSaveLoad() {
        this.dialog.close();
        this.screens.saveLoadDialog().show();
    }
    
    public void gotoToLibrary() {
        this.dialog.close();
        this.screens.libraryDialog().show();
    }
    
    public void goToOldEndTurn() {
        this.endTurnService.setPresentOld(Boolean.valueOf(true));
        this.dialog.close();
        this.screens.endTurnDialog().show();
    }
    
    public void endTurn() {
        this.dialog.close();
        this.screens.endTurnDialog().show();
    }
}
