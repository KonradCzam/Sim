package com.example.Sim.Services;

import com.example.Sim.Model.Achievement;
import javafx.scene.control.Alert;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class AchievementService {
    Map<String, Achievement> achievementList = new HashMap<>();
    @Resource
    PlayerService playerService;
    @Resource
    NpcService npcService;
    @Resource
    EndTurnService endTurnService;
    public AchievementService() {
        achievementList.put("Player level I", new Achievement("Player level I", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Player level II", new Achievement("Player level II", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Player level III", new Achievement("Player level III", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Player skill I", new Achievement("Player skill I", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Player skill II", new Achievement("Player skill II", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Player skill III", new Achievement("Player skill III", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Player stat I", new Achievement("Player stat I", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Player stat II", new Achievement("Player stat II", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Player stat III", new Achievement("Player stat III", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Perfect Player I", new Achievement("Perfect Player I", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Perfect Player II", new Achievement("Perfect Player II", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Perfect Player III", new Achievement("Perfect Player III", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Slave level I", new Achievement("Slave level I", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Slave level II", new Achievement("Slave level II", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Slave level III", new Achievement("Slave level III", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Slave skill I", new Achievement("Slave skill I", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Slave skill II", new Achievement("Slave skill II", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Slave skill III", new Achievement("Slave skill III", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Slave skill master I", new Achievement("Slave skill master I", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Slave skill master II", new Achievement("Slave skill master II", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Slave skill master III", new Achievement("Slave skill master III", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Slave stat I", new Achievement("Slave stat I", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Slave stat II", new Achievement("Slave stat II", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Slave stat III", new Achievement("Slave stat III", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Slave master stat I", new Achievement("Slave master stat I", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Slave master stat II", new Achievement("Slave master stat II", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Slave master stat III", new Achievement("Slave master stat III", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Perfect slave I", new Achievement("Perfect slave I", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Perfect slave II", new Achievement("Perfect slave II", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Perfect slave III", new Achievement("Perfect slave III", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Brothel upgrades I", new Achievement("Brothel upgrades I", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Brothel upgrades II", new Achievement("Brothel upgrades II", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Brothel upgrades III", new Achievement("Brothel upgrades III", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Brothel gold I", new Achievement("Brothel gold I", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Brothel gold II", new Achievement("Brothel gold II", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Brothel gold III", new Achievement("Brothel gold III", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Gold per turn I", new Achievement("Gold per turn I", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Gold per turn II", new Achievement("Gold per turn II", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Gold per turn III", new Achievement("Gold per turn III", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Number of slaves I", new Achievement("Number of slaves I", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Number of slaves II", new Achievement("Number of slaves II", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
        achievementList.put("Number of slaves III", new Achievement("Number of slaves III", "", false, "/UI/AchievmentYes.png", "/UI/AchievmentNo.png"));
    }

    public void updateAchievments() {
        if (playerService.getStat("Level").getValue() > 30) {
            achievementList.get("Player level III").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! Player level III");
        } else if (playerService.getStat("Level").getValue() > 15) {
            achievementList.get("Player level II").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! Player level II");
        } else if (playerService.getStat("Level").getValue() > 5) {
            achievementList.get("Player level I").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! Player level I");
        }
        if (checkPlayerSkill(100)) {
            achievementList.get("Player skill III").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! Player skill III");
        } else if (checkPlayerSkill(75)) {
            achievementList.get("Player skill II").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! Player skill II");
        } else if (checkPlayerSkill(50)) {
            achievementList.get("Player skill I").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! Player skill I");
        }
        if (checkPlayerStat(100)) {
            achievementList.get("Player stat III").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! Player stat III");
        }
        else if (checkPlayerStat(75)) {
            achievementList.get("Player stat II").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! Player stat II");
        }
        else if (checkPlayerStat(50)) {
            achievementList.get("Player stat I").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! Player stat I ");
        }
        if (checkPlayerAll(100)) {
            achievementList.get("Perfect Player III").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! Perfect Player III");
        }
        else if (checkPlayerAll(75)) {
            achievementList.get("Perfect Player II").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! Perfect Player II");
        }
        else if (checkPlayerAll(50)) {
            achievementList.get("Perfect Player I").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! Perfect Player I");
        }
        if (checkSlavesLevels(30)) {
            achievementList.get("Slave level III").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! Slave level III");
        }
        else if (checkSlavesLevels(15)) {
            achievementList.get("Slave level II").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! Slave level II");
        }
        else if (checkSlavesLevels(5)) {
            achievementList.get("Slave level I").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! Slave level I" );
        }
        if (checkSlavesSkills(100)) {
            achievementList.get("Slave skill III").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! Slave skill III");
        }
        else if (checkSlavesSkills(75)) {
            achievementList.get("Slave skill II").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! Slave skill II");
        }
        else if (checkSlavesSkills(50)) {
            achievementList.get("Slave skill I").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! Slave skill I" );
        }
        if (checkSlavesStats(100)) {
            achievementList.get("Slave stat III").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! Slave stat III");
        }
        else if (checkSlavesStats(75)) {
            achievementList.get("Slave stat II").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! Slave stat II" );
        }
        else if (checkSlavesStats(50)) {
            achievementList.get("Slave stat I").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! Slave stat I" );
        }
        if (checkSlavesAllSkills(100)) {
            achievementList.get("Slave skill master III").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! ");
        }
        else if (checkSlavesAllSkills(75)) {
            achievementList.get("Slave skill master II").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! ");
        }
        else if (checkSlavesAllSkills(50)) {
            achievementList.get("Slave skill master I").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! ");
        }
        if (checkSlavesAllStats(100)) {
            achievementList.get("Slave master stat III").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! ");
        }
        if (checkSlavesAllStats(75)) {
            achievementList.get("Slave master stat II").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! ");
        }
        if (checkSlavesAllStats(50)) {
            achievementList.get("Slave master stat I").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! ");
        }
        if (checkSlavesPerfection(100)) {
            achievementList.get("Perfect slave III").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! ");
        }
        if (checkSlavesPerfection(75)) {
            achievementList.get("Perfect slave II").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! ");
        }
        if (checkSlavesPerfection(50)) {
            achievementList.get("Perfect slave I").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! ");
        }
        if (false) {
            achievementList.get("Brothel upgrades III").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! ");
        }
        if (false) {
            achievementList.get("Brothel upgrades II").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! ");
        }
        if (false) {
            achievementList.get("Brothel upgrades I").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! ");
        }
        if (playerService.getPlayerGold() > 1000000) {
            achievementList.get("Brothel gold III").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! ");
        }
        if (playerService.getPlayerGold() > 500000) {
            achievementList.get("Brothel gold II").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! ");
        }
        if (playerService.getPlayerGold() > 100000) {
            achievementList.get("Brothel gold I").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! ");
        }
        if (endTurnService.getEarnedLastTurn() > 50000) {
            achievementList.get("Gold per turn III").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! ");
        }
        if (endTurnService.getEarnedLastTurn() > 25000) {
            achievementList.get("Gold per turn II").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! ");
        }
        if (endTurnService.getEarnedLastTurn() > 5000) {
            achievementList.get("Gold per turn I").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! ");
        }
        if (npcService.getHiredNpcs().size() > 40) {
            achievementList.get("Number of slaves III").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! ");
        }
        if (npcService.getHiredNpcs().size() > 25) {
            achievementList.get("Number of slaves II").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! ");
        }
        if (npcService.getHiredNpcs().size() > 10) {
            achievementList.get("Number of slaves I").setFinished(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have gained an achievement! ");
        }

    }

    private boolean checkSlavesPerfection(int value) {
        AtomicReference<Boolean> response = new AtomicReference<>(true);
        npcService.getHiredNpcs().forEach(npc ->{
            npc.getStats().forEach((s, stat) -> {
                if(stat.getValue() < value){
                    response.set(false);
                }
            });
            response.set(true);
        });
        return response.get();
    }

    private boolean checkSlavesAllStats(int value) {
        AtomicReference<Boolean> response = new AtomicReference<>(true);
        npcService.getHiredNpcs().forEach(npc ->{
            npc.getStats().forEach((s, stat) -> {
                if(stat.getValue() < value){
                    response.set(false);
                }
            });
            response.set(true);
        });
        return response.get();
    }

    private boolean checkSlavesAllSkills(int value) {
        AtomicReference<Boolean> response = new AtomicReference<>(true);
        npcService.getHiredNpcs().forEach(npc ->{
            npc.getSkills().forEach((s, stat) -> {
                if(stat.getValue() < value){
                    response.set(false);
                }
            });
            response.set(true);
        });
        return response.get();
    }

    private boolean checkSlavesStats(int value) {
        AtomicReference<Boolean> response = new AtomicReference<>(false);
        npcService.getHiredNpcs().forEach(npc ->{
            npc.getStats().forEach((s, stat) -> {
                if(stat.getValue() > value){
                    response.set(true);
                }
            });
            response.set(true);
        });
        return response.get();
    }
    private boolean checkSlavesSkills(int value) {
        AtomicReference<Boolean> response = new AtomicReference<>(false);
        npcService.getHiredNpcs().forEach(npc ->{
            npc.getSkills().forEach((s, stat) -> {
                if(stat.getValue() > value){
                    response.set(true);
                }
            });
            response.set(true);
        });
        return response.get();
    }

    private boolean checkSlavesLevels(int value) {
        AtomicReference<Boolean> response = new AtomicReference<>(false);
        npcService.getHiredNpcs().forEach(npc ->{
            if(npc.getStat("Level").getValue() > value);
            response.set(true);
        });
        return response.get();
    }

    private boolean checkPlayerAll(int value) {
        AtomicReference<Boolean> response = new AtomicReference<>(true);
        playerService.getPlayer().getSkills().forEach((s, stat) -> {
            if (stat.getValue() < value) {
                response.set(false);
            }
        });
        return response.get();
    }

    private boolean checkPlayerStat(int value) {
        AtomicReference<Boolean> response = new AtomicReference<>(false);
        playerService.getPlayer().getSkills().forEach((s, stat) -> {
            if (stat.getValue() > value) {
                response.set(true);
            }
        });
        return response.get();
    }

    private boolean checkPlayerSkill(Integer value) {
        AtomicReference<Boolean> response = new AtomicReference<>(false);
        playerService.getPlayer().getStats().forEach((s, stat) -> {
            if (stat.getValue() > value) {
                response.set(true);
            }
        });
        return response.get();
    }

    public Map<String,Achievement> getAchievementList() {
        return achievementList;
    }
}
