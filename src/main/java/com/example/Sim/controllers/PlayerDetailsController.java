// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.controllers;

import java.util.function.Consumer;
import com.example.Sim.Model.DetailsInterface;
import java.util.List;
import java.util.Map;
import javafx.scene.Node;
import javafx.scene.image.Image;
import com.example.Sim.Model.Item;
import com.example.Sim.Model.NPC.Skill;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.Collection;
import javafx.collections.FXCollections;
import com.example.Sim.Model.NPC.Stat;
import javafx.scene.control.TableColumn;
import java.util.ResourceBundle;
import java.net.URL;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.Utilities.ImageHandler;
import javafx.stage.WindowEvent;
import javax.annotation.Resource;
import com.example.Sim.Services.PlayerService;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import org.springframework.stereotype.Service;
import com.example.Sim.FXML.DialogController;
import javafx.fxml.Initializable;

@Service
public class PlayerDetailsController implements Initializable, DialogController
{
    double orgSceneX;
    double orgSceneY;
    double orgTranslateX;
    double orgTranslateY;
    EventHandler<MouseEvent> onMouseReleasedEventHanlder;
    EventHandler<MouseEvent> onMousePressedEventHandler;
    EventHandler<MouseEvent> onMouseDraggedEventHandler;
    @FXML
    private GridPane playerEqGrid;
    @FXML
    private GridPane playerItemsGrid;
    @FXML
    private ImageView playerImage;
    @FXML
    private TableView playerSkillsTable;
    @FXML
    private TableView playerTraitsTable;
    @FXML
    private TableView playerStatsTable;
    @Resource
    private PlayerService playerService;
    EventHandler<WindowEvent> onShownEventHandler;
    @Resource
    private ImageHandler imageHandler;
    private ScreensConfiguration screens;
    private FXMLDialog dialog;
    
    public PlayerDetailsController(final ScreensConfiguration screens) {
        this.onMouseReleasedEventHanlder = (EventHandler)new PlayerDetailsController$1(this);
        this.onMousePressedEventHandler = (EventHandler)new PlayerDetailsController$2(this);
        this.onMouseDraggedEventHandler = (EventHandler)new PlayerDetailsController$3(this);
        this.onShownEventHandler = (EventHandler)new PlayerDetailsController$4(this);
        this.screens = screens;
    }
    
    public void initialize(final URL location, final ResourceBundle resources) {
        this.dialog.setOnShown(this.onShownEventHandler);
        this.initializeGrids();
    }
    
    public void refresh() {
        this.initializeTables();
    }
    
    public void initializeTables() {
        this.initializeSkillsTable();
        this.initializeStatsTable();
        this.initializeTraits();
    }
    
    private <T> void addTooltipToColumnCells(final TableColumn<Stat, T> column) {
    }
    
    private void initializeTraits() {
        final ObservableList data = FXCollections.observableArrayList((Collection)this.playerService.getPlayer().getTraits());
        this.playerTraitsTable.setItems(data);
        final TableColumn tableColumn = (TableColumn)this.playerTraitsTable.getColumns().get(0);
        tableColumn.setCellValueFactory((Callback)new PropertyValueFactory("name"));
    }
    
    private void initializeStatsTable() {
        final ObservableList data = FXCollections.observableArrayList((Collection)this.playerService.getPlayer().getStats().values());
        this.playerStatsTable.setItems(data);
        TableColumn tableColumn = (TableColumn)this.playerStatsTable.getColumns().get(0);
        tableColumn.setCellValueFactory((Callback)new PropertyValueFactory("name"));
        tableColumn = (TableColumn)this.playerStatsTable.getColumns().get(1);
        tableColumn.setCellValueFactory((Callback)new PropertyValueFactory("value"));
        final TableColumn<Stat, Double> tableCol = (TableColumn<Stat, Double>)new TableColumn("Progress");
        tableCol.setPrefWidth(100.0);
        tableCol.setCellValueFactory((Callback)new PropertyValueFactory("progress"));
        tableCol.setCellFactory(ProgressBarTableCell.forTableColumn());
        this.playerStatsTable.getColumns().addAll(new Object[] { tableCol });
        this.playerStatsTable.setEditable(true);
        this.setTooltips(this.playerStatsTable);
    }
    
    private void initializeSkillsTable() {
        final ObservableList data = FXCollections.observableArrayList((Collection)this.playerService.getPlayer().getSkills().values());
        this.playerSkillsTable.setItems(data);
        TableColumn tableColumn = (TableColumn)this.playerSkillsTable.getColumns().get(0);
        tableColumn.setSortType(TableColumn.SortType.ASCENDING);
        tableColumn.setCellValueFactory((Callback)new PropertyValueFactory("name"));
        tableColumn = (TableColumn)this.playerSkillsTable.getColumns().get(1);
        tableColumn.setCellValueFactory((Callback)new PropertyValueFactory("value"));
        final TableColumn<Skill, Double> tableCol = (TableColumn<Skill, Double>)new TableColumn("Progress");
        tableCol.setCellValueFactory((Callback)new PropertyValueFactory("progress"));
        tableCol.setCellFactory(ProgressBarTableCell.forTableColumn());
        tableCol.setPrefWidth(100.0);
        tableCol.setResizable(false);
        this.playerSkillsTable.getColumns().addAll(new Object[] { tableCol });
        this.playerSkillsTable.setEditable(true);
        this.setTooltips(this.playerSkillsTable);
    }
    
    public void initializeGrids() {
        this.initializeEqGrid();
        this.initializePlayerItemsGrid();
    }
    
    public void initializeEqGrid() {
        final Map<String, Item> inventory = (Map<String, Item>)this.playerService.getPlayer().getEquippedItems();
        Integer index = 0;
        for (int row = 0; row < 4; ++row) {
            for (int column = 0; column < 3; ++column) {
                if (index < inventory.size()) {
                    final String imagePath = inventory.get(index).getPath();
                    final Image image = new Image(PlayerDetailsController.class.getResourceAsStream(imagePath));
                    final ImageView imageView = new ImageView(image);
                    this.playerEqGrid.add((Node)imageView, column, row);
                    ++index;
                }
                else {
                    final String imagePath = "/UI/EqEmpty.png";
                    final Image image = new Image(PlayerDetailsController.class.getResourceAsStream(imagePath));
                    final ImageView imageView = new ImageView(image);
                    this.playerEqGrid.add((Node)imageView, column, row);
                }
            }
        }
    }
    
    public void initializePlayerItemsGrid() {
        final List<Item> inventory = (List<Item>)this.playerService.getPlayer().getInventory();
        Integer index = 0;
        for (int row = 0; row < 4; ++row) {
            for (int column = 0; column < 15; ++column) {
                if (index < inventory.size()) {
                    final String imagePath = inventory.get(index).getPath();
                    final Image image = new Image(PlayerDetailsController.class.getResourceAsStream(imagePath));
                    final ImageView imageView = new ImageView(image);
                    this.playerItemsGrid.add((Node)imageView, column, row);
                    ++index;
                }
                else {
                    final String imagePath = "/UI/EqEmpty.png";
                    final Image image = new Image(PlayerDetailsController.class.getResourceAsStream(imagePath));
                    final ImageView imageView = new ImageView(image);
                    this.playerItemsGrid.add((Node)imageView, column, row);
                }
            }
        }
    }
    
    private <T extends DetailsInterface> void setTooltips(final TableView tableView) {
    }
    
    public void setDetectors() {
        this.playerEqGrid.getChildren().stream().forEach(this::lambda$setDetectors$0);
        this.playerItemsGrid.getChildren().stream().forEach(this::lambda$setDetectors$1);
    }
    
    public void setDetector(final ImageView image) {
        image.setOnMouseDragReleased((EventHandler)new PlayerDetailsController$5(this));
    }
    
    public void setDragables() {
        this.playerEqGrid.getChildren().stream().forEach(this::lambda$setDragables$2);
        this.playerItemsGrid.getChildren().stream().forEach(this::lambda$setDragables$3);
    }
    
    public void setDragable(final ImageView image) {
        image.setOnDragDetected((EventHandler)new PlayerDetailsController$6(this, image));
        image.setOnMousePressed(this.onMousePressedEventHandler);
        image.setOnMouseDragged(this.onMouseDraggedEventHandler);
        image.setOnMouseReleased(this.onMouseReleasedEventHanlder);
    }
    
    public void replaceImages(final ImageView source, final ImageView target) {
        final ImageView temp = new ImageView();
        temp.setImage(target.getImage());
        target.setImage(source.getImage());
        source.setImage(temp.getImage());
    }
    
    public void setDialog(final FXMLDialog dialog) {
        this.dialog = dialog;
    }
    
    public void goToHub() {
        this.dialog.close();
        this.screens.hubDialog().show();
    }
}
