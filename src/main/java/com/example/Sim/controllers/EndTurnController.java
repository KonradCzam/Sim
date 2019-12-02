package com.example.Sim.controllers;

import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.Exceptions.ImageNotFound;
import com.example.Sim.FXML.DialogController;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Model.NPC.Npc;
import com.example.Sim.Model.Raport.*;
import com.example.Sim.Services.EndTurnService;
import com.example.Sim.Services.NpcService;
import com.example.Sim.Utilities.ImageHandler;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    ImageView endTurnImage;
    @FXML
    TextArea descriptionBox;

    @FXML
    ImageView endTurnImage2;
    @FXML
    TextArea descriptionBox2;
    @FXML
    VBox profileVBox;
    @Resource
    NpcService npcService;
    @Resource
    EndTurnService endTurnService;
    @Resource
    ImageHandler imageHandler;
    EventHandler<WindowEvent> onShownEventHandler =
            new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    if(endTurnService.getPresentOld()){
                        endTurnService.setPresentOld(false);
                        return;
                    }
                    EndTurnRapport endTurnRapport = endTurnService.endTurn();
                    GirlEndTurnRapport girlEndTurnRapport = endTurnRapport.getGirlEndTurnRapport();
                    List<NpcRoot> npcRoots = girlEndTurnRapport.getNpcRootList();

                    createRootNode(npcRoots);
                   // npcRoots.forEach(npcRoot -> createSingleProfileTab(npcRoot));
                    endTurnTable.refresh();

                }


            };
    private ScreensConfiguration screens;
    private FXMLDialog dialog;

    public EndTurnController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTreeTable();
        dialog.setOnShown(onShownEventHandler);
    }

    private void initializeTreeTable() {
        TreeTableColumn<GirlEndTurnRapport, String> nameColumn = new TreeTableColumn<>("Name");
        nameColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("Name"));
        nameColumn.setPrefWidth(200.0);

        TreeTableColumn<GirlEndTurnRapport, Integer> jobColumn = new TreeTableColumn<>("Job");
        jobColumn.setPrefWidth(100.0);
        jobColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("job"));

        TreeTableColumn<GirlEndTurnRapport, String> statusColumn = new TreeTableColumn<>("Status");
        statusColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("messageLevel"));

        TreeTableColumn<GirlEndTurnRapport, String> obedienceColumn = new TreeTableColumn<>("Ob.");
        obedienceColumn.setPrefWidth(70.0);
        obedienceColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("obedience"));

        TreeTableColumn<GirlEndTurnRapport, String> loveColumn = new TreeTableColumn<>("Love");
        loveColumn.setPrefWidth(70.0);
        loveColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("love"));

        endTurnTable.getColumns().add(nameColumn);
        endTurnTable.getColumns().add(jobColumn);
        endTurnTable.getColumns().add(statusColumn);
        endTurnTable.getColumns().add(obedienceColumn);
        endTurnTable.getColumns().add(loveColumn);
        endTurnTable.getSelectionModel().selectedItemProperty().addListener((obs) -> {
            tableRowSelected();
        });
    }


    private void financialTableRowSelected() {
    }

    private void createRootNode(List<NpcRoot> npcRoots) {
        NpcRoot endTurnRapport = new NpcRoot();
        TreeItem<GirlEndTurnRapport> rootNode = new TreeItem<>(endTurnRapport);
        List<TreeItem<GirlEndTurnRapport>> treeItems = new ArrayList<>();

        endTurnRapport.setName("Girls");
        npcRoots.forEach(jobRapport -> {
            TreeItem<GirlEndTurnRapport> npcRoot = new TreeItem<GirlEndTurnRapport>(jobRapport);
            npcRoot.getChildren().addAll(createNpcRootNode(jobRapport));
            treeItems.add(npcRoot);
        });
        rootNode.setExpanded(true);
        rootNode.getChildren().addAll(treeItems);
        endTurnTable.setRoot(rootNode);
    }


    private List<TreeItem<FinanceEndTurnRapport>> createClientRootNode(JobRoot financeRoot) {
        List<TreeItem<FinanceEndTurnRapport>> singleCustomerNodeList = new ArrayList<>();
        financeRoot.getAllCustomers().forEach(customerRoot -> singleCustomerNodeList.add(new TreeItem<FinanceEndTurnRapport>(customerRoot)));
        return singleCustomerNodeList;
    }

    private List<TreeItem<GirlEndTurnRapport>> createNpcRootNode(NpcRoot jobRapport) {
        List<TreeItem<GirlEndTurnRapport>> singleRapportNodeList = new ArrayList<>();
        jobRapport.getShiftRapport().forEach(singleEventRoot -> {
            singleRapportNodeList.add(new TreeItem<>(singleEventRoot));
        });
        return singleRapportNodeList;
    }

    private void tableRowSelected() {
        setProfilePicture(endTurnImage,getSelectedGirlEndTurnRapport());
    }

    public void createSingleProfileTab(NpcRoot npcRoot) {

        Pane profileTab = new Pane();
        profileTab.setPrefSize(420, 150);
        ImageView imageView = new ImageView();
        imageView.setFitWidth(100);
        imageView.setFitHeight(150);
        setProfilePicture(imageView, npcRoot);
        profileTab.getChildren().add(imageView);
        Label label = new Label();
        label.setText(npcRoot.getName());
        label.setLayoutX(100.0);
        profileTab.getChildren().add(label);
        profileTab.getStyleClass().add("profileTab");
        //profileVBox.getChildren().add(profileTab);
    }
    private void setProfilePicture(ImageView endTurnImage, GirlEndTurnRapport npcRoot){

            if (npcRoot != null)
                try {
                    if(npcRoot.getImagePath() !=null){
                        FileInputStream inputstream = new FileInputStream(new File(npcRoot.getImagePath()));
                        endTurnImage.setImage(new Image(inputstream));
                    }else {
                        npcRoot.setImagePath(imageHandler.setImage(endTurnImage, npcRoot.getPath(), npcRoot.getCategory(), false));
                    }
                    descriptionBox.setText(npcRoot.getDescription());
                } catch (ImageNotFound e) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, e.getTextMessage());
                    alert.showAndWait();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


    }
    private GirlEndTurnRapport getSelectedGirlEndTurnRapport(){
        TreeItem treeItem = (TreeItem) endTurnTable.getSelectionModel().getSelectedItem();
        GirlEndTurnRapport npcRoot = null;
        if (treeItem != null) {
            if (treeItem.getValue().getClass() == NpcRoot.class) {
                npcRoot = (NpcRoot) treeItem.getValue();
            } else if (treeItem.getValue().getClass() == SingleEventRoot.class) {
                npcRoot = (SingleEventRoot) treeItem.getValue();
            }
        }
        return npcRoot;
    }
    public void goToHub() {
        dialog.close();
        screens.hubDialog().show();
    }

    public void goToSelected() {
        TreeItem treeItem = (TreeItem) endTurnTable.getSelectionModel().getSelectedItem();
        if (treeItem != null) {
            GirlEndTurnRapport npcRoot = null;
            String npcName = null;
            if (treeItem.getValue().getClass() == NpcRoot.class) {
                npcRoot = (NpcRoot) treeItem.getValue();
                npcName = ((NpcRoot)npcRoot).getName();

            } else if (treeItem.getValue().getClass() == SingleEventRoot.class) {
                npcRoot = (SingleEventRoot) treeItem.getValue();
                npcName = ((SingleEventRoot)npcRoot).getNpcName();
            }
            if (npcRoot != null) {
                String finalNpcName = npcName;
                Npc selectedNpc = (Npc) npcService.getHiredNpcs().stream().filter(npc -> npc.getName() == finalNpcName).findFirst().get();
                npcService.setCurrentNpc(selectedNpc);
            }
        }
        dialog.close();
        screens.npcDetailsDialog().show();
    }


}
