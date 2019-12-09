package com.example.Sim.Scripts.ScriptGenerator.FunctionCalls;

import com.example.Sim.Scripts.ScriptGenerator.FunctionCall;
import lombok.Getter;

@Getter
public class HireNpcNode extends FunctionCall {
    String NpcName;

    public HireNpcNode() {
    }

    public HireNpcNode(String npcName, String text, String nextNode) {
        this.NpcName = npcName;
        this.Function = "HireNpc";
        this.Text = text;
        this.NextNode = nextNode;
    }
}

