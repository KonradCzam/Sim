// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.controllers.library;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.net.URL;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;
import javafx.scene.control.TextArea;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Configurable;
import javafx.fxml.Initializable;

@Configurable
public class SkillsAndStatsController implements Initializable
{
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
    
    public void initialize(final URL location, final ResourceBundle resources) {
        this.initializeLists();
    }
    
    private void initializeLists() {
        final List<String> allStats = new ArrayList<String>();
        allStats.addAll(this.lightStatsList);
        allStats.addAll(this.heavyStats);
        ObservableList data = FXCollections.observableArrayList((Collection)allStats);
        this.statsList.setItems(data);
        this.statsList.getSelectionModel().selectedItemProperty().addListener(this::lambda$initializeLists$0);
        data = FXCollections.observableArrayList((Collection)this.allSkills);
        this.skillsList.setItems(data);
        this.skillsList.getSelectionModel().selectedItemProperty().addListener(this::lambda$initializeLists$1);
    }
    
    private void skillSelected() {
    }
    
    private void statSelected() {
    }
}
