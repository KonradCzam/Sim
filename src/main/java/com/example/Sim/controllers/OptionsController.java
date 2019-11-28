// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.controllers;

import java.util.ResourceBundle;
import java.net.URL;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Config.ScreensConfiguration;
import javax.annotation.Resource;
import com.example.Sim.Services.NpcService;
import org.springframework.stereotype.Service;
import com.example.Sim.FXML.DialogController;
import javafx.fxml.Initializable;

@Service
public class OptionsController implements Initializable, DialogController
{
    @Resource
    NpcService npcService;
    private ScreensConfiguration screens;
    private FXMLDialog dialog;
    
    public OptionsController(final ScreensConfiguration screens) {
        this.screens = screens;
    }
    
    public void setDialog(final FXMLDialog dialog) {
        this.dialog = dialog;
    }
    
    public void initialize(final URL location, final ResourceBundle resources) {
    }
    
    public void goToStart() {
        this.dialog.close();
        this.screens.startDialog().show();
    }
}
