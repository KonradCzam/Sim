// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.controllers;

import javafx.beans.Observable;
import com.example.Sim.Model.Raport.CustomerRoot;
import java.util.function.Predicate;
import com.example.Sim.Model.NPC.Npc;
import java.io.FileNotFoundException;
import com.example.Sim.Exceptions.ImageNotFound;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import java.io.InputStream;
import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.File;
import com.example.Sim.Model.Raport.SingleEventRoot;
import com.example.Sim.Model.Raport.JobRoot;
import java.util.function.Consumer;
import java.util.Collection;
import java.util.ArrayList;
import javafx.scene.control.TreeItem;
import com.example.Sim.Model.Raport.NpcRoot;
import java.util.List;
import com.example.Sim.Model.Raport.FinanceEndTurnRapport;
import com.example.Sim.Model.Raport.GirlEndTurnRapport;
import javafx.util.Callback;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.control.TreeTableColumn;
import java.util.ResourceBundle;
import java.net.URL;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Config.ScreensConfiguration;
import javafx.stage.WindowEvent;
import javafx.event.EventHandler;
import com.example.Sim.Utilities.ImageHandler;
import com.example.Sim.Services.EndTurnService;
import javax.annotation.Resource;
import com.example.Sim.Services.NpcService;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.control.TreeTableView;
import org.springframework.stereotype.Service;
import com.example.Sim.FXML.DialogController;
import javafx.fxml.Initializable;

@Service
public class EndTurnController implements Initializable, DialogController
{
    @FXML
    TreeTableView endTurnTable;
    @FXML
    Button goToSelectedButton;
    @FXML
    ImageView endTurnImage;
    @FXML
    TextArea descriptionBox;
    @FXML
    TreeTableView endTurnFinanceTable;
    @FXML
    ImageView endTurnImage2;
    @FXML
    TextArea descriptionBox2;
    @Resource
    NpcService npcService;
    @Resource
    EndTurnService endTurnService;
    @Resource
    ImageHandler imageHandler;
    EventHandler<WindowEvent> onShownEventHandler;
    private ScreensConfiguration screens;
    private FXMLDialog dialog;
    
    public EndTurnController(final ScreensConfiguration screens) {
        this.onShownEventHandler = (EventHandler)new EndTurnController$1(this);
        this.screens = screens;
    }
    
    public void setDialog(final FXMLDialog dialog) {
        this.dialog = dialog;
    }
    
    public void initialize(final URL location, final ResourceBundle resources) {
        this.initializeTreeTable();
        this.initializeFinanacialTreeTable();
        this.dialog.setOnShown(this.onShownEventHandler);
    }
    
    private void initializeTreeTable() {
        final TreeTableColumn<GirlEndTurnRapport, String> nameColumn = (TreeTableColumn<GirlEndTurnRapport, String>)new TreeTableColumn("Name");
        nameColumn.setCellValueFactory((Callback)new TreeItemPropertyValueFactory("Name"));
        nameColumn.setPrefWidth(200.0);
        final TreeTableColumn<GirlEndTurnRapport, Integer> jobColumn = (TreeTableColumn<GirlEndTurnRapport, Integer>)new TreeTableColumn("Job");
        jobColumn.setPrefWidth(100.0);
        jobColumn.setCellValueFactory((Callback)new TreeItemPropertyValueFactory("job"));
        final TreeTableColumn<GirlEndTurnRapport, String> statusColumn = (TreeTableColumn<GirlEndTurnRapport, String>)new TreeTableColumn("Status");
        statusColumn.setCellValueFactory((Callback)new TreeItemPropertyValueFactory("messageLevel"));
        final TreeTableColumn<GirlEndTurnRapport, String> obedienceColumn = (TreeTableColumn<GirlEndTurnRapport, String>)new TreeTableColumn("Ob.");
        obedienceColumn.setPrefWidth(70.0);
        obedienceColumn.setCellValueFactory((Callback)new TreeItemPropertyValueFactory("obedience"));
        final TreeTableColumn<GirlEndTurnRapport, String> loveColumn = (TreeTableColumn<GirlEndTurnRapport, String>)new TreeTableColumn("Love");
        loveColumn.setPrefWidth(70.0);
        loveColumn.setCellValueFactory((Callback)new TreeItemPropertyValueFactory("love"));
        this.endTurnTable.getColumns().add((Object)nameColumn);
        this.endTurnTable.getColumns().add((Object)jobColumn);
        this.endTurnTable.getColumns().add((Object)statusColumn);
        this.endTurnTable.getColumns().add((Object)obedienceColumn);
        this.endTurnTable.getColumns().add((Object)loveColumn);
        this.endTurnTable.getSelectionModel().selectedItemProperty().addListener(this::lambda$initializeTreeTable$0);
    }
    
    private void initializeFinanacialTreeTable() {
        final TreeTableColumn<FinanceEndTurnRapport, String> jobColumn = (TreeTableColumn<FinanceEndTurnRapport, String>)new TreeTableColumn("Job");
        jobColumn.setCellValueFactory((Callback)new TreeItemPropertyValueFactory("name"));
        jobColumn.setPrefWidth(180.0);
        final TreeTableColumn<FinanceEndTurnRapport, Integer> moneyColumn = (TreeTableColumn<FinanceEndTurnRapport, Integer>)new TreeTableColumn("Spent");
        moneyColumn.setPrefWidth(40.0);
        moneyColumn.setCellValueFactory((Callback)new TreeItemPropertyValueFactory("moneyEarned"));
        final TreeTableColumn<FinanceEndTurnRapport, String> popularityColumn = (TreeTableColumn<FinanceEndTurnRapport, String>)new TreeTableColumn("Popularity");
        popularityColumn.setCellValueFactory((Callback)new TreeItemPropertyValueFactory("popularity"));
        final TreeTableColumn<FinanceEndTurnRapport, String> tierColumn = (TreeTableColumn<FinanceEndTurnRapport, String>)new TreeTableColumn("Tier");
        tierColumn.setCellValueFactory((Callback)new TreeItemPropertyValueFactory("tier"));
        final TreeTableColumn<FinanceEndTurnRapport, String> happinessColumn = (TreeTableColumn<FinanceEndTurnRapport, String>)new TreeTableColumn("Happiness");
        happinessColumn.setCellValueFactory((Callback)new TreeItemPropertyValueFactory("happiness"));
        final TreeTableColumn<FinanceEndTurnRapport, String> mainConcernColumn = (TreeTableColumn<FinanceEndTurnRapport, String>)new TreeTableColumn("Concern");
        mainConcernColumn.setCellValueFactory((Callback)new TreeItemPropertyValueFactory("mainConcern"));
        final TreeTableColumn<FinanceEndTurnRapport, String> secondaryConcernColumn = (TreeTableColumn<FinanceEndTurnRapport, String>)new TreeTableColumn("Secondary concern");
        secondaryConcernColumn.setCellValueFactory((Callback)new TreeItemPropertyValueFactory("secondaryConcern"));
        this.endTurnFinanceTable.getColumns().add((Object)jobColumn);
        this.endTurnFinanceTable.getColumns().add((Object)tierColumn);
        this.endTurnFinanceTable.getColumns().add((Object)mainConcernColumn);
        this.endTurnFinanceTable.getColumns().add((Object)secondaryConcernColumn);
        this.endTurnFinanceTable.getColumns().add((Object)happinessColumn);
        this.endTurnFinanceTable.getColumns().add((Object)moneyColumn);
        this.endTurnFinanceTable.getSelectionModel().selectedItemProperty().addListener(this::lambda$initializeFinanacialTreeTable$1);
    }
    
    private void financialTableRowSelected() {
    }
    
    private void createRootNode(final List<NpcRoot> npcRoots) {
        final NpcRoot endTurnRapport = new NpcRoot();
        final TreeItem<GirlEndTurnRapport> rootNode = (TreeItem<GirlEndTurnRapport>)new TreeItem((Object)endTurnRapport);
        final List<TreeItem<GirlEndTurnRapport>> treeItems = new ArrayList<TreeItem<GirlEndTurnRapport>>();
        endTurnRapport.setName("Girls");
        npcRoots.forEach(this::lambda$createRootNode$2);
        rootNode.setExpanded(true);
        rootNode.getChildren().addAll((Collection)treeItems);
        this.endTurnTable.setRoot((TreeItem)rootNode);
    }
    
    private void createFinancialRootNode(final FinanceEndTurnRapport financeEndTurnRapport) {
        financeEndTurnRapport.setName("Jobs");
        final TreeItem<FinanceEndTurnRapport> rootNode = (TreeItem<FinanceEndTurnRapport>)new TreeItem((Object)financeEndTurnRapport);
        final List<TreeItem<FinanceEndTurnRapport>> treeItems = new ArrayList<TreeItem<FinanceEndTurnRapport>>();
        financeEndTurnRapport.getFinanceRootList().forEach(this::lambda$createFinancialRootNode$3);
        rootNode.setExpanded(true);
        rootNode.getChildren().addAll((Collection)treeItems);
        this.endTurnFinanceTable.setRoot((TreeItem)rootNode);
    }
    
    private List<TreeItem<FinanceEndTurnRapport>> createClientRootNode(final JobRoot financeRoot) {
        final List<TreeItem<FinanceEndTurnRapport>> singleCustomerNodeList = new ArrayList<TreeItem<FinanceEndTurnRapport>>();
        financeRoot.getAllCustomers().forEach(EndTurnController::lambda$createClientRootNode$4);
        return singleCustomerNodeList;
    }
    
    private List<TreeItem<GirlEndTurnRapport>> createNpcRootNode(final NpcRoot jobRapport) {
        final List<TreeItem<GirlEndTurnRapport>> singleRapportNodeList = new ArrayList<TreeItem<GirlEndTurnRapport>>();
        jobRapport.getDayShiftRapport().forEach(EndTurnController::lambda$createNpcRootNode$5);
        jobRapport.getNightShiftRapport().forEach(EndTurnController::lambda$createNpcRootNode$6);
        return singleRapportNodeList;
    }
    
    private void tableRowSelected() {
        final TreeItem treeItem = (TreeItem)this.endTurnTable.getSelectionModel().getSelectedItem();
        if (treeItem != null) {
            GirlEndTurnRapport npcRoot = null;
            if (treeItem.getValue().getClass() == NpcRoot.class) {
                npcRoot = (GirlEndTurnRapport)treeItem.getValue();
            }
            else if (treeItem.getValue().getClass() == SingleEventRoot.class) {
                npcRoot = (GirlEndTurnRapport)treeItem.getValue();
            }
            if (npcRoot != null) {
                try {
                    if (npcRoot.getImagePath() != null) {
                        final FileInputStream inputstream = new FileInputStream(new File(npcRoot.getImagePath()));
                        this.endTurnImage.setImage(new Image((InputStream)inputstream));
                    }
                    else {
                        npcRoot.setImagePath(this.imageHandler.setImage(this.endTurnImage, npcRoot.getPath(), npcRoot.getCategory(), false));
                    }
                    this.descriptionBox.setText(npcRoot.getDescription());
                }
                catch (ImageNotFound e) {
                    final Alert alert = new Alert(Alert.AlertType.CONFIRMATION, e.getTextMessage(), new ButtonType[0]);
                    alert.showAndWait();
                }
                catch (FileNotFoundException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
    
    public void goToHub() {
        this.dialog.close();
        this.screens.hubDialog().show();
    }
    
    public void goToSelected() {
        final TreeItem treeItem = (TreeItem)this.endTurnTable.getSelectionModel().getSelectedItem();
        if (treeItem != null) {
            GirlEndTurnRapport npcRoot = null;
            String npcName = null;
            if (treeItem.getValue().getClass() == NpcRoot.class) {
                npcRoot = (GirlEndTurnRapport)treeItem.getValue();
                npcName = ((NpcRoot)npcRoot).getName();
            }
            else if (treeItem.getValue().getClass() == SingleEventRoot.class) {
                npcRoot = (GirlEndTurnRapport)treeItem.getValue();
                npcName = ((SingleEventRoot)npcRoot).getNpcName();
            }
            if (npcRoot != null) {
                final String finalNpcName = npcName;
                final Npc selectedNpc = (Npc)this.npcService.getHiredNpcs().stream().filter(EndTurnController::lambda$goToSelected$7).findFirst().get();
                this.npcService.setCurrentNpc(selectedNpc);
            }
        }
        this.dialog.close();
        this.screens.npcDetailsDialog().show();
    }
}
