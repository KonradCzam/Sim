package com.example.Sim.controllers;

import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.FXML.DialogController;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Services.NpcService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URL;
import java.util.ResourceBundle;

@Service
public class StartController implements Initializable, DialogController {

    @Resource
    NpcService npcService;
    @FXML
    private Button newGameButton;
    @FXML
    private Button optionsButton;
    @FXML
    private Button loadGameButon;
    @FXML
    private Button quitButton;
    private ScreensConfiguration screens;
    private FXMLDialog dialog;


    public StartController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void startGame() {
        npcService.createNpcs();
        npcService.shuffleHirable();
        dialog.close();
        screens.hubDialog().show();
    }

    public void loadGame() {
        dialog.close();
        screens.saveLoadDialog().show();
    }

    public void quit() {
        Runtime.getRuntime().exit(0);
    }

    public void goToOptions() {
        dialog.close();
        screens.optionsDialog().show();
    }
}
