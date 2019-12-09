package com.example.Sim.Scripts.ScriptGenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

@Getter
@AllArgsConstructor
public class Node {
    @XmlElement
    String Name;
    @XmlElement
    String Text;
    @XmlElementWrapper(name="Options")
    @XmlElement(name="FunctionCall")
    List<FunctionCall> Options;

    public Node() {
    }

    
}

