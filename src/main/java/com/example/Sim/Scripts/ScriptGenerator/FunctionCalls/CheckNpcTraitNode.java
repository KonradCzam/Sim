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
public class CheckNpcTraitNode extends FunctionCall {
    @XmlAttribute
    public String NpcName;
    @XmlAttribute
    public String TraitName;
    @XmlAttribute
    public String NextNodePositive;
    @XmlAttribute
    public String NextNodeNegative;

    public CheckNpcTraitNode() {
    }

    public CheckNpcTraitNode(String text, String npcName, String traitName, String nextNodePositive, String nextNodeNegative) {
        Text = text;
        Function = "CheckNpcTrait";
        NpcName = npcName;
        TraitName = traitName;
        NextNodePositive = nextNodePositive;
        NextNodeNegative = nextNodeNegative;
    }
}

