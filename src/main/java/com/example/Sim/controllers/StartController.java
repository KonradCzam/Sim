package com.example.Sim.controllers;

import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.Exceptions.ImageNotFound;
import com.example.Sim.FXML.DialogController;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Model.Raport.EndTurnRapport;
import com.example.Sim.Model.Raport.NpcRoot;
import com.example.Sim.Model.Raport.SingleEventRoot;
import com.example.Sim.Services.EndTurnService;
import com.example.Sim.Services.NpcService;
import com.example.Sim.Utilities.ImageHandler;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.WindowEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Service
public class StartController implements Initializable, DialogController {

    @FXML
    private Button newGameButton;
    @FXML
    private Button optionsButton;
    @FXML
    private Button loadGameButon;
    @FXML
    private Button quitButton;

    @Resource
    NpcService npcService;
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
    public void startGame(){
        npcService.createNpcs();
        npcService.shuffleHirable();
        dialog.close();
        screens.hubDialog().show();
    }
    public void loadGame(){
        dialog.close();
        screens.saveLoadDialog().show();
    }
    public void quit(){
        Runtime.getRuntime().exit(0);
    }
    public void goToOptions(){
        dialog.close();
        screens.optionsDialog().show();
    }
}
