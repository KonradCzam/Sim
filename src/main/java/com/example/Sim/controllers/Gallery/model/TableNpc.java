package com.example.Sim.controllers.Gallery.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Setter
@Getter
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class TableNpc {
    String displayName;
    String path;
    String folder;
}
