package com.example.Sim.Config;

import com.example.Sim.Exceptions.ImageNotFound;
import com.example.Sim.FXML.DialogController;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.Utilities.FileUtility;
import com.example.Sim.Girls.GirlService;
import com.example.Sim.Model.TableGirl;
import com.example.Sim.Utilities.ImageHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Service
public class BrothelController  {
    @FXML
    private AnchorPane brothelAnchor;

    private ScreensConfiguration screens;
    private FXMLDialog dialog;


    @Resource
    FileUtility fileUtility;
    @Resource
    ImageHandler imageHandler;
    @Autowired
    GirlService girlService;

    public BrothelController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    public void goToGallery(){
        screens.loginDialog().show();
    }


    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }
}

