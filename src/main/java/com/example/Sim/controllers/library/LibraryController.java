package com.example.Sim.controllers.library;

import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.FXML.DialogController;
import com.example.Sim.FXML.FXMLDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Service
public class LibraryController implements Initializable, DialogController {

    @FXML
    private AnchorPane targetPane;
    @FXML
    private Button libraryButton;
    @FXML
    private ListView topicTable;
    @Autowired
    ApplicationContext applicationContext;
    @Value("#{'${library.all}'.split(',')}")
    List<String> topics;


    private List<String> allCategories;
    public void handleButtonAction() {
        dialog.close();
        screens.hubDialog().show();
    }

    private ScreensConfiguration screens;
    private FXMLDialog dialog;



    public LibraryController(ScreensConfiguration screens) {
        this.screens = screens;
    }
    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        changeTargetPane("library/new.fxml");
        ObservableList data = FXCollections.observableArrayList(topics);
        topicTable.setItems(data);
        topicTable.getSelectionModel().selectedItemProperty().addListener((obs) -> {
            topicSelected();
        });

    }

    private void topicSelected() {
        String topicName = (String)topicTable.getSelectionModel().getSelectedItem();
        topicName = topicName.toLowerCase();
        topicName = topicName.replaceAll("\\s+","");

        changeTargetPane("library/" + topicName+".fxml");
    }

    public void changeTargetPane(String fxml){
        Node root = null;
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getClassLoader().getResource(fxml));
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        try {
            root  = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        targetPane.getChildren().setAll(root);
    }


}
