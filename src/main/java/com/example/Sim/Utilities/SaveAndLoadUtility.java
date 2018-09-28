package com.example.Sim.Utilities;


import java.io.*;
import java.util.ArrayList;
import java.util.List;


import com.example.Sim.Model.SaveData;
import com.example.Sim.Model.SaveSlot;
import com.example.Sim.Services.NpcService;
import com.example.Sim.Services.PlayerService;
import javafx.concurrent.Task;

import javax.annotation.Resource;

public class SaveAndLoadUtility {

    @Resource
    NpcService npcService;
    @Resource
    PlayerService playerService;

    private final String DIRECTORY_NAME = "Saves" + File.separator;

    /**
     * Directory where the game files are located!
     */
    private final String PATH_ROOT = "." + File.separator + DIRECTORY_NAME;
    private final String SAVE_FILE = "gameData.saveSlot";
    private final String SAVE_FILE_PATH = PATH_ROOT + SAVE_FILE;



    public SaveSlot createSaveSlot() {

        SaveSlot saveData = new SaveSlot("gameData.saveSlot", npcService.getHiredNpcs(), npcService.getHirableNpcs(), playerService.getPlayer());
        return saveData;

    }
    public SaveSlot createEmptySlot() {

        SaveSlot saveData = new SaveSlot(true);
        return saveData;

    }
    public List<SaveSlot> getSavedGames() {
        List<SaveSlot> saveSlots = new ArrayList<>();
        File dir = new File(DIRECTORY_NAME);
        String[] list = dir.list();
        for (int i = 0; i < list.length; i++) {
            saveSlots.add(new SaveSlot(readSaveFile(list[i])));
        }
        return saveSlots;
    }

    public SaveData readSaveFile(String path) {

        SaveData savedData = null;
        File root = new File(PATH_ROOT);
        File file = new File(PATH_ROOT + path);

        if (root.exists() && file.exists()) {
            try {
                logState("Loading file");

                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream inputStream = new ObjectInputStream(fileIn);

                savedData = (SaveData) inputStream.readObject();


                inputStream.close();
                fileIn.close();

                logState("File loaded");


            } catch (IOException | ClassNotFoundException e) {
                logState("Failed to load! " + e.getLocalizedMessage());
            }
        } else {
            logState("Nothing to load.");
        }

        return savedData;
    }

    /**
     * Method which when called will attempt to save a SaveSlot
     * to a specified directory.
     *
     * @param saveSlot
     */

    public void saveGame(SaveSlot saveSlot) {
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws Exception {

                File root = new File(PATH_ROOT);
                File file = new File(SAVE_FILE_PATH);

                file.delete();
                logState("Saving file...");
                try {
                    root.mkdirs();

                    FileOutputStream fileOut = new FileOutputStream(SAVE_FILE_PATH);
                    BufferedOutputStream bufferedStream = new BufferedOutputStream(fileOut);
                    ObjectOutputStream outputStream = new ObjectOutputStream(bufferedStream);

                    outputStream.writeObject(saveSlot.getSaveData());
                    outputStream.close();
                    fileOut.close();

                    logState("File saved.");

                } catch (IOException e) {
                    logState("Failed to save, I/O exception");
                    e.printStackTrace();
                }
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Method which when called attempts to retrieve the saved data
     * from a specified directory
     */


    private void processSavedData(SaveData savedData) {
        /*SaveData.profileName = savedData.getProfileName();
        SaveData.score = savedData.getScore();
        SaveData.level = savedData.getLevel();
        SaveData.gameVolume = savedData.getGameVolume();
        SaveData.resolutionX = savedData.getResolutionX();
        SaveData.resolutionY = savedData.getResolutionY();*/
        logState("LOADED");
        logState("LOADED");
    }

    private String logState(String log) {
        System.out.println("Game Saver: " + log);
        return log;
    }


}

