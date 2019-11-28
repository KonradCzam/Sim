// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.controllers;

import java.util.ResourceBundle;
import java.net.URL;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Config.ScreensConfiguration;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javax.annotation.Resource;
import com.example.Sim.Services.NpcService;
import org.springframework.stereotype.Service;
import com.example.Sim.FXML.DialogController;
import javafx.fxml.Initializable;

@Service
public class StartController implements Initializable, DialogController
{
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
    
    public StartController(final ScreensConfiguration screens) {
        this.screens = screens;
    }
    
    public void setDialog(final FXMLDialog dialog) {
        this.dialog = dialog;
    }
    
    public void initialize(final URL location, final ResourceBundle resources) {
    }
    
    public void startGame() {
        this.npcService.createNpcs();
        this.npcService.shuffleHirable();
        this.dialog.close();
        this.screens.hubDialog().show();
    }
    
    public void loadGame() {
        this.dialog.close();
        this.screens.saveLoadDialog().show();
    }
    
    public void quit() {
        Runtime.getRuntime().exit(0);
    }
    
    public void goToOptions() {
        this.dialog.close();
        this.screens.interactionDialog().show();
    }
    
    public void goToAch() {
        this.dialog.close();
        this.screens.achievementsDialog().show();
    }
}
