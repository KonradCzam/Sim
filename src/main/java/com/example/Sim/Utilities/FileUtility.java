// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.Utilities;

import java.util.function.Predicate;
import java.util.Iterator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.IOException;
import java.util.function.Function;
import java.nio.file.Path;
import java.util.Comparator;
import java.nio.file.Files;
import java.nio.file.FileVisitOption;
import java.nio.file.Paths;
import java.io.File;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileUtility
{
    @Value("${girls.directory:./New folder/}")
    private String directory;
    @Value("#{'${categories.all}'.split(',')}")
    private List<String> allCategories;
    
    public void openImage() {
    }
    
    public String[] getFileArray() {
        final File dir = new File(this.directory);
        return dir.list();
    }
    
    public boolean removeFile(final String filename) {
        final File dir = new File(this.directory + filename);
        return dir.delete();
    }
    
    public void deleteDirectoryStream(String stringPath) throws IOException {
        stringPath = this.directory + stringPath;
        final Path path = Paths.get(stringPath, new String[0]);
        Files.walk(path, new FileVisitOption[0]).sorted(Comparator.reverseOrder()).map((Function<? super Path, ?>)Path::toFile).forEach(File::delete);
    }
    
    public List<String> checkNpcTypes(final String name) {
        final String dirPath = this.directory + name;
        final List<String> possibleCategories = new ArrayList<String>();
        final File directory = new File(dirPath);
        final List<String> pictures = Arrays.asList(directory.list());
        for (final String category : this.allCategories) {
            if (pictures.stream().filter(FileUtility::lambda$checkNpcTypes$0).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()).size() > 0) {
                possibleCategories.add(category);
            }
        }
        return possibleCategories;
    }
    
    public boolean checkIfGifAvailable(final String name, final String category) {
        final String dirPath = this.directory + name;
        final File directory = new File(dirPath);
        final List<String> pictures = Arrays.asList(directory.list());
        return pictures.stream().filter(FileUtility::lambda$checkIfGifAvailable$1).filter((Predicate<? super Object>)FileUtility::lambda$checkIfGifAvailable$2).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()).size() > 0;
    }
    
    public void setDirectory(final String directory) {
        this.directory = directory;
    }
    
    public void setAllCategories(final List<String> allCategories) {
        this.allCategories = allCategories;
    }
}
