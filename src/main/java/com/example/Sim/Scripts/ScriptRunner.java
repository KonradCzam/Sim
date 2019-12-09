package com.example.Sim.Scripts;


import com.example.Sim.Scripts.ScriptGenerator.FunctionCall;
import com.example.Sim.Scripts.ScriptGenerator.FunctionCalls.SellNpcNode;
import com.example.Sim.Scripts.ScriptGenerator.Node;
import com.example.Sim.Scripts.ScriptGenerator.Script;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class ScriptRunner {

    @Resource
    ScriptFunctionCaller scriptFunctionCaller;
    List<Node> scriptNodes;

    public Node startScript(String scriptPath) {
        Script script = null;
        try {
            script = loadScript(scriptPath);
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        scriptNodes = script.getNode();
        return scriptNodes.get(0);
    }

    public Node run(String response, Node currentNode) {
        FunctionCall functionCall = currentNode.getOptions().stream().filter(functionCall1 -> functionCall1.getText().equals(response)).findAny().get();
        String nextNodeName = scriptFunctionCaller.call(functionCall);
        if(nextNodeName.equals("End")){
            return new Node("End","End",null);
        }
        Node nextScriptNode = selectNode(nextNodeName);

        return nextScriptNode;
    }

    private Node selectNode(String nodeName) {
        return scriptNodes.stream().filter(node -> node.getName().equals(nodeName)).findAny().get();
    }


    private Script loadScript(String path)throws JAXBException, FileNotFoundException {
        File file = new File(path);
        JAXBContext jaxbContext = JAXBContext.newInstance(Script.class, SellNpcNode.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Script script = (Script) unmarshaller.unmarshal(file);
        return script;
    }




}
