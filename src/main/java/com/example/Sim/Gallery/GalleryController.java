package com.example.Sim.Gallery;

import com.example.Sim.FXML.DialogController;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.Utilities.FileUtility;
import com.example.Sim.Girls.GirlService;
import com.example.Sim.Model.TableGirl;
import com.example.Sim.Utilities.ImageHandler;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArrayBase;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

@Service
public class GalleryController implements Initializable,DialogController {


    @FXML
    public Button button;

    @FXML
    public TableView girlTable;

    @FXML
    public TableView toRemoveTable;

    @FXML
    public Pane buttonPane;

    @FXML
    public Label removeTableText;

    @FXML
    public Pane confirmationButtonPane;

    @FXML
    public Button directoryButton;

    private ScreensConfiguration screens;
    private FXMLDialog dialog;
    DirectoryChooser directoryChooser;
    private String directory;

    @Resource
    FileUtility fileUtility;
    @Resource
    ImageHandler imageHandler;
    @Resource
    GirlService girlService;



    public GalleryController(ScreensConfiguration screens) {
        this.screens = screens;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        directoryChooser = new DirectoryChooser();

        TableColumn tableColumn = (TableColumn)girlTable.getColumns().get(0);
        tableColumn.setCellValueFactory(new PropertyValueFactory("displayName"));
        tableColumn = (TableColumn)girlTable.getColumns().get(1);
        tableColumn.setCellValueFactory(new PropertyValueFactory("folder"));

        tableColumn = (TableColumn)toRemoveTable.getColumns().get(0);
        tableColumn.setCellValueFactory(new PropertyValueFactory("displayName"));
        tableColumn = (TableColumn)toRemoveTable.getColumns().get(1);
        tableColumn.setCellValueFactory(new PropertyValueFactory("folder"));

        toRemoveTable.setVisible(false);
        girlTable.getSelectionModel().selectFirst();
    }

    public void removeFolder(){
        TableGirl selectedTableGirl = (TableGirl)girlTable.getSelectionModel().getSelectedItem();
        try {
            fileUtility.deleteDirectoryStream(selectedTableGirl.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        refresh(directory);
    }
    public void removeBoth(){
        removeFolder();
        removeGirlFile();
    }
    public void removeGirlFile(){
        TableGirl selectedTableGirl = (TableGirl)girlTable.getSelectionModel().getSelectedItem();
        if(selectedTableGirl.getPath().equals(selectedTableGirl.getDisplayName())) {
            fileUtility.removeFile(selectedTableGirl.getPath() + ".girlsx");
        }else{
            fileUtility.removeFile(selectedTableGirl.getDisplayName() + ".rgirlsx");
        }
        refresh(directory);

    }
    public void autoCleanUp(){
        setAutoCleanMode(true);

        Predicate<TableGirl> equalsPresent = i -> (i.getFolder().equals("Present"));
        ObservableList data = FXCollections.observableArrayList(girlService.getNormalTableGirls());
        data.addAll(FXCollections.observableArrayList(girlService.getRandomTableGirls()));
        data.removeIf(equalsPresent);

        toRemoveTable.setItems(data);
        refresh(directory);
    }
    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }
    public void refresh(String directory) {
        girlService.createGirls(directory);

        ObservableList data = FXCollections.observableArrayList(girlService.getNormalTableGirls());
        data.addAll(FXCollections.observableArrayList(girlService.getRandomTableGirls()));
        girlTable.setItems(data);
    }
    private void setAutoCleanMode(Boolean bool) {
        removeTableText.setVisible(bool);
        toRemoveTable.setVisible(bool);
        buttonPane.setDisable(bool);
        confirmationButtonPane.setVisible(bool);
        girlTable.setDisable(bool);
        directoryButton.setDisable(bool);
    }
    public void confirmAutoCleanUp() {
        toRemoveTable.getItems().forEach(( object)-> {
            TableGirl girl = (TableGirl)object;
            if (girl.getPath().equals(girl.getDisplayName())) {
                fileUtility.removeFile(girl.getPath() + ".girlsx");
            } else {
                fileUtility.removeFile(girl.getDisplayName() + ".rgirlsx");
            }
        });
        refresh(directory);
        setAutoCleanMode(false);

    }

    public void cancelAutoCleanUp() {
        setAutoCleanMode(false);
    }
    public void selectDirectory(){
        File file  =  directoryChooser.showDialog(screens.getPrimaryStage());
        if(file != null) {
            directory = file.getAbsolutePath();

            directory += "\\";
            refresh(directory);
            if (girlTable.getItems().size() > 0)
                buttonPane.setDisable(false);
            else
                throwAlarm("No .girlsx or .rgirlsx files found in the specified folder: "+directory,Alert.AlertType.WARNING);
        }else
            throwAlarm ("Cant open directory, dunno why",Alert.AlertType.WARNING);
    }
    public void throwAlarm(String text, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType, text, ButtonType.OK);

        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {

        }
    }
}
