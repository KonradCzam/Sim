package com.example.Sim.controllers;

import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.FXML.DialogController;
import com.example.Sim.FXML.FXMLDialog;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ResourceBundle;

@Service
@NoArgsConstructor
public class UpgradesController {

    @FXML
    TableView upgradesTableView;
    @FXML
    TextArea upgradesTextArea;

    private ScreensConfiguration screens;
    private FXMLDialog dialog;


    public UpgradesController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }


    public void goToHub() {
        screens.activate("hub");
    }
}
