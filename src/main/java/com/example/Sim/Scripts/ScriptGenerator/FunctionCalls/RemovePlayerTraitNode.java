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
public class RemovePlayerTraitNode extends FunctionCall {
    @XmlAttribute
    public String TraitName;
    @XmlAttribute
    public String NextNodePositive;
    @XmlAttribute
    public String NextNodeNegative;

    public RemovePlayerTraitNode() {
    }

    public RemovePlayerTraitNode( String text, String nextNode,String traitName) {
        this.Function = "RemovePlayerTrait";
        this.NextNode = nextNode;
        this.Text = text;
        TraitName = traitName;
    }

}

