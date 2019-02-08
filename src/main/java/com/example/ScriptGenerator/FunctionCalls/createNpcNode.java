package com.example.ScriptGenerator.Nodes;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class createNpcNode extends FunctionCall{
    List<String> paramNames;
    public createNpcNode(String name, String function, List< String> params, String text) {
        paramNames = new ArrayList<>();
        paramNames.add("Path");
        paramNames.add("NextNode");
        this.function = "createNpc";
        this.text = text;
        this.params = fillParams(getParamNames(),params);
    }
}

