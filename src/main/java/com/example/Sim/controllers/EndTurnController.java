package com.example.Sim.controllers;

import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.Exceptions.ImageNotFound;
import com.example.Sim.FXML.DialogController;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Model.Raport.EndTurnRapport;
import com.example.Sim.Model.Raport.NpcRoot;
import com.example.Sim.Model.Raport.SingleEventRoot;
import com.example.Sim.Services.EndTurnService;
import com.example.Sim.Services.NpcService;
import com.example.Sim.Utilities.ImageHandler;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.WindowEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Service
public class EndTurnController implements Initializable, DialogController {


    @FXML
    TreeTableView endTurnTable;
    @FXML
    Button goToSelectedButton;
    @FXML
    Button endTurnNextButton;
    @FXML
    ImageView endTurnImage;
    @FXML
    TextArea descriptionBox;

    @Resource
    NpcService npcService;
    @Resource
    EndTurnService endTurnService;
    @Resource
    ImageHandler imageHandler;



    private ScreensConfiguration screens;
    private FXMLDialog dialog;

    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }

    public EndTurnController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTreeTable();

        dialog.setOnShown(onShownEventHandler);
    }

    EventHandler<WindowEvent> onShownEventHandler =
            new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    EndTurnRapport endTurnRapport = endTurnService.endTurn();
                    List<NpcRoot> npcRoots = endTurnRapport.getNpcRootList();
                    createRootNode(npcRoots);
                }
            };

    private void initializeTreeTable() {
        TreeTableColumn<EndTurnRapport, String> nameColumn = new TreeTableColumn<>("Name");
        nameColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("Name"));
        nameColumn.setPrefWidth(300.0);

        TreeTableColumn<EndTurnRapport, Integer> jobColumn = new TreeTableColumn<>("Job");
        jobColumn.setPrefWidth(100.0);
        jobColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("job"));

        TreeTableColumn<EndTurnRapport, Integer> moneyColumn = new TreeTableColumn<>("Money Earned");
        moneyColumn.setPrefWidth(100.0);
        moneyColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("moneyEarned"));

        endTurnTable.getColumns().add(nameColumn);
        endTurnTable.getColumns().add(jobColumn);
        endTurnTable.getColumns().add(moneyColumn);
        endTurnTable.getSelectionModel().selectedItemProperty().addListener((obs) -> {
            tableRowSelected();
        });
    }

    private void createRootNode(List<NpcRoot> npcRoots) {
        NpcRoot endTurnRapport = new NpcRoot();
        TreeItem<EndTurnRapport> rootNode = new TreeItem<>(endTurnRapport);
        List<TreeItem<EndTurnRapport>> treeItems = new ArrayList<>();
        endTurnRapport.setName("Girls");
        endTurnRapport.setMoneyEarned(calcTotalEarned(npcRoots));

        npcRoots.forEach(jobRapport -> {
            TreeItem<EndTurnRapport> npcRoot = new TreeItem<EndTurnRapport>(jobRapport);
            npcRoot.getChildren().addAll(createNpcRootNode(jobRapport));
            treeItems.add(npcRoot);
        });
        rootNode.setExpanded(true);
        rootNode.getChildren().addAll(treeItems);
        endTurnTable.setRoot(rootNode);
    }

    private List<TreeItem<EndTurnRapport>> createNpcRootNode(NpcRoot jobRapport) {
        List<TreeItem<EndTurnRapport>> singleRapportNodeList = new ArrayList<>();
        jobRapport.getDayShiftRapport().forEach(singleEventRoot -> {
            singleRapportNodeList.add(new TreeItem<EndTurnRapport>(singleEventRoot));
        });
        jobRapport.getNightShiftRapport().forEach(singleEventRoot -> {
            singleRapportNodeList.add(new TreeItem<EndTurnRapport>(singleEventRoot));
        });
        return singleRapportNodeList;
    }

    private void tableRowSelected() {
        TreeItem treeItem = (TreeItem) endTurnTable.getSelectionModel().getSelectedItem();
        if (treeItem != null) {
            EndTurnRapport npcRoot = null;
            if (treeItem.getValue().getClass() == NpcRoot.class) {
                npcRoot = (NpcRoot) treeItem.getValue();
            } else if (treeItem.getValue().getClass() == SingleEventRoot.class) {
                npcRoot = (SingleEventRoot) treeItem.getValue();
            }
            if (npcRoot != null)
                try {
                    imageHandler.setImage(endTurnImage, npcRoot.getPath(), npcRoot.getCategory(), false);
                    descriptionBox.setText(npcRoot.getDescription());
                } catch (ImageNotFound e) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, e.getTextMessage());
                    alert.showAndWait();
                }
        }

    }


    private Integer calcTotalEarned(List<NpcRoot> npcRoots) {
        Integer totalEarned = 0;
        for (int i = 0; i < npcRoots.size(); i++) {
            totalEarned += npcRoots.get(i).getDayMoneyEarned();
            totalEarned += npcRoots.get(i).getNightMoneyEarned();
        }
        return totalEarned;
    }

    public void goToHub() {
        dialog.close();
        screens.hubDialog().show();
    }

    public void goToSelected() {
        dialog.close();
        screens.hubDialog().show();
    }


}
