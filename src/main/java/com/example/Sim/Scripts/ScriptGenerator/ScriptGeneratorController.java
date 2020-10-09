package com.example.Sim.Scripts.ScriptGenerator;

import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.FXML.DialogController;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Scripts.ScriptGenerator.FunctionCalls.ChangeGoldNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Service
@NoArgsConstructor
public class ScriptGeneratorController  {
    ScreensConfiguration screens;
    public ScriptGeneratorController(ScreensConfiguration sc){
        this.screens = sc;
    }
    @FXML
    Label param1Label;
    @FXML
    Label param2Label;
    @FXML
    Label param3Label;
    @FXML
    Label param4Label;
    @FXML
    Label param5Label;
    @FXML
    ChoiceBox<String> newNodeTypeChoiceBox;
    @FXML
    Button addNewButton;
    @FXML
    Button clearButton;
    @FXML
    TextField param1Input;
    @FXML
    TextField param2Input;
    @FXML
    TextField param3Input;
    @FXML
    TextField param4Input;
    @FXML
    TextField param5Input;
    @FXML
    TextField newNodeNameInput;
    @FXML
    TableView nodeTable;

    public void initialize(URL location, ResourceBundle resources) {
        List<FunctionCall> options = new ArrayList<>();
        FunctionCall functionCall = new ChangeGoldNode("Name1","Text fc1","10");
        FunctionCall functionCall2 = new ChangeGoldNode("Name2","Text fc2","200");
        FunctionCall functionCall3 = new ChangeGoldNode("Name3","Text fc3","20000");
        options.add(functionCall);
        options.add(functionCall2);
        options.add(functionCall3);
        Node newNode = new Node("name","other", (ArrayList<FunctionCall>) options);
        Node newNode2 = new Node("name","other", (ArrayList<FunctionCall>) options);
        Script script = new Script();
        script.Node.add(newNode);
        script.Node.add(newNode2);
        ScriptFileCreator.saveToXml(script);
    }
}
