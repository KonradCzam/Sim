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
public class ChangePlayerStatNode extends FunctionCall {
    @XmlAttribute
    public String Stat;
    public String StatChange;

    public ChangePlayerStatNode() {
    }

    public ChangePlayerStatNode(String text, String nextNode, String stat, String statChange) {
        Stat = stat;
        StatChange = statChange;
        this.Function = "ChangePlayerStat";
        this.NextNode = nextNode;
        this.Text = text;
    }

}

