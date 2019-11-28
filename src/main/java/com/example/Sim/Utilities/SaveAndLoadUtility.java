// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.Utilities;

import javafx.concurrent.Task;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import com.example.Sim.Model.SaveData;
import java.util.ArrayList;
import java.util.List;
import com.example.Sim.Model.SaveSlot;
import java.util.Optional;
import java.io.File;
import com.example.Sim.Services.EndTurnService;
import com.example.Sim.Services.PlayerService;
import javax.annotation.Resource;
import com.example.Sim.Services.NpcService;

public class SaveAndLoadUtility
{
    private final String DIRECTORY_NAME;
    private final String PATH_ROOT;
    private final String SAVE_FILE = "gameData.saveSlot";
    private final String SAVE_FILE_PATH;
    @Resource
    NpcService npcService;
    @Resource
    PlayerService playerService;
    @Resource
    EndTurnService endTurnService;
    
    public SaveAndLoadUtility() {
        this.DIRECTORY_NAME = "Saves" + File.separator;
        this.PATH_ROOT = "." + File.separator + this.DIRECTORY_NAME;
        this.SAVE_FILE = "gameData.saveSlot";
        this.SAVE_FILE_PATH = this.PATH_ROOT + "gameData.saveSlot";
    }
    
    public SaveSlot createSaveSlot(final Optional<String> name) {
        final Services services = new Services(this.npcService, this.playerService, this, this.endTurnService);
        SaveSlot saveData;
        if (name.isPresent()) {
            saveData = new SaveSlot((String)name.get(), this.npcService.getHiredNpcs(), this.npcService.getHirableNpcs(), this.playerService.getPlayer(), services);
        }
        else {
            saveData = new SaveSlot(this.npcService.getHiredNpcs(), this.npcService.getHirableNpcs(), this.playerService.getPlayer(), services);
        }
        return saveData;
    }
    
    public SaveSlot createEmptySlot() {
        final Services services = new Services(this.npcService, this.playerService, this, this.endTurnService);
        final SaveSlot saveData = new SaveSlot(Boolean.valueOf(true), services);
        return saveData;
    }
    
    public List<SaveSlot> getSavedGames() {
        final Services services = new Services(this.npcService, this.playerService, this, this.endTurnService);
        final List<SaveSlot> saveSlots = new ArrayList<SaveSlot>();
        final File dir = new File(this.DIRECTORY_NAME);
        final String[] list = dir.list();
        for (int i = 0; i < list.length; ++i) {
            saveSlots.add(new SaveSlot(this.readSaveFile(list[i]), services));
        }
        return saveSlots;
    }
    
    public SaveData readSaveFile(final String path) {
        SaveData savedData = null;
        final File root = new File(this.PATH_ROOT);
        final File file = new File(this.PATH_ROOT + path);
        if (root.exists() && file.exists()) {
            try {
                this.logState("Loading file");
                final FileInputStream fileIn = new FileInputStream(file);
                final ObjectInputStream inputStream = new ObjectInputStream(fileIn);
                savedData = (SaveData)inputStream.readObject();
                this.processSavedData(savedData);
                inputStream.close();
                fileIn.close();
                this.logState("File loaded");
            }
            catch (IOException | ClassNotFoundException ex2) {
                final Exception ex;
                final Exception e = ex;
                this.logState("Failed to load! " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
        else {
            this.logState("Nothing to load.");
        }
        return savedData;
    }
    
    public void saveGame(final SaveSlot saveSlot) {
        final Task<Void> task = (Task<Void>)new SaveAndLoadUtility$1(this, saveSlot);
        final Thread thread = new Thread((Runnable)task);
        thread.setDaemon(true);
        thread.start();
    }
    
    private void processSavedData(final SaveData savedData) {
        this.logState("LOADED");
        this.logState("LOADED");
    }
    
    private String logState(final String log) {
        System.out.println("Game Saver: " + log);
        return log;
    }
}
