// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.controllers.library;

import javafx.beans.Observable;
import com.example.Sim.Model.Jobs.Task;
import javafx.collections.ObservableList;
import java.util.List;
import com.example.Sim.Model.Jobs.Job;
import java.util.Collection;
import javafx.collections.FXCollections;
import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.Sim.Services.JobService;
import javafx.scene.control.TextArea;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Configurable;
import javafx.fxml.Initializable;

@Configurable
public class TaskController implements Initializable
{
    @FXML
    ListView libraryJobs;
    @FXML
    ListView libraryTasks;
    @FXML
    TextArea taskDescription;
    @FXML
    TextArea taskNotes;
    @Autowired
    JobService jobService;
    
    public void initialize(final URL location, final ResourceBundle resources) {
        this.initializeLists();
        final String text2 = "Type decides whether there is a single node or multiple nodes in the end turn report.\nMoney coefficient is a multiplyer used in calculation of money earned for the job.\nDefault pic category a picture from this category is returned if there is no picture found that matches the job more \nThreshold type decides which stat is used to check if npc will refuse the task.\nThe higher the threshold value the more obedient or morally flexible the npc must be to finish the task.\nNote the value does not represent the exact value, its a simplification, there are more calculations involved.";
        this.taskNotes.setText(text2);
    }
    
    private void initializeLists() {
        final List<String> jobNames = new ArrayList<String>();
        this.jobService.getJobList().forEach(TaskController::lambda$initializeLists$0);
        ObservableList data = FXCollections.observableArrayList((Collection)jobNames);
        this.libraryJobs.setItems(data);
        this.libraryJobs.getSelectionModel().selectedItemProperty().addListener(this::lambda$initializeLists$1);
        final List<String> taskNames = new ArrayList<String>();
        this.jobService.getJobList().get(0).getTasks().forEach(TaskController::lambda$initializeLists$2);
        data = FXCollections.observableArrayList((Collection)taskNames);
        this.libraryTasks.setItems(data);
        this.libraryTasks.getSelectionModel().selectedItemProperty().addListener(this::lambda$initializeLists$3);
    }
    
    private void taskSelected() {
        final String taskName = (String)this.libraryTasks.getSelectionModel().getSelectedItem();
        final Task task = JobService.allTasks.get(taskName);
        if (task != null) {
            final String text = "Name: " + task.getName() + "\nDescription: " + task.getDescription() + "\nType: " + task.getType() + "\nTiring: " + task.getTiring() + "\nMoney Coefficient: " + task.getMoneyCoefficient().toString() + "\nExperience gain: " + task.getExpGain().toString() + "\nDefault pic category: " + task.getDefaultCat() + "\nThreshold type: " + task.getType() + "\nThreshold value: " + task.getThreshold();
            this.taskDescription.setText(text);
        }
    }
    
    private void jobSelected() {
        final List<String> taskNames = new ArrayList<String>();
        final Integer index = this.libraryJobs.getSelectionModel().getSelectedIndex();
        this.jobService.getJobList().get(index).getTasks().forEach(TaskController::lambda$jobSelected$4);
        final ObservableList data = FXCollections.observableArrayList((Collection)taskNames);
        this.libraryTasks.getItems().remove(0, this.libraryTasks.getItems().size());
        this.libraryTasks.setItems(data);
        final String text = "Select a task to see its parameters";
        this.taskDescription.setText(text);
    }
}
