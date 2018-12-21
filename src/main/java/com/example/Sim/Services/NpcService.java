package com.example.Sim.Services;

import com.example.Sim.Model.NPC.Npc;
import com.example.Sim.Model.NPC.Trait;
import com.example.Sim.Utilities.FileUtility;
import com.example.Sim.Exceptions.NpcCreationException;
import com.example.Sim.Utilities.NpcCreator;
import com.example.Sim.controllers.Gallery.model.TableNpc;
import javafx.scene.control.Alert;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor
@Service
public class NpcService {

    @Resource
    NpcCreator npcCreator;
    @Resource
    FileUtility fileUtility;
    @Resource
    private transient PlayerService playerService;
    @Resource
    private transient JobService jobService;

    String[] files;
    List<Npc> normalNpcs = new ArrayList<Npc>();
    List<Npc> randomNpcs = new ArrayList<Npc>();
    List<String> randomTemplates = new ArrayList();
    List<Npc> hiredNpcs = new ArrayList<Npc>();
    List<Npc> hirableNpcs = new ArrayList<Npc>();
    String npcName;
    String folderPresent;
    Npc currentNpc;
    private Integer hired = 0;

    public void createNpcs() {
        normalNpcs = new ArrayList<Npc>();
        randomNpcs = new ArrayList<Npc>();
        files = fileUtility.getFileArray();
        for (int i = 0; i < files.length; i++) {
            if (files[i].endsWith(".girlsx")) {
                addToNormalList(files[i]);
            }
            if (files[i].endsWith(".rgirlsx")) {
                addToTemplateList(files[i]);

            }
        }
    }

    private void addToTemplateList(String filePath) {
        npcName = filePath.substring(0, filePath.length() - 8);
        folderPresent = setFolder(files, npcName, true);
        Npc randomNpc = null;
        try {
            randomNpc = npcCreator.createRandomNpc(filePath);
        } catch (NpcCreationException e) {
            showAlert(e,filePath);
        }
        randomNpc.setName(npcName);
        randomNpc.setFolder(folderPresent);
        randomNpc.setPrice(randomNpc.calculateValue());
        randomNpcs.add(randomNpc);
    }

    private void addToNormalList(String filePath) {
        npcName = filePath.substring(0, filePath.length() - 7);
        folderPresent = setFolder(files, npcName, true);
        Npc npc = null;
        try {
            npc = npcCreator.createNpc(filePath);
        } catch (NpcCreationException e) {
            showAlert(e,filePath);
        }
        npc.setName(npcName);
        npc.setFolder(folderPresent);
        npc.setPrice(npc.calculateValue());
        normalNpcs.add(npc);
    }

    private void addToRandomList(String filePath,String npcName) {
        folderPresent = setFolder(files, npcName, true);
        Npc randomNpc = null;
        try {
            randomNpc = npcCreator.createRandomNpc(filePath);
        } catch (NpcCreationException e) {
            showAlert(e,filePath);
        }
        randomNpc.setName(npcName);
        randomNpc.setFolder(folderPresent);
        randomNpc.setPrice(randomNpc.calculateValue());
        randomNpcs.add(randomNpc);

    }

    public List<TableNpc> getNormalTableNpcs() {
        List<TableNpc> tableNpcs = new ArrayList<TableNpc>();
        String girlPath;
        for (int i = 0; i < normalNpcs.size(); i++) {
            npcName = normalNpcs.get(i).getName();
            folderPresent = normalNpcs.get(i).getFolder();
            girlPath = normalNpcs.get(i).getPath();
            TableNpc tableNpc = new TableNpc(npcName, girlPath, folderPresent);
            tableNpcs.add(tableNpc);
        }
        return tableNpcs;
    }

    public List<Trait> getTrait(String nodename) {
       return npcCreator.getTrait(nodename);
    }

    private String setFolder(String[] files, String girlName, Boolean random) {
        if (contains(files, girlName, true))
            return "Present";
        else
            return "Missing";
    }
    public void showAlert(NpcCreationException e, String filePath){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Failed to create npc with name: "
                + npcName +
                " and filepath: " + filePath
                + "\n\nThe error was: " + e.getMessage());
        alert.showAndWait();
        e.printStackTrace();
    }
    private boolean contains(String[] array, String v, Boolean random) {
        if (random) {
            for (String e : array)
                if (v != null && v.startsWith(e))
                    return true;
            return false;
        } else {
            for (String e : array)
                if (v != null && v.endsWith(e))
                    return true;
            return false;
        }
    }

    public List<Npc> getHirableNpcsList(Integer quantity) {

        if (hirableNpcs.isEmpty()) {
            hirableNpcs.addAll(normalNpcs);
            hirableNpcs.addAll(randomNpcs);
            shuffleHirable();
        }
        return hirableNpcs.subList(0, quantity - hired);
    }

    public void hireNpc(Npc npc) {
        hiredNpcs.add(npc);
        hirableNpcs.remove(npc);
        hired += 1;
    }
    public void hireNpcNoHiredChange(Npc npc) {
        hiredNpcs.add(npc);
        hirableNpcs.remove(npc);
    }
    public void hireNpcs(List<Npc> npcs) {
        npcs.forEach(npc -> hireNpc(npc));
    }

    public void killNpc(Npc npc) {
        hiredNpcs.remove(npc);
    }
    public void sellNpcs(List<Npc> npcList){
        npcList.forEach(npc -> sellNpc(npc));
    }
    public void sellNpc (Npc npc){
        hiredNpcs.remove(npc);
        hirableNpcs.add(npc);
        playerService.changeGold(npc.calculateValue());
    }
    public void shuffleHirable() {
        Collections.shuffle(hirableNpcs);
    }
    public void setCurrentNpc(Npc npc) {
        currentNpc = npc;
    }
    public void setHired(Integer hired) {
        this.hired = hired;
    }

    public void setHiredNpcs(List<Npc> hiredNpcs) {
        this.hiredNpcs = hiredNpcs;
    }

    public void setHirableNpcs(List<Npc> hirableNpcs) {
        this.hirableNpcs = hirableNpcs;
    }
}
