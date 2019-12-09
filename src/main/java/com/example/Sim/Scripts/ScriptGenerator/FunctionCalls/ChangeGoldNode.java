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
public class ChangeGoldNode extends FunctionCall {
    @XmlAttribute
    public String GoldChange;

    public ChangeGoldNode() {
    }

    public ChangeGoldNode(String text, String nextNode, String goldChange) {
        this.GoldChange = goldChange;
        this.Function = "ChangeGold";
        this.NextNode = nextNode;
        this.Text = text;
    }

}

