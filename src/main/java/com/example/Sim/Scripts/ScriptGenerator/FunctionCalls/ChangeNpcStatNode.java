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
public class ChangeNpcStatNode extends FunctionCall {
    @XmlAttribute
    public String NpcName;
    @XmlAttribute
    public String StatName;
    @XmlAttribute
    public String StatChange;

    public ChangeNpcStatNode() {
    }

    public ChangeNpcStatNode(String text, String nextNode , String npcName, String statName, String statChange) {
        NpcName = npcName;
        StatName = statName;
        StatChange = statChange;
        this.Function = "ChangeNpcStat";
        this.NextNode = nextNode;
        this.Text = text;
    }

}

