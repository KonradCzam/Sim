package com.example.Sim.Services;

import com.example.Sim.Model.Jobs.Task;
import com.example.Sim.Model.Npc;
import com.example.Sim.Utilities.FileUtility;
import com.example.Sim.Utilities.NpcCreator;
import com.example.Sim.controllers.Gallery.model.TableNpc;
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
    private transient JobService jobService;

    String[] files;
    List<Npc> normalNpcs = new ArrayList<Npc>();
    List<Npc> randomNpcs = new ArrayList<Npc>();
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
                addToRandomList(files[i]);
            }
        }
    }

    private void addToNormalList(String filePath) {
        npcName = filePath.substring(0, filePath.length() - 7);
        folderPresent = setFolder(files, npcName, true);
        Npc npc = npcCreator.createNpc(filePath);
        npc.setName(npcName);
        npc.setFolder(folderPresent);
        npc.setPrice(calculateValue(npc));
        normalNpcs.add(npc);
    }

    private void addToRandomList(String filePath) {
        npcName = filePath.substring(0, filePath.length() - 8);
        folderPresent = setFolder(files, npcName, true);
        Npc randomNpc = npcCreator.createRandomNpc(filePath);
        randomNpc.setName(npcName);
        randomNpc.setFolder(folderPresent);
        randomNpc.setPrice(calculateValue(randomNpc));
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

    public List<TableNpc> getRandomTableNpcs() {
        List<TableNpc> tableNpcs = new ArrayList<TableNpc>();
        String girlPath;
        for (int i = 0; i < randomNpcs.size(); i++) {
            npcName = randomNpcs.get(i).getName();
            folderPresent = randomNpcs.get(i).getFolder();
            girlPath = randomNpcs.get(i).getPath();
            TableNpc tableNpc = new TableNpc(npcName, girlPath, folderPresent);
            tableNpcs.add(tableNpc);
        }
        return tableNpcs;
    }

    private String setFolder(String[] files, String girlName, Boolean random) {
        if (contains(files, girlName, true))
            return "Present";
        else
            return "Missing";
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

    public void setCurrentNpc(Npc npc) {
        currentNpc = npc;
    }

    public List<Npc> getHirableNpcsList(Integer quantity) {

        if (hirableNpcs.isEmpty()) {
            hirableNpcs.addAll(normalNpcs);
            hirableNpcs.addAll(randomNpcs);
        }
        return hirableNpcs.subList(0, quantity - hired);
    }

    public Integer calculateValue(Npc npc) {
        Double avgPerformance = jobService.calculateTaskPerformance(npc, npc.getNightShift());
        Double price = (avgPerformance * 5);
        return price.intValue();
    }

    public void hireNpc(Npc npc) {
        hiredNpcs.add(npc);
        hirableNpcs.remove(npc);
        hired += 1;
    }

    public void hireNpc(List<Npc> npcs) {
        npcs.forEach(npc -> hireNpc(npc));
    }

    public void fireNpc(Npc npc) {
        hiredNpcs.remove(npc);
        hirableNpcs.add(npc);
    }

    public void shuffleHirable() {
        Collections.shuffle(hirableNpcs);
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
