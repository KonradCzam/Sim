package com.example.Sim.Scripts.ScriptGenerator.FunctionCalls;

import com.example.Sim.Scripts.ScriptGenerator.FunctionCall;
import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@Getter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SkipNode extends FunctionCall {

    public SkipNode() {
    }

    public SkipNode(String text, String nextNode) {
        Text = text;
        NextNode = nextNode;
        this.Function = "Skip";

    }

}

