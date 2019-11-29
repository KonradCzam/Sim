package com.example.Sim.controllers.library;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Configurable
public class SkillsAndStatsController implements Initializable {

    @FXML
    private ListView skillsList;
    @FXML
    private ListView statsList;
    @FXML
    private TextArea skillsAndStatsDescription;

    @Value("#{'${stats.status}'.split(',')}")
    private List<String> lightStatsList;
    @Value("#{'${stats.important}'.split(',')}")
    private List<String> heavyStats;
    @Value("#{'${skills.all}'.split(',')}")
    private List<String> allSkills;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeLists();

    }
    private void initializeLists() {
        List<String> allStats = new ArrayList<>();
        allStats.addAll(lightStatsList);
        allStats.addAll(heavyStats);
        ObservableList data = FXCollections.observableArrayList(allStats);
        statsList.setItems(data);
        statsList.getSelectionModel().selectedItemProperty().addListener((obs) -> {
            statSelected();
        });


        data = FXCollections.observableArrayList(allSkills);
        skillsList.setItems(data);
        skillsList.getSelectionModel().selectedItemProperty().addListener((obs) -> {
            skillSelected();
        });
    }

    private void skillSelected() {

    }

    private void statSelected() {
    }


}
