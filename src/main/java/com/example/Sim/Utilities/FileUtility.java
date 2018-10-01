package com.example.Sim.Utilities;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Setter
public class FileUtility {

    @Value("${girls.directory:./New folder/}")
    private String directory;
    @Value("#{'${categories.all}'.split(',')}")
    private List<String> allCategories;

    public void openImage() {

    }

    public String[] getFileArray() {
        File dir = new File(directory);
        return dir.list();
    }


    public boolean removeFile(String filename) {
        File dir = new File(directory + filename);
        return dir.delete();
    }

    public void deleteDirectoryStream(String stringPath) throws IOException {
        stringPath = directory + stringPath;
        Path path = Paths.get(stringPath);
        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    public List<String> checkNpcTypes(String name) {
        String dirPath = directory + name;
        List<String> possibleCategories = new ArrayList<>();

        File directory = new File(dirPath);

        List<String> pictures = Arrays.asList(directory.list());
        for (String category : allCategories) {
            if (pictures.stream().filter(line -> line.toLowerCase().contains(category)).collect(Collectors.toList()).size() > 0)
                possibleCategories.add(category);

        }
        return possibleCategories;

    }

    public boolean checkIfGifAvailable(String name, String category) {
        String dirPath = directory + name;
        File directory = new File(dirPath);
        List<String> pictures = Arrays.asList(directory.list());
        if (pictures.stream()
                .filter(line -> line.toLowerCase().contains(category))
                .filter(line -> line.toLowerCase().contains(".gif"))
                .collect(Collectors.toList()).size() > 0)
            return true;
        else
            return false;
    }
}