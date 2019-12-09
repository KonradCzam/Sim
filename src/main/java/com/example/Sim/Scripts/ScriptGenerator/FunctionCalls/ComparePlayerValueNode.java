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
public class ComparePlayerValueNode extends FunctionCall {
    @XmlAttribute
    public String TraitName;
    @XmlAttribute
    public String NextNodePositive;
    @XmlAttribute
    public String NextNodeNegative;

    public ComparePlayerValueNode() {
    }

    public ComparePlayerValueNode(String name, String text, String nextNode) {
        this.Function = "SellNpc";
        this.NextNode = nextNode;
        this.Text = text;
    }

}

