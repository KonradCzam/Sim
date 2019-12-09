package com.example.Sim.Scripts.ScriptGenerator.FunctionCalls;

import com.example.Sim.Scripts.ScriptGenerator.FunctionCall;
import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;


@Getter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SellNpcNode extends FunctionCall {
    @XmlAttribute
    public String NpcName;

    public SellNpcNode() {
    }

    public SellNpcNode(String name, String text, String nextNode) {
        this.NpcName = name;
        this.Function = "SellNpc";
        this.NextNode = nextNode;
        this.Text = text;
    }

}

