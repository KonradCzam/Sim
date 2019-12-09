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
public class RemoveNpcTraitNode extends FunctionCall {
    @XmlAttribute
    public String NpcName;
    @XmlAttribute
    public String TraitName;

    public RemoveNpcTraitNode() {
    }

    public RemoveNpcTraitNode(String text,String nextNode, String npcName, String traitName) {
        NextNode = nextNode;
        Text = text;
        Function = "RemoveNpcTrait";
        NpcName = npcName;
        TraitName = traitName;

    }
}

