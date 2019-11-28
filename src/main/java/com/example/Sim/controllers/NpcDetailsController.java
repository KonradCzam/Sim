// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.controllers;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tooltip;
import java.util.TreeMap;
import java.util.Iterator;
import javafx.scene.control.Tab;
import com.example.Sim.Model.NPC.NPCTaskExpStats;
import java.util.Map;
import java.util.function.Consumer;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.image.Image;
import com.example.Sim.Model.Item;
import com.example.Sim.Model.DetailsInterface;
import com.example.Sim.Model.NPC.Skill;
import java.util.Collection;
import java.util.ArrayList;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import javafx.util.Callback;
import com.example.Sim.Model.NPC.Stat;
import javafx.scene.control.TableColumn;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import com.example.Sim.Exceptions.ImageNotFound;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import java.util.ResourceBundle;
import java.net.URL;
import javafx.stage.WindowEvent;
import com.example.Sim.Model.NPC.Npc;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Config.ScreensConfiguration;
import org.springframework.beans.factory.annotation.Value;
import com.example.Sim.Factors.HousingFactor;
import com.example.Sim.Services.DescriptionService;
import com.example.Sim.Utilities.ImageHandler;
import com.example.Sim.Model.Player;
import com.example.Sim.Services.PlayerService;
import javax.annotation.Resource;
import com.example.Sim.Services.NpcService;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import org.springframework.stereotype.Service;
import com.example.Sim.FXML.DialogController;
import javafx.fxml.Initializable;

@Service
public class NpcDetailsController implements Initializable, DialogController
{
    double orgSceneX;
    double orgSceneY;
    double orgTranslateX;
    double orgTranslateY;
    EventHandler<MouseEvent> onMouseReleasedEventHanlder;
    EventHandler<MouseEvent> onMousePressedEventHandler;
    EventHandler<MouseEvent> onMouseDraggedEventHandler;
    @FXML
    private GridPane EqGrid;
    @FXML
    private GridPane PlayerEqGrid;
    @FXML
    private GridPane npcEqGrid;
    @FXML
    private Label ownerLabel;
    @FXML
    private Label statsLabel;
    @FXML
    private Label skillsLabel;
    @FXML
    private ImageView detailsImage;
    @FXML
    private TableView skillsTable;
    @FXML
    private TableView traitsTable;
    @FXML
    private TableView statsTable;
    @FXML
    private ComboBox housingCombo;
    @FXML
    private TextArea descriptionText;
    @FXML
    private TabPane jobsTabsPane;
    private TableView jobsTable;
    @Resource
    private NpcService npcService;
    @Resource
    private PlayerService playerService;
    @Resource
    private Player player;
    @Resource
    private ImageHandler imageHandler;
    @Resource
    private DescriptionService descriptionService;
    @Resource
    HousingFactor housingFactor;
    @Value("#{'${factors.housing.names}'.split(',')}")
    String[] names;
    private ScreensConfiguration screens;
    private FXMLDialog dialog;
    private Npc currentNpc;
    EventHandler<WindowEvent> onShownEventHandler;
    
    public NpcDetailsController(final ScreensConfiguration screens) {
        this.onMouseReleasedEventHanlder = (EventHandler)new NpcDetailsController$1(this);
        this.onMousePressedEventHandler = (EventHandler)new NpcDetailsController$2(this);
        this.onMouseDraggedEventHandler = (EventHandler)new NpcDetailsController$3(this);
        this.onShownEventHandler = (EventHandler)new NpcDetailsController$4(this);
        this.screens = screens;
    }
    
    public void initialize(final URL location, final ResourceBundle resources) {
        this.currentNpc = this.npcService.getCurrentNpc();
        this.dialog.setOnShown(this.onShownEventHandler);
        this.player = this.playerService.getPlayer();
        this.initializeGrids();
        this.initializeTables();
        this.initializeFactors();
        this.refreshTables();
        this.createJobExpPage();
    }
    
    public void refresh() {
        this.currentNpc = this.npcService.getCurrentNpc();
        this.descriptionText.setText(this.currentNpc.getDesc() + this.descriptionService.genStatusDesc(this.currentNpc));
        this.ownerLabel.setText(this.currentNpc.getName() + "'s items");
        this.statsLabel.setText(this.currentNpc.getName() + "'s base skills");
        this.skillsLabel.setText(this.currentNpc.getName() + "'s base stats");
        try {
            this.imageHandler.setImage(this.detailsImage, this.currentNpc.getPath(), "profile", false);
        }
        catch (ImageNotFound e) {
            final Alert alert = new Alert(Alert.AlertType.CONFIRMATION, e.getTextMessage(), new ButtonType[0]);
            alert.showAndWait();
        }
        this.refreshTables();
        this.refreshFactors();
        this.createJobExpPage();
    }
    
    private void initializeFactors() {
        final ObservableList<String> options = (ObservableList<String>)FXCollections.observableArrayList((Object[])this.names);
        this.housingCombo.setItems((ObservableList)options);
    }
    
    public void initializeTables() {
        this.initializeSkillsTable();
        this.initializeStatsTable();
        this.initializeTraitsTable();
    }
    
    private <T> void addTooltipToColumnCells(final TableColumn<Stat, T> column) {
        final Callback<TableColumn<Stat, T>, TableCell<Stat, T>> existingCellFactory = (Callback<TableColumn<Stat, T>, TableCell<Stat, T>>)column.getCellFactory();
        column.setCellFactory(NpcDetailsController::lambda$addTooltipToColumnCells$0);
    }
    
    private void initializeTraitsTable() {
        TableColumn tableColumn = (TableColumn)this.traitsTable.getColumns().get(0);
        tableColumn.setCellValueFactory((Callback)new PropertyValueFactory("name"));
        tableColumn = (TableColumn)this.traitsTable.getColumns().get(1);
        tableColumn.setCellValueFactory((Callback)new PropertyValueFactory("effect"));
    }
    
    private void initializeStatsTable() {
        TableColumn tableColumn = (TableColumn)this.statsTable.getColumns().get(0);
        tableColumn.setSortType(TableColumn.SortType.ASCENDING);
        tableColumn.setCellValueFactory((Callback)new PropertyValueFactory("name"));
        tableColumn = (TableColumn)this.statsTable.getColumns().get(1);
        tableColumn.setCellValueFactory((Callback)new PropertyValueFactory("value"));
        final TableColumn<Stat, Double> tableCol = (TableColumn<Stat, Double>)new TableColumn("Progress");
        tableCol.setCellValueFactory((Callback)new PropertyValueFactory("progress"));
        tableCol.setCellFactory(ProgressBarTableCell.forTableColumn());
        this.statsTable.getColumns().addAll(new Object[] { tableCol });
        this.statsTable.setEditable(false);
        this.setTooltips(this.statsTable);
    }
    
    public void refreshTables() {
        this.skillsTable.getItems().remove(0, this.skillsTable.getItems().size());
        final ObservableList data = FXCollections.observableArrayList((Collection)new ArrayList(this.currentNpc.getSkills().values()));
        this.skillsTable.setItems(data);
        this.setTooltips(this.skillsTable);
        this.statsTable.getItems().remove(0, this.statsTable.getItems().size());
        final ObservableList data2 = FXCollections.observableArrayList((Collection)new ArrayList(this.currentNpc.getStats().values()));
        this.statsTable.setItems(data2);
        this.setTooltips(this.statsTable);
        this.traitsTable.getItems().remove(0, this.traitsTable.getItems().size());
        final ObservableList data3 = FXCollections.observableArrayList((Collection)this.currentNpc.getTraits());
        this.traitsTable.setItems(data3);
    }
    
    private void refreshFactors() {
        final Integer index = this.currentNpc.getFactors().get("HousingFactor");
        this.housingCombo.getSelectionModel().select((Object)this.names[index]);
    }
    
    private void initializeSkillsTable() {
        TableColumn tableColumn = (TableColumn)this.skillsTable.getColumns().get(0);
        tableColumn.setCellValueFactory((Callback)new PropertyValueFactory("name"));
        tableColumn = (TableColumn)this.skillsTable.getColumns().get(1);
        tableColumn.setCellValueFactory((Callback)new PropertyValueFactory("value"));
        final TableColumn<Skill, Double> tableCol = (TableColumn<Skill, Double>)new TableColumn("Progress");
        tableCol.setCellValueFactory((Callback)new PropertyValueFactory("progress"));
        tableCol.setCellFactory(ProgressBarTableCell.forTableColumn());
        tableCol.setPrefWidth(110.0);
        tableCol.setResizable(false);
        this.skillsTable.getColumns().addAll(new Object[] { tableCol });
        this.skillsTable.setEditable(false);
    }
    
    public void initializeGrids() {
        this.clearGrids();
        this.initializeNpcGrid();
        this.initializePlayerGrid();
        this.initializeEqGrid();
        this.setDragables();
        this.setDetectors();
    }
    
    private <T extends DetailsInterface> void setTooltips(final TableView tableView) {
        this.skillsTable.setRowFactory(this::lambda$setTooltips$1);
    }
    
    private void clearGrids() {
        this.PlayerEqGrid.getChildren().clear();
        this.npcEqGrid.getChildren().clear();
        this.EqGrid.getChildren().clear();
    }
    
    public void initializeNpcGrid() {
        final List<Item> inventory = (List<Item>)this.currentNpc.getInventory();
        Integer index = 0;
        for (int row = 0; row < 4; ++row) {
            for (int column = 0; column < 6; ++column) {
                if (index < inventory.size()) {
                    final String imagePath = inventory.get(index).getPath();
                    final Image image = new Image(NpcDetailsController.class.getResourceAsStream(imagePath));
                    final ImageView imageView = new ImageView(image);
                    this.npcEqGrid.add((Node)imageView, column, row);
                    ++index;
                }
                else {
                    final String imagePath = "/UI/EqEmpty.png";
                    final Image image = new Image(NpcDetailsController.class.getResourceAsStream(imagePath));
                    final ImageView imageView = new ImageView(image);
                    this.npcEqGrid.add((Node)imageView, column, row);
                }
            }
        }
    }
    
    public void initializeEqGrid() {
        final List<Item> inventory = (List<Item>)this.currentNpc.getInventory();
        Integer index = 0;
        for (int row = 0; row < 5; ++row) {
            for (int column = 0; column < 3; ++column) {
                if (index < inventory.size()) {
                    final String imagePath = inventory.get(index).getPath();
                    final Image image = new Image(NpcDetailsController.class.getResourceAsStream(imagePath));
                    final ImageView imageView = new ImageView(image);
                    this.EqGrid.add((Node)imageView, column, row);
                    ++index;
                }
                else {
                    final String imagePath = "/UI/EqEmpty.png";
                    final Image image = new Image(NpcDetailsController.class.getResourceAsStream(imagePath));
                    final ImageView imageView = new ImageView(image);
                    this.EqGrid.add((Node)imageView, column, row);
                }
            }
        }
    }
    
    public void initializePlayerGrid() {
        final List<Item> inventory = (List<Item>)this.player.getInventory();
        Integer index = 0;
        for (int row = 0; row < 4; ++row) {
            for (int column = 0; column < 6; ++column) {
                if (index < inventory.size()) {
                    final String imagePath = inventory.get(index).getPath();
                    final Image image = new Image(NpcDetailsController.class.getResourceAsStream(imagePath));
                    final ImageView imageView = new ImageView(image);
                    this.PlayerEqGrid.add((Node)imageView, column, row);
                    ++index;
                }
                else {
                    final String imagePath = "/UI/EqEmpty.png";
                    final Image image = new Image(NpcDetailsController.class.getResourceAsStream(imagePath));
                    final ImageView imageView = new ImageView(image);
                    this.PlayerEqGrid.add((Node)imageView, column, row);
                }
            }
        }
    }
    
    public void setDetectors() {
        this.EqGrid.getChildren().stream().forEach(this::lambda$setDetectors$2);
        this.PlayerEqGrid.getChildren().stream().forEach(this::lambda$setDetectors$3);
        this.npcEqGrid.getChildren().stream().forEach(this::lambda$setDetectors$4);
    }
    
    public void setDetector(final ImageView image) {
        image.setOnMouseDragReleased((EventHandler)new NpcDetailsController$6(this));
    }
    
    public void setDragables() {
        this.EqGrid.getChildren().stream().forEach(this::lambda$setDragables$5);
        this.PlayerEqGrid.getChildren().stream().forEach(this::lambda$setDragables$6);
        this.npcEqGrid.getChildren().stream().forEach(this::lambda$setDragables$7);
    }
    
    public void setDragable(final ImageView image) {
        image.setOnDragDetected((EventHandler)new NpcDetailsController$7(this, image));
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
    
    public void housingSelected() {
        final Integer selectedIndex = this.housingCombo.getSelectionModel().getSelectedIndex();
        this.currentNpc.getFactors().put("HousingFactor", selectedIndex);
        this.descriptionText.setText(this.currentNpc.getDesc() + this.descriptionService.genStatusDesc(this.currentNpc));
    }
    
    public void setDialog(final FXMLDialog dialog) {
        this.dialog = dialog;
    }
    
    public void createJobExpPage() {
        this.jobsTabsPane.getTabs().remove(0, this.jobsTabsPane.getTabs().size());
        for (final Map.Entry<String, List<NPCTaskExpStats>> entry : this.currentNpc.getJobExperience().entrySet()) {
            final Tab tab = new Tab();
            tab.setText((String)entry.getKey());
            tab.setContent((Node)this.createJobTable(entry.getValue()));
            this.jobsTabsPane.getTabs().add((Object)tab);
        }
    }
    
    private TableView createJobTable(final List<NPCTaskExpStats> npcTaskExpStatsList) {
        final TableColumn<Map.Entry<String, Integer>, String> taskNameCol = (TableColumn<Map.Entry<String, Integer>, String>)new TableColumn("Task");
        taskNameCol.setCellValueFactory((Callback)new NpcDetailsController$8(this));
        final TableColumn<Map.Entry<String, Integer>, String> expCol = (TableColumn<Map.Entry<String, Integer>, String>)new TableColumn("Exp");
        expCol.setCellValueFactory((Callback)new NpcDetailsController$9(this));
        final TableView<Map.Entry<String, Integer>> table = (TableView<Map.Entry<String, Integer>>)new TableView(this.getJobsTableData(npcTaskExpStatsList));
        table.getColumns().addAll((Object[])new TableColumn[] { taskNameCol, expCol });
        return table;
    }
    
    public ObservableList<Map.Entry<String, Integer>> getJobsTableData(final List<NPCTaskExpStats> npcTaskExpStatsList) {
        final Map<String, Integer> taskMap = new TreeMap<String, Integer>();
        npcTaskExpStatsList.forEach(NpcDetailsController::lambda$getJobsTableData$8);
        return (ObservableList<Map.Entry<String, Integer>>)FXCollections.observableArrayList((Collection)taskMap.entrySet());
    }
    
    public void goToHub() {
        this.dialog.close();
        this.screens.hubDialog().show();
    }
    
    public void goToPrev() {
        Integer index = this.npcService.getHiredNpcs().indexOf(this.currentNpc);
        --index;
        if (index == -1) {
            index = this.npcService.getHiredNpcs().size() - 1;
        }
        this.npcService.setCurrentNpc((Npc)this.npcService.getHiredNpcs().get(index));
        this.refresh();
    }
    
    public void goToNext() {
        Integer index = this.npcService.getHiredNpcs().indexOf(this.currentNpc);
        ++index;
        if (index >= this.npcService.getHiredNpcs().size()) {
            index = 0;
        }
        this.npcService.setCurrentNpc((Npc)this.npcService.getHiredNpcs().get(index));
        this.refresh();
    }
}
