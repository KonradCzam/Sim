package com.example.Sim.Model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;

import java.beans.Transient;
import java.io.File;
import java.io.Serializable;

@Getter
public class Item implements Serializable{
    String name;
    String description;
    String path;
    public transient ImageView imageView;

    public Item(String path) {
        File file = new File(path);
        Image image = new Image(file.toURI().toString());
        this.imageView = new ImageView(image);
    }
}
