package com.example.Sim.Scripts.ScriptGenerator;

import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
public class Script {
    List<Node> Node = new ArrayList<>();

    public Script() {
    }
}
