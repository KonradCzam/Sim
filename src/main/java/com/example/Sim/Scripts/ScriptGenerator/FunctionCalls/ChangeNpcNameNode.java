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
public class ChangeNpcNameNode extends FunctionCall {
    @XmlAttribute
    public String NpcName;
    @XmlAttribute
    public String NewNpcName;

    public ChangeNpcNameNode() {
    }

    public ChangeNpcNameNode(String text, String nextNode, String npcName, String newNpcName) {
        this.NpcName = npcName;
        this.NewNpcName = newNpcName;
        this.Function = "ChangeNpcName";
        this.NextNode = nextNode;
        this.Text = text;
    }

}

