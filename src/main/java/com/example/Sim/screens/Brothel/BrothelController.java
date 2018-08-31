package com.example.Sim.screens.Brothel;

import com.example.Sim.screens.Brothel.model.BrothelTableGirl;
import com.example.Sim.Exceptions.ImageNotFound;
import com.example.Sim.FXML.DialogController;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.Model.Girl;
import com.example.Sim.Utilities.FileUtility;
import com.example.Sim.Girls.GirlService;
import com.example.Sim.Utilities.ImageHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Service
public class BrothelController  implements Initializable,DialogController {
    @FXML
    private AnchorPane brothelAnchor;
    @FXML
    public TableView brothelTable;
    @FXML
    public ImageView brothelImage;
    private ScreensConfiguration screens;
    private FXMLDialog dialog;


    @Resource
    FileUtility fileUtility;
    @Resource
    ImageHandler imageHandler;
    @Autowired
    GirlService girlService;
    @Resource
    BrothelService brothelService;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    girlService.createGirls();

    initiateTable();
    }
    public void initiateTable(){
        addGirlsToBrothel();
        brothelTable.getSelectionModel().selectedItemProperty().addListener((obs) -> {
            tableRowSelected();
        });
    }
    public void updateTable(List<BrothelTableGirl> list ){
        ObservableList data = FXCollections.observableArrayList(list);
        brothelTable.setItems(data);
        TableColumn tableColumn = (TableColumn)brothelTable.getColumns().get(0);
        tableColumn.setCellValueFactory(new PropertyValueFactory("name"));
        tableColumn = (TableColumn)brothelTable.getColumns().get(1);
        tableColumn.setCellValueFactory(new PropertyValueFactory("tiredness"));
        tableColumn = (TableColumn)brothelTable.getColumns().get(2);
        tableColumn.setCellValueFactory(new PropertyValueFactory("dayShift"));
        tableColumn = (TableColumn)brothelTable.getColumns().get(3);
        tableColumn.setCellValueFactory(new PropertyValueFactory("nightShift"));
        tableColumn = (TableColumn)brothelTable.getColumns().get(4);
        tableColumn.setCellValueFactory(new PropertyValueFactory("avgSex"));

        brothelTable.getSelectionModel().selectFirst();
    }
    public BrothelController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    public void tableRowSelected(){

        BrothelTableGirl selectedBrothelGirl = (BrothelTableGirl)brothelTable.getSelectionModel().getSelectedItem();


            try{
                imageHandler.setImage(brothelImage,selectedBrothelGirl.getPath(),"profile",false);
            }catch (ImageNotFound e){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, e.getTextMessage());
                alert.showAndWait();
            }


    }
    public void addGirlsToBrothel(){
        List<Girl> girlsList= girlService.getNormalGirls();
        for (Girl girl:girlsList) {
            brothelService.addGirlToBrothel(girl);
        }
        updateTable(brothelService.getBrothelTableGirls());
    }
    public void goToGallery(){
        dialog.close();
        screens.loginDialog().show();
    }


    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }
}

