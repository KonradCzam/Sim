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
public class AddPlayerTraitNode extends FunctionCall {
    @XmlAttribute
    public String TraitName;

    public AddPlayerTraitNode() {
    }

    public AddPlayerTraitNode(String text, String nextNode, String traitName) {
        this.TraitName = traitName;
        this.Function = "AddPlayerTrait";
        this.NextNode = nextNode;
        this.Text = text;
    }

}

