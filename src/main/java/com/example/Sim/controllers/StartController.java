package com.example.Sim.controllers;

import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.Config.SimConfig;
import com.example.Sim.Services.NpcService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@NoArgsConstructor
public class StartController {

    @Resource
    NpcService npcService;
    @Resource
    Pane hubPane;
    @FXML
    private Button newGameButton;
    @FXML
    private Button optionsButton;
    @FXML
    private Button loadGameButon;
    @FXML
    private Button quitButton;
    private ScreensConfiguration screens;


    public StartController(ScreensConfiguration sc) {
    this.screens = sc;
    }
    public void startGame() {
        screens.activate("hub");
    }

    public void loadGame() {
        screens.activate("saveLoad");
    }

    public void quit() {
        Runtime.getRuntime().exit(0);
    }

    public void goToOptions() {
        screens.activate("interaction");
    }
    public void goToAch(){
        screens.activate("scriptGenerator");
    }
}
