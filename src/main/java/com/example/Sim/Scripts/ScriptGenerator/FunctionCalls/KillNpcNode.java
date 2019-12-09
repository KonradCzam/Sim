package com.example.Sim.Scripts.ScriptGenerator.FunctionCalls;

import com.example.Sim.Scripts.ScriptGenerator.FunctionCall;
import lombok.Getter;

import javax.xml.bind.annotation.XmlAttribute;

@Getter
public class KillNpcNode extends FunctionCall {
    @XmlAttribute
    public String NpcName;

    public KillNpcNode() {
    }

    public KillNpcNode(String text, String npcName, String nextNode) {
        this.NpcName = npcName;
        this.Function = "KillNpc";
        this.Text = text;
        this.NextNode = nextNode;
    }
}

