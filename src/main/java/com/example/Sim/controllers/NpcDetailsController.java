package com.example.Sim.controllers;

import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.Exceptions.ImageNotFound;
import com.example.Sim.FXML.DialogController;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Factors.HousingFactor;
import com.example.Sim.Model.DetailsInterface;
import com.example.Sim.Model.Item;
import com.example.Sim.Model.NPC.NPCTaskExpStats;
import com.example.Sim.Model.NPC.Npc;
import com.example.Sim.Model.NPC.Skill;
import com.example.Sim.Model.NPC.Stat;
import com.example.Sim.Model.Player;
import com.example.Sim.Scripts.ScriptRunner;
import com.example.Sim.Services.DescriptionService;
import com.example.Sim.Services.NpcService;
import com.example.Sim.Services.PlayerService;
import com.example.Sim.Utilities.ImageHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Service
public class NpcDetailsController implements Initializable, DialogController {
    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;
    EventHandler<MouseEvent> onMouseReleasedEventHanlder =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    ((ImageView) (t.getSource())).setMouseTransparent(false);
                    ((ImageView) (t.getSource())).setTranslateX(0);
                    ((ImageView) (t.getSource())).setTranslateY(0);

                }
            };
    EventHandler<MouseEvent> onMousePressedEventHandler =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    orgSceneX = t.getSceneX();
                    orgSceneY = t.getSceneY();
                    orgTranslateX = ((ImageView) (t.getSource())).getTranslateX();
                    orgTranslateY = ((ImageView) (t.getSource())).getTranslateY();
                    ((ImageView) (t.getSource())).setMouseTransparent(true);
                    t.setDragDetect(true);
                }
            };
    EventHandler<MouseEvent> onMouseDraggedEventHandler =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    double offsetX = t.getSceneX() - orgSceneX;
                    double offsetY = t.getSceneY() - orgSceneY;
                    double newTranslateX = orgTranslateX + offsetX;
                    double newTranslateY = orgTranslateY + offsetY;
                    ((ImageView) (t.getSource())).setTranslateX(newTranslateX);
                    ((ImageView) (t.getSource())).setTranslateY(newTranslateY);
                }
            };
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
    private ScriptRunner scriptRunner;
    @Resource
    HousingFactor housingFactor;
    @Value("#{'${factors.housing.names}'.split(',')}")
    String[] names;
    private ScreensConfiguration screens;
    private FXMLDialog dialog;
    private Npc currentNpc;
    EventHandler<WindowEvent> onShownEventHandler =
            new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    refresh();

                }
            };

    public NpcDetailsController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentNpc = npcService.getCurrentNpc();
        dialog.setOnShown(onShownEventHandler);
        player = playerService.getPlayer();
        initializeGrids();
        initializeTables();
        initializeFactors();
        refreshTables();
        createJobExpPage();
    }


    public void refresh() {
        currentNpc = npcService.getCurrentNpc();

        descriptionText.setText(currentNpc.getDesc() + descriptionService.genStatusDesc(currentNpc));
        ownerLabel.setText(currentNpc.getName() + "'s items");
        statsLabel.setText(currentNpc.getName() + "'s base skills");
        skillsLabel.setText(currentNpc.getName() + "'s base stats");


        try {
            imageHandler.setImage(detailsImage, currentNpc.getPath(), "profile", false);
        } catch (ImageNotFound e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, e.getTextMessage());
            alert.showAndWait();
        }
        refreshTables();
        refreshFactors();
        createJobExpPage();

    }


    private void initializeFactors() {
        ObservableList<String> options =
                FXCollections.observableArrayList(names);
        housingCombo.setItems(options);
    }

    public void initializeTables() {
        initializeSkillsTable();
        initializeStatsTable();
        initializeTraitsTable();
    }

    private <T> void addTooltipToColumnCells(TableColumn<Stat, T> column) {

        Callback<TableColumn<Stat, T>, TableCell<Stat, T>> existingCellFactory
                = column.getCellFactory();

        column.setCellFactory(c -> {
            TableCell<Stat, T> cell = existingCellFactory.call(c);

            Tooltip tooltip = new Tooltip();
            // can use arbitrary binding here to make text depend on cell
            // in any way you need:
            tooltip.textProperty().bind(cell.itemProperty().asString());

            cell.setTooltip(tooltip);
            return cell;
        });
    }

    private void initializeTraitsTable() {


        TableColumn tableColumn = (TableColumn) traitsTable.getColumns().get(0);
        tableColumn.setCellValueFactory(new PropertyValueFactory("name"));
        tableColumn = (TableColumn) traitsTable.getColumns().get(1);
        tableColumn.setCellValueFactory(new PropertyValueFactory("effect"));

    }

    private void initializeStatsTable() {

        TableColumn tableColumn = (TableColumn) statsTable.getColumns().get(0);
        tableColumn.setSortType(TableColumn.SortType.ASCENDING);
        tableColumn.setCellValueFactory(new PropertyValueFactory("name"));
        tableColumn = (TableColumn) statsTable.getColumns().get(1);
        tableColumn.setCellValueFactory(new PropertyValueFactory("value"));
        TableColumn<Stat, Double> tableCol = new TableColumn<>("Progress");
        tableCol.setCellValueFactory(new PropertyValueFactory<Stat, Double>("progress"));
        tableCol.setCellFactory(ProgressBarTableCell.<Stat>forTableColumn());
        statsTable.getColumns().addAll(tableCol);
        statsTable.setEditable(false);
        setTooltips(statsTable);

    }

    public void refreshTables() {
        skillsTable.getItems().remove(0, skillsTable.getItems().size());
        ObservableList data = FXCollections.observableArrayList(new ArrayList<Skill>(currentNpc.getSkills().values()));
        skillsTable.setItems(data);
        setTooltips(skillsTable);

        statsTable.getItems().remove(0, statsTable.getItems().size());
        ObservableList data2 = FXCollections.observableArrayList(new ArrayList<Stat>(currentNpc.getStats().values()));
        statsTable.setItems(data2);
        setTooltips(statsTable);

        traitsTable.getItems().remove(0, traitsTable.getItems().size());
        ObservableList data3 = FXCollections.observableArrayList(currentNpc.getTraits());
        traitsTable.setItems(data3);

    }

    private void refreshFactors() {
        Integer index = currentNpc.getFactors().get("HousingFactor");
        housingCombo.getSelectionModel().select(names[index]);
    }

    private void initializeSkillsTable() {

        TableColumn tableColumn = (TableColumn) skillsTable.getColumns().get(0);
        tableColumn.setCellValueFactory(new PropertyValueFactory("name"));
        tableColumn = (TableColumn) skillsTable.getColumns().get(1);
        tableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Skill, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Skill, String> p) {
                // this callback returns property for just one cell, you can't use a loop here
                // for first column we use key
                if(p.getValue().getTraitBonus() != null && p.getValue().getTraitBonus() != 0)
                    return new SimpleStringProperty(p.getValue().getValue().toString() + " + " + p.getValue().getTraitBonus().toString());
                return new SimpleStringProperty(p.getValue().getValue().toString());

            }
        });
        TableColumn<Skill, Double> tableCol = new TableColumn<>("Progress");
        tableCol.setCellValueFactory(new PropertyValueFactory<Skill, Double>("progress"));
        tableCol.setCellFactory(ProgressBarTableCell.<Skill>forTableColumn());
        tableCol.setPrefWidth(110);
        tableCol.setResizable(false);
        skillsTable.getColumns().addAll(tableCol);
        skillsTable.setEditable(false);

    }

    public void initializeGrids() {
        clearGrids();
        initializeNpcGrid();
        initializePlayerGrid();
        initializeEqGrid();
        setDragables();
        setDetectors();
    }

    private <T extends DetailsInterface> void setTooltips(TableView tableView) {
        skillsTable.setRowFactory(tv -> new TableRow<T>() {
            private Tooltip tooltip = new Tooltip();

            @Override
            public void updateItem(T stat, boolean empty) {
                super.updateItem(stat, empty);
                if (stat == null) {
                    setTooltip(null);
                } else {

                    tooltip.setText("Stat description. Progress: " + stat.getProgress() * 100 + "%");
                    setTooltip(tooltip);
                }
            }
        });
    }

    private void clearGrids() {
        PlayerEqGrid.getChildren().clear();
        npcEqGrid.getChildren().clear();
        EqGrid.getChildren().clear();
    }

    public void initializeNpcGrid() {
        List<Item> inventory = currentNpc.getInventory();

        Integer index = 0;
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 6; column++) {
                if (index < inventory.size()) {
                    String imagePath = inventory.get(index).getPath();
                    Image image = new Image(NpcDetailsController.class.getResourceAsStream(imagePath));
                    ImageView imageView = new ImageView(image);
                    npcEqGrid.add(imageView, column, row);
                    index++;
                } else {
                    String imagePath = "/UI/EqEmpty.png";
                    Image image = new Image(NpcDetailsController.class.getResourceAsStream(imagePath));
                    ImageView imageView = new ImageView(image);
                    npcEqGrid.add(imageView, column, row);
                }
            }
        }
    }

    public void initializeEqGrid() {
        List<Item> inventory = currentNpc.getInventory();
        Integer index = 0;
        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 3; column++) {
                if (index < inventory.size()) {
                    String imagePath = inventory.get(index).getPath();
                    Image image = new Image(NpcDetailsController.class.getResourceAsStream(imagePath));
                    ImageView imageView = new ImageView(image);
                    EqGrid.add(imageView, column, row);
                    index++;
                } else {
                    String imagePath = "/UI/EqEmpty.png";
                    Image image = new Image(NpcDetailsController.class.getResourceAsStream(imagePath));
                    ImageView imageView = new ImageView(image);
                    EqGrid.add(imageView, column, row);
                }
            }
        }
    }

    public void initializePlayerGrid() {

        List<Item> inventory = player.getInventory();

        Integer index = 0;
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 6; column++) {
                if (index < inventory.size()) {
                    String imagePath = inventory.get(index).getPath();
                    Image image = new Image(NpcDetailsController.class.getResourceAsStream(imagePath));
                    ImageView imageView = new ImageView(image);
                    PlayerEqGrid.add(imageView, column, row);
                    index++;
                } else {
                    String imagePath = "/UI/EqEmpty.png";
                    Image image = new Image(NpcDetailsController.class.getResourceAsStream(imagePath));
                    ImageView imageView = new ImageView(image);
                    PlayerEqGrid.add(imageView, column, row);
                }
            }
        }
    }

    public void setDetectors() {
        EqGrid.getChildren().stream().forEach(imageview -> setDetector((ImageView) imageview));
        PlayerEqGrid.getChildren().stream().forEach(imageview -> setDetector((ImageView) imageview));
        npcEqGrid.getChildren().stream().forEach(imageview -> setDetector((ImageView) imageview));
    }

    public void setDetector(ImageView image) {
        image.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
            public void handle(MouseDragEvent event) {
                replaceImages((ImageView) (event.getPickResult().getIntersectedNode()), (ImageView) event.getGestureSource());
            }
        });
    }

    public void setDragables() {
        EqGrid.getChildren().stream().forEach(imageview -> setDragable((ImageView) imageview));
        PlayerEqGrid.getChildren().stream().forEach(imageview -> setDragable((ImageView) imageview));
        npcEqGrid.getChildren().stream().forEach(imageview -> setDragable((ImageView) imageview));
    }

    public void setDragable(ImageView image) {
        image.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                image.startFullDrag();
            }
        });
        image.setOnMousePressed(onMousePressedEventHandler);
        image.setOnMouseDragged(onMouseDraggedEventHandler);
        image.setOnMouseReleased(onMouseReleasedEventHanlder);
    }

    public void replaceImages(ImageView source, ImageView target) {
        ImageView temp = new ImageView();
        temp.setImage(target.getImage());
        target.setImage(source.getImage());
        source.setImage(temp.getImage());
    }

    public void housingSelected() {
        Integer selectedIndex = housingCombo.getSelectionModel().getSelectedIndex();
        currentNpc.getFactors().put("HousingFactor", selectedIndex);
        descriptionText.setText(currentNpc.getDesc() + descriptionService.genStatusDesc(currentNpc));
    }

    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }
    public void createJobExpPage(){
        jobsTabsPane.getTabs().remove(0,jobsTabsPane.getTabs().size());
        for (Map.Entry<String,List<NPCTaskExpStats>> entry : currentNpc.getJobExperience().entrySet()) {
            Tab tab = new Tab();
            tab.setText(entry.getKey());
            tab.setContent(createJobTable(entry.getValue()));
            jobsTabsPane.getTabs().add(tab);
        }
    }

    private  TableView createJobTable(List<NPCTaskExpStats> npcTaskExpStatsList) {

        // use fully detailed type for Map.Entry<String, String>
        TableColumn<NPCTaskExpStats, String> taskNameCol = new TableColumn<>("Task");
        taskNameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<NPCTaskExpStats, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<NPCTaskExpStats, String> p) {
                // this callback returns property for just one cell, you can't use a loop here
                // for first column we use key
                return new SimpleStringProperty(p.getValue().getTaskName());
            }
        });

        TableColumn<NPCTaskExpStats, String> expCol = new TableColumn<>("Exp");
        expCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<NPCTaskExpStats, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<NPCTaskExpStats, String> p) {
                // for second column we use value
                return new SimpleStringProperty(p.getValue().getExp().toString());
            }
        });
        TableColumn<NPCTaskExpStats, String> rankCol = new TableColumn<>("Rank");
        rankCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<NPCTaskExpStats, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<NPCTaskExpStats, String> p) {
                // for second column we use value
                return new SimpleStringProperty(p.getValue().getRankLevel().toString());
            }
        });

        TableColumn<NPCTaskExpStats, String> rankNameCol = new TableColumn<>("Rank name");
        rankNameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<NPCTaskExpStats, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<NPCTaskExpStats, String> p) {
                // for second column we use value
                return new SimpleStringProperty(p.getValue().getRank());
            }
        });
        final TableView<NPCTaskExpStats> table = new TableView<NPCTaskExpStats>(FXCollections.observableArrayList(npcTaskExpStatsList));
        table.getColumns().addAll(taskNameCol, expCol,rankCol,rankNameCol);
        return table;
    }

    public void goToHub() {
        dialog.close();
        screens.hubDialog().show();
    }

    public void goToPrev() {
        Integer index = npcService.getHiredNpcs().indexOf(currentNpc);
        index -=1;
        if(index ==-1)
            index = npcService.getHiredNpcs().size()-1;
        npcService.setCurrentNpc(npcService.getHiredNpcs().get(index));
        refresh();
    }

    public void goToNext() {
        Integer index = npcService.getHiredNpcs().indexOf(currentNpc);
        index +=1;
        if(index >= npcService.getHiredNpcs().size())
            index = 0;
        npcService.setCurrentNpc(npcService.getHiredNpcs().get(index));
        refresh();
    }
    public void goToScript(){
        screens.interactionDialog().show();
    }
}
