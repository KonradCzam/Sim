package com.example.Sim.Utilities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class ImageHandler {

    public void setImage(ImageView imgView,String path,String imgCategory) {
        String dirPath = "./New folder/";
        Image img = null;
        dirPath += path;
        if(path == null) {
            imgView.setImage(null);
        }else {
            File directory = new File(dirPath);
            String[] pictures = directory.list();
            int index = ThreadLocalRandom.current().nextInt(pictures.length);
            dirPath += "\\" + pictures[index];
            try {
                FileInputStream inputstream = new FileInputStream(new File(dirPath));
                img = new Image(inputstream);
            } catch (
                    IOException e) {
                e.printStackTrace();
            }
            imgView.setImage(img);
        }
    }
}
