package com.example.Sim.Utilities;

import com.example.Sim.Exceptions.ImageNotFound;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class ImageHandler {
    public void setImage(ImageView imgView, String path, String imgCategory) throws ImageNotFound {
        setImage(imgView, path, imgCategory, false);
    }

    public void setImage(ImageView imgView, String path, Boolean gifonly) throws ImageNotFound {
        setImage(imgView, path, null, false);
    }

    public void setImage(ImageView imgView, String path, String imgCategory, boolean gifOnly) throws ImageNotFound {
        String dirPath = "./New folder/";
        String gifString = ".gif";
        Image img = null;
        dirPath += path;
        if (path == null) {
            imgView.setImage(null);
        } else {
            File directory = new File(dirPath);
            List<String> pictures = Arrays.asList(directory.list());
            List<String> filteredPictures;
            if (imgCategory != null & gifOnly) {
                filteredPictures = pictures.stream()
                        .filter(line -> line.toLowerCase().contains(imgCategory))
                        .filter(line -> line.toLowerCase().contains(".gif"))
                        .collect(Collectors.toList());
            } else if (imgCategory != null & !gifOnly) {
                filteredPictures = pictures.stream()
                        .filter(line -> line.toLowerCase().contains(imgCategory))
                        .collect(Collectors.toList());
            } else if (imgCategory == null & gifOnly) {
                filteredPictures = pictures.stream()
                        .filter(line -> line.contains(".gif"))
                        .collect(Collectors.toList());
            } else {
                filteredPictures = pictures;
            }
            if (filteredPictures.size() == 0) {
                throw new ImageNotFound("Image not found for girl: " + path + "\\nCategory: " + imgCategory);
            }

            int index = ThreadLocalRandom.current().nextInt(filteredPictures.size());
            dirPath += "\\" + filteredPictures.get(index);
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
