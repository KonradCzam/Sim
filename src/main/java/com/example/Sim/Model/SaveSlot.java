package com.example.Sim.Model;


import com.example.Sim.Services.EndTurnService;
import com.example.Sim.Services.NpcService;
import com.example.Sim.Services.PlayerService;
import com.example.Sim.Utilities.SaveAndLoadUtility;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.WindowEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Getter
@NoArgsConstructor
@Component
@Scope("prototype")
public class SaveSlot  implements ApplicationContextAware {

    private static final long serialVersionUID = 1L;

    public Button saveButton;
    public Button loadButton;
    private SaveData saveData;
    ApplicationContext context;
    @Resource
    NpcService npcService;
    @Resource
    PlayerService playerService;
    @Resource
    SaveAndLoadUtility saveAndLoadUtility;
    @Resource
    EndTurnService endTurnService;

    public SaveSlot(NpcService npcService, PlayerService playerService, SaveAndLoadUtility saveAndLoadUtility, EndTurnService endTurnService) {
        this.npcService = npcService;
        this.playerService = playerService;
        this.saveAndLoadUtility = saveAndLoadUtility;
        this.endTurnService = endTurnService;
    }

    public SaveSlot(String name, List<Npc> hiredNpcs, List<Npc> hirableNpcs, Player player) {

        this.saveButton = new Button("Override");
        this.loadButton = new Button("Load Game");
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd\nHH:mm:ss");
        this.saveData = new SaveData(name,sdf.format(cal.getTime()),endTurnService.getTurn(),npcService.getHired(),hiredNpcs,hirableNpcs,player);

        this.saveButton.setOnAction(onSaveActionHandler);
        this.loadButton.setOnAction(onLoadActionHandler);
    }
    public SaveSlot(SaveData saveData) {

        this.saveButton = new Button("Override");
        this.loadButton = new Button("Load Game");
        this.saveData = saveData;
        this.saveButton.setOnAction(onSaveActionHandler);
        this.loadButton.setOnAction(onLoadActionHandler);
    }
    public SaveSlot(Boolean empty) {

        this.saveButton = new Button("Save Game");
        this.loadButton = new Button("Load Game");
        this.saveData = new SaveData("Empty Slot","",0,null,null,null,null);
        this.saveButton.setOnAction(onSaveActionHandler);
        this.loadButton.setDisable(true);
    }
    EventHandler<ActionEvent> onSaveActionHandler =
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    save();
                }
            };
    EventHandler<ActionEvent> onLoadActionHandler =
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    load();
                }
            };
    private void save(){
        saveAndLoadUtility.saveGame(saveAndLoadUtility.createSaveSlot());
        saveAndLoadUtility.getSavedGames();
    }

    private void load(){
        SaveData saveData = saveAndLoadUtility.readSaveFile(this.saveData.getName());
        playerService.setPlayer(saveData.getPlayer());
        npcService.setHirableNpcs(saveData.getHirableNpcs());
        npcService.setHiredNpcs(saveData.getHiredNpcs());
        npcService.setHired(saveData.getHired());
        endTurnService.setTurn(saveData.turn);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context=applicationContext;
    }
}
