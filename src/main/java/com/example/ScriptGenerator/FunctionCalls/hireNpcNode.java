package com.example.ScriptGenerator.Nodes;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class hireNpcNode extends FunctionCall{
    List<String> paramNames;
    public hireNpcNode(String name, String function, List< String> params, String text) {
        paramNames = new ArrayList<>();
        paramNames.add("NpcName");
        paramNames.add("NextNode");
        this.function = "hireNpc";
        this.text = text;
        this.params = fillParams(getParamNames(),params);
    }
}

