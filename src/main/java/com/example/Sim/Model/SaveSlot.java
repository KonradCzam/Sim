// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.Model;

import org.springframework.beans.BeansException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.example.Sim.Utilities.Services;
import com.example.Sim.Model.NPC.Npc;
import java.util.List;
import com.example.Sim.Services.EndTurnService;
import com.example.Sim.Utilities.SaveAndLoadUtility;
import com.example.Sim.Services.PlayerService;
import javax.annotation.Resource;
import com.example.Sim.Services.NpcService;
import org.springframework.context.ApplicationContext;
import javafx.scene.control.Button;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationContextAware;

@Component
@Scope("prototype")
public class SaveSlot implements ApplicationContextAware
{
    private static final long serialVersionUID = 1L;
    public Button saveButton;
    public Button loadButton;
    ApplicationContext context;
    @Resource
    NpcService npcService;
    @Resource
    PlayerService playerService;
    @Resource
    SaveAndLoadUtility saveAndLoadUtility;
    @Resource
    EndTurnService endTurnService;
    private SaveData saveData;
    private Integer index;
    
    public SaveSlot(final String name, final List<Npc> hiredNpcs, final List<Npc> hirableNpcs, final Player player, final Services services) {
        this.inject(services);
        final Calendar cal = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd\nHH:mm:ss");
        this.saveData = new SaveData(name, sdf.format(cal.getTime()), this.endTurnService.getTurn(), this.npcService.getHired(), (List)hiredNpcs, (List)hirableNpcs, player);
    }
    
    public SaveSlot(final List<Npc> hiredNpcs, final List<Npc> hirableNpcs, final Player player, final Services services) {
        this.inject(services);
        final Calendar cal = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd\nHH:mm:ss");
        this.saveData = new SaveData(sdf.format(cal.getTime()), sdf.format(cal.getTime()), this.endTurnService.getTurn(), this.npcService.getHired(), (List)hiredNpcs, (List)hirableNpcs, player);
    }
    
    public SaveSlot(final SaveData saveData, final Services services) {
        this.inject(services);
        this.saveData = saveData;
    }
    
    public SaveSlot(final Boolean empty, final Services services) {
        this.inject(services);
        this.saveData = new SaveData("Empty Slot", "", Integer.valueOf(0), (Integer)null, (List)null, (List)null, (Player)null);
    }
    
    public void inject(final Services services) {
        this.npcService = services.getNpcService();
        this.playerService = services.getPlayerService();
        this.saveAndLoadUtility = services.getSaveAndLoadUtility();
        this.endTurnService = services.getEndTurnService();
    }
    
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
    
    public Integer getIndex() {
        return this.index;
    }
    
    public void setIndex(final Integer index) {
        this.index = index;
    }
    
    public Button getSaveButton() {
        return this.saveButton;
    }
    
    public Button getLoadButton() {
        return this.loadButton;
    }
    
    public ApplicationContext getContext() {
        return this.context;
    }
    
    public NpcService getNpcService() {
        return this.npcService;
    }
    
    public PlayerService getPlayerService() {
        return this.playerService;
    }
    
    public SaveAndLoadUtility getSaveAndLoadUtility() {
        return this.saveAndLoadUtility;
    }
    
    public EndTurnService getEndTurnService() {
        return this.endTurnService;
    }
    
    public SaveData getSaveData() {
        return this.saveData;
    }
    
    public SaveSlot() {
    }
}
