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
public class CheckPlayerTraitNode extends FunctionCall {
    @XmlAttribute
    public String TraitName;
    @XmlAttribute
    public String NextNodePositive;
    @XmlAttribute
    public String NextNodeNegative;

    public CheckPlayerTraitNode() {
    }

    public CheckPlayerTraitNode(String text, String traitName, String nextNodePositive, String nextNodeNegative) {
        Text = text;
        Function = "CheckPlayerTrait";
        TraitName = traitName;
        NextNodePositive = nextNodePositive;
        NextNodeNegative = nextNodeNegative;
    }

}

