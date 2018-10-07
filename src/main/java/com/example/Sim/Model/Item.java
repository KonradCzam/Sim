package com.example.Sim.Model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;

import java.io.File;
import java.io.Serializable;

@Getter
public class Item implements Serializable {

    private String name;
    private String description;
    private String path;

    public Item(String path) {
        this.path = path;
    }
}
