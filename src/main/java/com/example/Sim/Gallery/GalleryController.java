package com.example.Sim.Gallery;

import com.example.Sim.FXML.DialogController;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.FileUtilities.FileUtility;
import com.example.Sim.Model.GirlReader;
import com.example.Sim.Model.TableGirl;
import com.example.Sim.Model.GirlsLists;
import com.example.Sim.FileUtilities.ImageHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URL;
import java.util.ResourceBundle;

@Service
public class GalleryController implements Initializable,DialogController {
    @FXML
    private ImageView imgView;
    @FXML
    public Button button;
    @FXML
    public TableView girlTable;

    private ScreensConfiguration screens;
    private FXMLDialog dialog;
    private GirlsLists girlsLists;
    private Integer rowId = 0;
    @Resource
    FileUtility fileUtility;
    @Resource
    ImageHandler imageHandler;
    GirlReader girlReader = new GirlReader();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        girlReader.createGirl("s");
        girlsLists = fileUtility.getGirlList();

        imageHandler.setImage(imgView,girlsLists.getNormalTableGirls().get(rowId).getName(),null);

        ObservableList data = FXCollections.observableArrayList(girlsLists.getNormalTableGirls());
        data.addAll(FXCollections.observableArrayList(girlsLists.getRandomTableGirls()));
        girlTable.setItems(data);
        TableColumn tableColumn = (TableColumn)girlTable.getColumns().get(0);
        tableColumn.setCellValueFactory(new PropertyValueFactory("name"));
        tableColumn = (TableColumn)girlTable.getColumns().get(1);
        tableColumn.setCellValueFactory(new PropertyValueFactory("folder"));

    }
    public void tableRowSelected(){

        rowId = girlTable.getSelectionModel().getFocusedIndex();
        TableGirl selectedTableGirl = (TableGirl)girlTable.getSelectionModel().getSelectedItem();
        if(selectedTableGirl.getFolder().equals("Present"))
            imageHandler.setImage(imgView,girlsLists.getNormalTableGirls().get(rowId).getName(),null);
    }
    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }
    public GalleryController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    public void buttonPress(){
        imageHandler.setImage(imgView,girlsLists.getNormalTableGirls().get(rowId).getName(),null);

    }

    public void removeFolder(){
        TableGirl selectedTableGirl = (TableGirl)girlTable.getSelectionModel().getSelectedItem();
        fileUtility.removeFile(selectedTableGirl.getName());
    }
    public void removeBoth(){
        removeFolder();
        removeGirlFile();
    }
    public void removeGirlFile(){
        TableGirl selectedTableGirl = (TableGirl)girlTable.getSelectionModel().getSelectedItem();
        fileUtility.removeFile(selectedTableGirl.getName());
    }
}
