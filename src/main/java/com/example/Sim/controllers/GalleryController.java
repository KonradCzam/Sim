// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.controllers;

import com.example.Sim.Exceptions.ImageNotFound;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import com.example.Sim.controllers.Gallery.model.TableNpc;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import java.util.Collection;
import javafx.collections.FXCollections;
import java.util.ResourceBundle;
import java.net.URL;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Config.ScreensConfiguration;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import com.example.Sim.Services.NpcService;
import com.example.Sim.Utilities.ImageHandler;
import javax.annotation.Resource;
import com.example.Sim.Utilities.FileUtility;
import javafx.scene.control.ComboBox;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.springframework.stereotype.Service;
import com.example.Sim.FXML.DialogController;
import javafx.fxml.Initializable;

@Service
public class GalleryController implements Initializable, DialogController
{
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
    private Integer rowId;
    
    public GalleryController(final ScreensConfiguration screens) {
        this.rowId = 0;
        this.screens = screens;
    }
    
    public void initialize(final URL location, final ResourceBundle resources) {
        this.npcService.createNpcs();
        this.initializeTable();
        this.gifOnly.setDisable(true);
    }
    
    public void initializeTable() {
        final ObservableList data = FXCollections.observableArrayList((Collection)this.npcService.getNormalTableNpcs());
        this.npcTable.setItems(data);
        TableColumn tableColumn = (TableColumn)this.npcTable.getColumns().get(0);
        tableColumn.setCellValueFactory((Callback)new PropertyValueFactory("displayName"));
        tableColumn = (TableColumn)this.npcTable.getColumns().get(1);
        tableColumn.setCellValueFactory((Callback)new PropertyValueFactory("folder"));
        this.npcTable.getSelectionModel().selectFirst();
    }
    
    public void setComboboxItems(final String path) {
        final ObservableList data = FXCollections.observableArrayList((Collection)this.fileUtility.checkNpcTypes(path));
        this.imgType.setItems(data);
        this.imgType.getSelectionModel().selectFirst();
    }
    
    public void tableRowSelected() {
        this.rowId = this.npcTable.getSelectionModel().getFocusedIndex();
        final TableNpc selectedTableNpc = (TableNpc)this.npcTable.getSelectionModel().getSelectedItem();
        this.setComboboxItems(selectedTableNpc.getPath());
        if (selectedTableNpc.getFolder().equals("Present")) {
            try {
                this.imageHandler.setImage(this.imgView, selectedTableNpc.getPath(), (String)null, this.gifOnly.isSelected());
            }
            catch (ImageNotFound e) {
                final Alert alert = new Alert(Alert.AlertType.CONFIRMATION, e.getTextMessage(), new ButtonType[0]);
                alert.showAndWait();
            }
        }
    }
    
    public void categorySelected() {
        if (this.imgType.getSelectionModel().getSelectedItem() != null) {
            this.gifOnly.setSelected(false);
            final TableNpc selectedTableNpc = (TableNpc)this.npcTable.getSelectionModel().getSelectedItem();
            final String category = this.imgType.getSelectionModel().getSelectedItem().toString();
            this.gifOnly.setDisable(!this.fileUtility.checkIfGifAvailable(selectedTableNpc.getPath(), category));
        }
    }
    
    public void buttonPress() {
        final TableNpc selectedTableNpc = (TableNpc)this.npcTable.getSelectionModel().getSelectedItem();
        try {
            final String category = this.imgType.getSelectionModel().getSelectedItem().toString();
            this.imageHandler.setImage(this.imgView, selectedTableNpc.getPath(), category, this.gifOnly.isSelected());
        }
        catch (ImageNotFound e) {
            final Alert alert = new Alert(Alert.AlertType.CONFIRMATION, e.getTextMessage(), new ButtonType[0]);
            alert.showAndWait();
        }
    }
    
    public void goToBrothel() {
        this.dialog.close();
        this.screens.hubDialog().show();
    }
    
    public void setDialog(final FXMLDialog dialog) {
        this.dialog = dialog;
    }
}
