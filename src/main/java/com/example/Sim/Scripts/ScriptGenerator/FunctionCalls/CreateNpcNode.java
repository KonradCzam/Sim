package com.example.Sim.Scripts.ScriptGenerator.FunctionCalls;

import com.example.Sim.Scripts.ScriptGenerator.FunctionCall;
import lombok.Getter;

import javax.xml.bind.annotation.XmlAttribute;

@Getter
public class CreateNpcNode extends FunctionCall {
    @XmlAttribute
    String Path;

    public CreateNpcNode() {
    }

    public CreateNpcNode(String text, String nextNode, String path) {
        this.Path = path;
        this.Function = "CreateNpc";
        this.Text = text;
        this.NextNode = nextNode;
    }
}

