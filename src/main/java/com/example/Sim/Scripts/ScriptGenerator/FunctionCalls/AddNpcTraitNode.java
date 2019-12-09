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
public class AddNpcTraitNode extends FunctionCall {
    @XmlAttribute
    public String NpcName;
    @XmlAttribute
    public String TraitName;

    public AddNpcTraitNode() {
    }

    public AddNpcTraitNode(String text, String nextNode, String npcName, String traitName) {
        Text = text;
        Function = "AddNpcTrait";
        NpcName = npcName;
        TraitName = traitName;
        NextNode = nextNode;
    }
}

