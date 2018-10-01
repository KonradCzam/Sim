package com.example.Sim.controllers;

import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.FXML.DialogController;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Services.NpcService;
import javafx.fxml.Initializable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URL;
import java.util.ResourceBundle;

@Service
public class OptionsController implements Initializable, DialogController {

    @Resource
    NpcService npcService;
    private ScreensConfiguration screens;
    private FXMLDialog dialog;


    public OptionsController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
