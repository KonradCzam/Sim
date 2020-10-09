package com.example.Sim.controllers;

import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.FXML.DialogController;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Model.NPC.Npc;
import com.example.Sim.Scripts.ScriptGenerator.FunctionCall;
import com.example.Sim.Scripts.ScriptGenerator.Node;
import com.example.Sim.Scripts.ScriptRunner;
import com.example.Sim.Services.PlayerService;
import com.example.Sim.Utilities.ImageHandler;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Service
public class InteractionController  {
    public InteractionController() {
    }

    @FXML
    private TextArea interText;
    @FXML
    private TextArea interOption1;
    @FXML
    private TextArea interOption2;
    @FXML
    private TextArea interOption3;
    @FXML
    private TextArea interOption4;
    @FXML
    private ImageView interImage;

    @Resource
    private ScriptRunner scriptRunner;
    @Resource
    private PlayerService playerService;
    @Resource
    private ImageHandler imageHandler;

    private ScreensConfiguration screens;
    private FXMLDialog dialog;
    private Node currentNode;
    private Npc currentNpc;

    EventHandler<MouseEvent> onMouseClicked =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    TextArea selectedArea = (TextArea)t.getSource();
                    Node nextNode = scriptRunner.run(selectedArea.getText(), currentNode);
                    if("End".equals(currentNode.getName())){
                        screens.activate("playerDetails");
                    }else{
                        currentNode = nextNode;
                        setTexts(currentNode);
                    }
                }
            };

    private void setTexts(Node node) {
        interText.setText(node.getText());
        List<FunctionCall> options =  node.getOptions();

        interOption1.setText(options.get(0).getText());
        if(options.size() > 1)
        interOption2.setText(options.get(1).getText());
        if(options.size() > 2)
        interOption3.setText(options.get(2).getText());
        if(options.size() > 3)
        interOption4.setText(options.get(3).getText());
    }

    public InteractionController(ScreensConfiguration screens) {
        this.screens = screens;
    }


    public void initialize(URL location, ResourceBundle resources) {

        currentNode = scriptRunner.startScript("Data/Scripts/product.xml");
        interOption1.setOnMouseClicked(onMouseClicked);
        interOption2.setOnMouseClicked(onMouseClicked);
        interOption3.setOnMouseClicked(onMouseClicked);
        interOption4.setOnMouseClicked(onMouseClicked);
        setTexts(currentNode);
    }



    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }





}
