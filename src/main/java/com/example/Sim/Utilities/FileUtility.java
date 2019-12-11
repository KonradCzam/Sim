package com.example.Sim.Utilities;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Setter
public class FileUtility {

    @Value("${girls.directory:./New folder/}")
    private String directory;
    @Value("#{'${categories.all}'.split(',')}")
    private List<String> allCategories;


    public String[] getFileArray() {
        File dir = new File(directory);
        return dir.list();
    }

    public String getRandomLine(String path) {
        File file = new File(path);
        String result = null;
        Random rand = new Random();
        int n = 0;
        try {
            for(Scanner sc = new Scanner(file); sc.hasNext(); )
            {
                ++n;
                String line = sc.nextLine();
                if(rand.nextInt(n) == 0)
                    result = line;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return result;

    }

    public boolean removeFile(String filename) {
        File dir = new File(directory + filename);
        return dir.delete();
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