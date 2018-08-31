package com.example.Sim.screens.Gallery;

import com.example.Sim.Exceptions.ImageNotFound;
import com.example.Sim.FXML.DialogController;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.Utilities.FileUtility;
import com.example.Sim.Girls.GirlService;
import com.example.Sim.screens.Gallery.model.TableGirl;
import com.example.Sim.Utilities.ImageHandler;
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
public class GalleryController implements Initializable,DialogController {
    @FXML
    private AnchorPane content;
    @FXML
    private ImageView imgView;
    @FXML
    public Button button;
    @FXML
    public TableView girlTable;
    @FXML
    public CheckBox gifOnly;
    @FXML
    public ComboBox imgType;
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

        initializeTable();
        gifOnly.setDisable(true);

    }

    public void initializeTable(){
        ObservableList data = FXCollections.observableArrayList(girlService.getNormalTableGirls());
        data.addAll(FXCollections.observableArrayList(girlService.getRandomTableGirls()));
        girlTable.setItems(data);
        TableColumn tableColumn = (TableColumn)girlTable.getColumns().get(0);
        tableColumn.setCellValueFactory(new PropertyValueFactory("displayName"));
        tableColumn = (TableColumn)girlTable.getColumns().get(1);
        tableColumn.setCellValueFactory(new PropertyValueFactory("folder"));

        girlTable.getSelectionModel().selectFirst();
    }
    public void setComboboxItems(String path){
        ObservableList data = FXCollections.observableArrayList(fileUtility.checkGirlTypes(path));
        imgType.setItems(data);
        imgType.getSelectionModel().selectFirst();
    }
    public void tableRowSelected(){

        rowId = girlTable.getSelectionModel().getFocusedIndex();
        TableGirl selectedTableGirl = (TableGirl)girlTable.getSelectionModel().getSelectedItem();
        setComboboxItems(selectedTableGirl.getPath());
        if(selectedTableGirl.getFolder().equals("Present")){
          try{
              imageHandler.setImage(imgView,selectedTableGirl.getPath(),null,gifOnly.isSelected());
          }catch (ImageNotFound e){
              Alert alert = new Alert(Alert.AlertType.CONFIRMATION, e.getTextMessage());
              alert.showAndWait();
          }
        }

    }

    public void categorySelected(){
        if(imgType.getSelectionModel().getSelectedItem() != null){
            gifOnly.setSelected(false);
            TableGirl selectedTableGirl = (TableGirl)girlTable.getSelectionModel().getSelectedItem();
            String category= imgType.getSelectionModel().getSelectedItem().toString();
            gifOnly.setDisable(!fileUtility.checkIfGifAvailable(selectedTableGirl.getPath(),category));
        }

    }
    public void buttonPress(){
        TableGirl selectedTableGirl = (TableGirl)girlTable.getSelectionModel().getSelectedItem();
        try{
            String category= imgType.getSelectionModel().getSelectedItem().toString();
            imageHandler.setImage(imgView,selectedTableGirl.getPath(),category,gifOnly.isSelected());
        }catch (ImageNotFound e){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, e.getTextMessage());
            alert.showAndWait();
        }

    }
    public void goToBrothel(){

        dialog.close();
        screens.brothelDialog().show();

    }



    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }
}
