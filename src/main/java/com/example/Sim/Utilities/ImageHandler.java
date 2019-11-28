// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Arrays;
import java.io.File;
import javafx.scene.image.Image;
import com.example.Sim.Exceptions.ImageNotFound;
import javafx.scene.image.ImageView;

public class ImageHandler
{
    public void setImage(final ImageView imgView, final String path, final String imgCategory) throws ImageNotFound {
        this.setImage(imgView, path, imgCategory, false);
    }
    
    public void setImage(final ImageView imgView, final String path, final Boolean gifonly) throws ImageNotFound {
        this.setImage(imgView, path, null, false);
    }
    
    public String setImage(final ImageView imgView, final String path, final String imgCategory, final boolean gifOnly) throws ImageNotFound {
        String dirPath = "./New folder/";
        final String gifString = ".gif";
        Image img = null;
        dirPath += path;
        try {
            if (path == null) {
                imgView.setImage((Image)null);
            }
            else {
                final File directory = new File(dirPath);
                final List<String> pictures = Arrays.asList(directory.list());
                List<String> filteredPictures;
                if (imgCategory != null & gifOnly) {
                    filteredPictures = pictures.stream().filter(line -> line.toLowerCase().contains(imgCategory)).filter(line -> line.toLowerCase().contains(".gif")).collect((Collector<? super Object, ?, List<String>>)Collectors.toList());
                }
                else if (imgCategory != null & !gifOnly) {
                    filteredPictures = pictures.stream().filter(line -> line.toLowerCase().contains(imgCategory)).collect((Collector<? super Object, ?, List<String>>)Collectors.toList());
                }
                else if (imgCategory == null & gifOnly) {
                    filteredPictures = pictures.stream().filter(line -> line.contains(".gif")).collect((Collector<? super Object, ?, List<String>>)Collectors.toList());
                }
                else {
                    filteredPictures = pictures;
                }
                if (filteredPictures.size() == 0) {
                    throw new ImageNotFound("Image not found for girl: " + path + "\\nCategory: " + imgCategory);
                }
                final int index = ThreadLocalRandom.current().nextInt(filteredPictures.size());
                dirPath = dirPath + "\\" + filteredPictures.get(index);
                try {
                    final FileInputStream inputstream = new FileInputStream(new File(dirPath));
                    img = new Image((InputStream)inputstream);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                imgView.setImage(img);
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        return dirPath;
    }
}
