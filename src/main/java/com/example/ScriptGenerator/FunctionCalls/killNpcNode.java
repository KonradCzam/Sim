package com.example.ScriptGenerator.Nodes;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class killNpcNode extends FunctionCall{
    List<String> paramNames;
    public killNpcNode(String name, String function, List< String> params, String text) {
        paramNames = new ArrayList<>();
        paramNames.add("NpcName");
        paramNames.add("NextNode");
        this.function = "killNpc";
        this.text = text;
        this.params = fillParams(getParamNames(),params);
    }
}

