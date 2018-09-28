package com.example.Sim.controllers;

import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.Exceptions.ImageNotFound;
import com.example.Sim.FXML.DialogController;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Model.*;
import com.example.Sim.Services.NpcService;
import com.example.Sim.Services.PlayerService;
import com.example.Sim.Utilities.ImageHandler;
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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Service
public class NpcDetailsController implements Initializable, DialogController {
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
    @Resource
    private NpcService npcService;
    @Resource
    private PlayerService playerService;

    private Player player;
@Resource
private ImageHandler imageHandler;

    private ScreensConfiguration screens;
    private FXMLDialog dialog;

    private Npc currentNpc;
    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;


    public NpcDetailsController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dialog.setOnShown(onShownEventHandler);
        player = playerService.getPlayer();
    }
    public void refresh(){
        currentNpc = npcService.getCurrentNpc();
        if(currentNpc != null) {
            ownerLabel.setText(currentNpc.getName() + "'s items");
            statsLabel.setText(currentNpc.getName() + "'s skills");
            skillsLabel.setText(currentNpc.getName() + "'s stats");


            try {
                imageHandler.setImage(detailsImage, currentNpc.getPath(), "profile", false);
            } catch (ImageNotFound e) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, e.getTextMessage());
                alert.showAndWait();
            }
            initializeGrids();
            initializeTables();
        }
    }
    EventHandler<WindowEvent> onShownEventHandler =
            new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                   refresh();
                }
            };
    public void initializeTables() {
        initializeSkillsTable();
        initializeStatsTable();
        initializeTraits();
    }
    private <T> void addTooltipToColumnCells(TableColumn<Stat,T> column) {

        Callback<TableColumn<Stat, T>, TableCell<Stat,T>> existingCellFactory
                = column.getCellFactory();

        column.setCellFactory(c -> {
            TableCell<Stat, T> cell = existingCellFactory.call(c);

            Tooltip tooltip = new Tooltip();
            // can use arbitrary binding here to make text depend on cell
            // in any way you need:
            tooltip.textProperty().bind(cell.itemProperty().asString());

            cell.setTooltip(tooltip);
            return cell ;
        });
    }
    private void initializeTraits() {
        ObservableList data = FXCollections.observableArrayList(currentNpc.getTraits());
        traitsTable.setItems(data);

        TableColumn tableColumn = (TableColumn) traitsTable.getColumns().get(0);
        tableColumn.setCellValueFactory(new PropertyValueFactory("name"));

    }

    private void initializeStatsTable() {
        ObservableList data = FXCollections.observableArrayList(new ArrayList<Stat>(currentNpc.getStats().values()));
        statsTable.setItems(data);
        TableColumn tableColumn = (TableColumn) statsTable.getColumns().get(0);
        tableColumn.setSortType(TableColumn.SortType.ASCENDING);
        tableColumn.setCellValueFactory(new PropertyValueFactory("name"));
        tableColumn = (TableColumn) statsTable.getColumns().get(1);
        tableColumn.setCellValueFactory(new PropertyValueFactory("value"));
        TableColumn<Stat, Double> tableCol = new TableColumn<>("Progress");
        tableCol.setCellValueFactory(new PropertyValueFactory<Stat, Double>("progress"));
        tableCol.setCellFactory(ProgressBarTableCell.<Stat> forTableColumn());
        statsTable.getColumns().addAll(tableCol);
        statsTable.setEditable(true);
        setTooltips(statsTable);

    }private void initializeSkillsTable() {
        ObservableList data = FXCollections.observableArrayList(new ArrayList<Skill>(currentNpc.getSkills().values()));
        skillsTable.setItems(data);

        TableColumn tableColumn = (TableColumn) skillsTable.getColumns().get(0);
        tableColumn.setCellValueFactory(new PropertyValueFactory("name"));
        tableColumn = (TableColumn) skillsTable.getColumns().get(1);
        tableColumn.setCellValueFactory(new PropertyValueFactory("value"));

        TableColumn<Skill, Double> tableCol = new TableColumn<>("Progress");
        tableCol.setCellValueFactory(new PropertyValueFactory<Skill, Double>("progress"));
        tableCol.setCellFactory(ProgressBarTableCell.<Skill> forTableColumn());
        tableCol.setPrefWidth(110);
        tableCol.setResizable(false);
        skillsTable.getColumns().addAll(tableCol);
        skillsTable.setEditable(true);
        setTooltips(skillsTable);
    }

    public void initializeGrids() {
        clearGrids();
        initializeNpcGrid();
        initializePlayerGrid();
        initializeEqGrid();
        setDragables();
        setDetectors();
    }
    private <T extends DetailsInterface> void setTooltips(TableView tableView){
        skillsTable.setRowFactory(tv -> new TableRow<T>() {
            private Tooltip tooltip = new Tooltip();
            @Override
            public void updateItem(T stat, boolean empty) {
                super.updateItem(stat, empty);
                if (stat == null) {
                    setTooltip(null);
                } else {
              
                    tooltip.setText("Stat description. Progress: " + stat.getProgress()*100 + "%");
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
        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 6; column++) {
                if (index < inventory.size()) {
                    npcEqGrid.add(inventory.get(index).getImageView(), column, row);
                    index++;
                } else {
                    String imagePath = "UI\\EqEmpty.png";
                    Image image = new Image(imagePath);
                    ImageView imageView = new ImageView(image);
                    npcEqGrid.add(imageView, column, row);
                }
            }
        }
    }
    public void initializeEqGrid() {
        Map<String,Item> inventory = player.getEquippedItems();
        Integer index = 0;
        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 3; column++) {
                if (index < inventory.size()) {
                    EqGrid.add(inventory.get(index).getImageView(), column, row);
                    index++;
                } else {
                    String imagePath = "UI\\EqEmpty.png";
                    Image image = new Image(imagePath);
                    ImageView imageView = new ImageView(image);
                    EqGrid.add(imageView, column, row);
                }
            }
        }
    }
    public void initializePlayerGrid() {

        List<Item> inventory = player.getInventory();

        Integer index = 0;
        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 6; column++) {
                if (index < inventory.size()) {
                    PlayerEqGrid.add(inventory.get(index).getImageView(), column, row);
                    index++;
                } else {
                    String imagePath = "UI\\EqEmpty.png";
                    Image image = new Image(imagePath);
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

    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }
    public void goToHub() {
        dialog.close();
        screens.hubDialog().show();
    }


}
