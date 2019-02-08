package com.example.ScriptGenerator.Nodes;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Getter

public class Node {
    String name;
    String text;
    List<FunctionCall> options;

    public Node() {
    }

    
}

