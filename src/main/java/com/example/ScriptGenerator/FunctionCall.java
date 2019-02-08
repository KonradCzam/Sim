package com.example.ScriptGenerator.Nodes;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FunctionCall {
    String text;
    String function;
    Map<String,String> params;

    public Map<String,String> fillParams(List<String> paramNameList, List< String> paramsList){
        Map<String,String> params = new TreeMap<>();
        for(int i = 0 ; i<paramNameList.size();i++){
            params.put(paramNameList.get(0),paramsList.get(0));
        }
        return (params);
    }
}
