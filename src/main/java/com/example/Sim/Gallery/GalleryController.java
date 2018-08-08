package com.example.Sim.Gallery;

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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
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

    private Integer rowId = 0;

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
        girlService.createGirls();

        ObservableList data = FXCollections.observableArrayList(girlService.getNormalTableGirls());
        data.addAll(FXCollections.observableArrayList(girlService.getRandomTableGirls()));
        girlTable.setItems(data);
        TableColumn tableColumn = (TableColumn)girlTable.getColumns().get(0);
        tableColumn.setCellValueFactory(new PropertyValueFactory("displayName"));
        tableColumn = (TableColumn)girlTable.getColumns().get(1);
        tableColumn.setCellValueFactory(new PropertyValueFactory("folder"));

        girlTable.getSelectionModel().selectFirst();
    }
    public void tableRowSelected(){

        rowId = girlTable.getSelectionModel().getFocusedIndex();
        TableGirl selectedTableGirl = (TableGirl)girlTable.getSelectionModel().getSelectedItem();
        if(selectedTableGirl.getFolder().equals("Present"))
            imageHandler.setImage(imgView,selectedTableGirl.getPath(),null);
    }

    public void buttonPress(){
        TableGirl selectedTableGirl = (TableGirl)girlTable.getSelectionModel().getSelectedItem();
        imageHandler.setImage(imgView,selectedTableGirl.getPath(),null);

    }

    public void removeFolder(){
        imageHandler.setImage(imgView,null,null);
        TableGirl selectedTableGirl = (TableGirl)girlTable.getSelectionModel().getSelectedItem();
        try {
            fileUtility.deleteDirectoryStream(selectedTableGirl.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        initialize(null,null);
    }
    public void removeBoth(){
        removeFolder();
        removeGirlFile();
    }
    public void removeGirlFile(){

        TableGirl selectedTableGirl = (TableGirl)girlTable.getSelectionModel().getSelectedItem();
        fileUtility.removeFile(selectedTableGirl.getPath()+".girlsx");

        initialize(null,null);

    }

    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }
}
