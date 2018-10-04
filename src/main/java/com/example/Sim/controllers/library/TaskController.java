package com.example.Sim.controllers.library;

import com.example.Sim.Model.Jobs.Task;
import com.example.Sim.Services.JobService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Configurable
public class TaskController implements Initializable {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeLists();
        String text2 =
                "Type decides whether there is a single node or multiple nodes in the end turn report.\n" +
                        "Money coefficient is a multiplyer used in calculation of money earned for the job.\n" +
                        "Default pic category a picture from this category is returned if there is no picture found that matches the job more \n" +
                        "Threshold type decides which stat is used to check if npc will refuse the task.\n" +
                        "The higher the threshold value the more obedient or morally flexible the npc must be to finish the task.\n" +
                        "Note the value does not represent the exact value, its a simplification, there are more calculations involved.";
        taskNotes.setText(text2);
    }


    private void initializeLists() {
        List<String> jobNames = new ArrayList<>();
        jobService.getJobList().forEach(job -> jobNames.add(job.getName()));
        ObservableList data = FXCollections.observableArrayList(jobNames);
        libraryJobs.setItems(data);
        libraryJobs.getSelectionModel().selectedItemProperty().addListener((obs) -> {
            jobSelected();
        });

        List<String> taskNames = new ArrayList<>();
        jobService.getJobList().get(0).getTasks().forEach(job -> taskNames.add(job.getName()));
        data = FXCollections.observableArrayList(taskNames);
        libraryTasks.setItems(data);
        libraryTasks.getSelectionModel().selectedItemProperty().addListener((obs) -> {
            taskSelected();
        });
    }

    private void taskSelected() {
        String taskName = (String) libraryTasks.getSelectionModel().getSelectedItem();
        Task task = jobService.getAllTasks().get(taskName);
        if (task != null) {
            String text = "Name: " + task.getName() + "\n" +
                    "Description: " + task.getDescription() + "\n" +
                    "Type: " + task.getType() + "\n" +
                    "Tiring: " + task.getTiring() + "\n" +
                    "Money Coefficient: " + task.getMoneyCoefficient().toString() + "\n" +
                    "Experience gain: " + task.getExpGain().toString() + "\n" +
                    "Default pic category: " + task.getDefaultCat() + "\n" +
                    "Threshold type: " + task.getType() + "\n" +
                    "Threshold value: " + task.getThreshold();

            taskDescription.setText(text);

        }
    }

    private void jobSelected() {
        List<String> taskNames = new ArrayList<>();
        Integer index = libraryJobs.getSelectionModel().getSelectedIndex();
        jobService.getJobList().get(index).getTasks().forEach(job -> taskNames.add(job.getName()));
        ObservableList data = FXCollections.observableArrayList(taskNames);

        libraryTasks.getItems().remove(0, libraryTasks.getItems().size());
        libraryTasks.setItems(data);
        String text = "Select a task to see its parameters";

        taskDescription.setText(text);

    }


}
