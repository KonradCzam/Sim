package com.example.Sim.controllers;

import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.FXML.DialogController;
import com.example.Sim.FXML.FXMLDialog;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ResourceBundle;

@Service
public class UpgradesController implements Initializable, DialogController {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void goToHub() {
        dialog.close();
        screens.hubDialog().show();
    }
}
