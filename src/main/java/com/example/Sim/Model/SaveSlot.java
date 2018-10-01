package com.example.Sim.Model;


import com.example.Sim.Services.EndTurnService;
import com.example.Sim.Services.NpcService;
import com.example.Sim.Services.PlayerService;
import com.example.Sim.Utilities.SaveAndLoadUtility;
import com.example.Sim.Utilities.Services;
import javafx.scene.control.Button;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeansException;
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
public class SaveSlot implements ApplicationContextAware {

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

    public SaveSlot(String name, List<Npc> hiredNpcs, List<Npc> hirableNpcs, Player player, Services services) {
        inject(services);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd\nHH:mm:ss");
        this.saveData = new SaveData(name, sdf.format(cal.getTime()), endTurnService.getTurn(), npcService.getHired(), hiredNpcs, hirableNpcs, player);

    }

    public SaveSlot(List<Npc> hiredNpcs, List<Npc> hirableNpcs, Player player, Services services) {
        inject(services);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd\nHH:mm:ss");
        this.saveData = new SaveData(sdf.format(cal.getTime()), sdf.format(cal.getTime()), endTurnService.getTurn(), npcService.getHired(), hiredNpcs, hirableNpcs, player);

    }

    public SaveSlot(SaveData saveData, Services services) {
        inject(services);
        this.saveData = saveData;
    }

    public SaveSlot(Boolean empty, Services services) {
        inject(services);
        this.saveData = new SaveData("Empty Slot", "", 0, null, null, null, null);
    }

    public void inject(Services services) {
        this.npcService = services.getNpcService();
        this.playerService = services.getPlayerService();
        this.saveAndLoadUtility = services.getSaveAndLoadUtility();
        this.endTurnService = services.getEndTurnService();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
