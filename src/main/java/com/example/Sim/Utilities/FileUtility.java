package com.example.Sim.Utilities;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

@Service
@Setter
public class FileUtility {
    public void openImage() {

    }

    private String directory  ;

    public String[] getFileArray(String directory){
        this.directory = directory;
        File dir = new File(directory);
        return dir.list();
    }

    public boolean removeFile(String filename){
        File dir = new File(directory+filename);
        return  dir.delete();
    }
    public void deleteDirectoryStream(String stringPath) throws IOException {
        stringPath = directory+stringPath;
        Path path = Paths.get(stringPath);
        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }
}