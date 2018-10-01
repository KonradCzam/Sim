package com.example.Sim.controllers;

import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.Exceptions.ImageNotFound;
import com.example.Sim.FXML.DialogController;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Services.NpcService;
import com.example.Sim.Utilities.FileUtility;
import com.example.Sim.Utilities.ImageHandler;
import com.example.Sim.controllers.Gallery.model.TableNpc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URL;
import java.util.ResourceBundle;

@Service
public class GalleryController implements Initializable, DialogController {
    @FXML
    public Button button;
    @FXML
    public TableView npcTable;
    @FXML
    public CheckBox gifOnly;
    @FXML
    public ComboBox imgType;
    @Resource
    FileUtility fileUtility;
    @Resource
    ImageHandler imageHandler;
    @Resource
    NpcService npcService;
    @FXML
    private AnchorPane content;
    @FXML
    private ImageView imgView;
    private ScreensConfiguration screens;
    private FXMLDialog dialog;
    private Integer rowId = 0;


    public GalleryController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        npcService.createNpcs();

        initializeTable();
        gifOnly.setDisable(true);


    }

    public void initializeTable() {
        ObservableList data = FXCollections.observableArrayList(npcService.getNormalTableNpcs());

        npcTable.setItems(data);
        TableColumn tableColumn = (TableColumn) npcTable.getColumns().get(0);
        tableColumn.setCellValueFactory(new PropertyValueFactory("displayName"));
        tableColumn = (TableColumn) npcTable.getColumns().get(1);
        tableColumn.setCellValueFactory(new PropertyValueFactory("folder"));

        npcTable.getSelectionModel().selectFirst();
    }

    public void setComboboxItems(String path) {
        ObservableList data = FXCollections.observableArrayList(fileUtility.checkNpcTypes(path));
        imgType.setItems(data);
        imgType.getSelectionModel().selectFirst();
    }

    public void tableRowSelected() {

        rowId = npcTable.getSelectionModel().getFocusedIndex();
        TableNpc selectedTableNpc = (TableNpc) npcTable.getSelectionModel().getSelectedItem();
        setComboboxItems(selectedTableNpc.getPath());
        if (selectedTableNpc.getFolder().equals("Present")) {
            try {
                imageHandler.setImage(imgView, selectedTableNpc.getPath(), null, gifOnly.isSelected());
            } catch (ImageNotFound e) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, e.getTextMessage());
                alert.showAndWait();
            }
        }

    }

    public void categorySelected() {
        if (imgType.getSelectionModel().getSelectedItem() != null) {
            gifOnly.setSelected(false);
            TableNpc selectedTableNpc = (TableNpc) npcTable.getSelectionModel().getSelectedItem();
            String category = imgType.getSelectionModel().getSelectedItem().toString();
            gifOnly.setDisable(!fileUtility.checkIfGifAvailable(selectedTableNpc.getPath(), category));
        }

    }

    public void buttonPress() {
        TableNpc selectedTableNpc = (TableNpc) npcTable.getSelectionModel().getSelectedItem();
        try {
            String category = imgType.getSelectionModel().getSelectedItem().toString();
            imageHandler.setImage(imgView, selectedTableNpc.getPath(), category, gifOnly.isSelected());
        } catch (ImageNotFound e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, e.getTextMessage());
            alert.showAndWait();
        }

    }

    public void goToBrothel() {

        dialog.close();
        screens.hubDialog().show();

    }


    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }
}
