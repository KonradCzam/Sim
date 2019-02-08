package com.example.ScriptGenerator.Nodes;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class sellNpcNode extends FunctionCall{
    List<String> paramNames;
    public sellNpcNode(String name, String function, List< String> params, String text) {
        paramNames = new ArrayList<>();
        paramNames.add("NpcName");
        paramNames.add("NextNode");
        this.function = "sellNpc";
        this.text = text;
        this.params = fillParams(getParamNames(),params);
    }
}

