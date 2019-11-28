// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.controllers.library;

import javafx.beans.Observable;
import java.io.IOException;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.collections.ObservableList;
import java.util.Collection;
import javafx.collections.FXCollections;
import java.util.ResourceBundle;
import java.net.URL;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Config.ScreensConfiguration;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.springframework.stereotype.Service;
import com.example.Sim.FXML.DialogController;
import javafx.fxml.Initializable;

@Service
public class LibraryController implements Initializable, DialogController
{
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
    private ScreensConfiguration screens;
    private FXMLDialog dialog;
    
    public void handleButtonAction() {
        this.dialog.close();
        this.screens.hubDialog().show();
    }
    
    public LibraryController(final ScreensConfiguration screens) {
        this.screens = screens;
    }
    
    public void setDialog(final FXMLDialog dialog) {
        this.dialog = dialog;
    }
    
    public void initialize(final URL location, final ResourceBundle resources) {
        this.changeTargetPane("library/new.fxml");
        final ObservableList data = FXCollections.observableArrayList((Collection)this.topics);
        this.topicTable.setItems(data);
        this.topicTable.getSelectionModel().selectedItemProperty().addListener(this::lambda$initialize$0);
    }
    
    private void topicSelected() {
        String topicName = (String)this.topicTable.getSelectionModel().getSelectedItem();
        topicName = topicName.toLowerCase();
        topicName = topicName.replaceAll("\\s+", "");
        this.changeTargetPane("library/" + topicName + ".fxml");
    }
    
    public void changeTargetPane(final String fxml) {
        Node root = null;
        final FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getClassLoader().getResource(fxml));
        fxmlLoader.setControllerFactory(this.applicationContext::getBean);
        try {
            root = (Node)fxmlLoader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.targetPane.getChildren().setAll((Object[])new Node[] { root });
    }
}
