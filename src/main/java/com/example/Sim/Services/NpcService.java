package com.example.Sim.Services;

import com.example.Sim.Exceptions.NpcCreationException;
import com.example.Sim.Model.NPC.Npc;
import com.example.Sim.Model.NPC.Trait;
import com.example.Sim.Utilities.FileUtility;
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
import java.util.Random;

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

    List<String> randomTemplates = new ArrayList();
    List<Npc> hiredNpcs = new ArrayList<Npc>();
    List<Npc> hirableNpcs = new ArrayList<Npc>();
    String npcName;
    String folderPresent;
    Npc currentNpc;
    List<String> randomNpcTemplatesString = new ArrayList<>();
    private Integer hired = 0;


    public void createNpcs() {
        files = fileUtility.getFileArray();
        for (int i = 0; i < files.length; i++) {
            if (files[i].endsWith(".girlsx")) {
                addToNormalList(files[i]);
            }
            if (files[i].endsWith(".rgirlsx")) {
                randomNpcTemplatesString.add(files[i]);
            }
        }
    }

    private List<Npc> createRandomHirableNpcs(Integer number) {
        List<Npc> randomNpcs = new ArrayList<Npc>();
        Random rand = new Random();
        for(int i =0 ; i<number;i++) {
            Integer random = rand.nextInt(randomNpcTemplatesString.size());
            String filePath = randomNpcTemplatesString.get(random);
            npcName = filePath.substring(0, filePath.length() - 8);
            Npc randomNpc = null;
            try {
                randomNpc = npcCreator.createRandomNpc(filePath);
            } catch (NpcCreationException e) {
                showAlert(e, filePath);
            }
            npcName = generateRandomName();
            randomNpc.setName(npcName);
            randomNpc.setFolder(checkIfFolderPresent(files, randomNpc.getPath()));
            randomNpc.setPrice(randomNpc.calculateValue());
            randomNpcs.add(randomNpc);
        }
        return randomNpcs;
    }

    private String generateRandomName() {
        String fullName;
        String name = fileUtility.getRandomLine("Data/Resources/RandomGirlNames.txt");
        String surname = fileUtility.getRandomLine("Data/Resources/RandomLastNames.txt");
        fullName = name + " " + surname;
        return fullName;
    }

    private void addToNormalList(String filePath) {
        npcName = filePath.substring(0, filePath.length() - 7);

        Npc npc = null;
        try {
            npc = npcCreator.createNpc(filePath);
        } catch (NpcCreationException e) {
            showAlert(e,filePath);
        }
        npc.setName(npcName);

        npc.setFolder(checkIfFolderPresent(files, npc.getPath()));
        npc.setPrice(npc.calculateValue());
        normalNpcs.add(npc);
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

    private String checkIfFolderPresent(String[] files, String girlName) {
        if (contains(files, girlName))
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
    private boolean contains(String[] array, String v) {
            for (String e : array)
                if (v != null && e.endsWith(v))
                    return true;
            return false;

    }

    public List<Npc> getHirableNpcsList(Integer quantity, Integer numberOfUniqueGirls) {
        hirableNpcs.clear();

        for(int i = 0; i<numberOfUniqueGirls;i++){
            Collections.shuffle(normalNpcs);
            hirableNpcs.add(normalNpcs.get(0));
        }

        hirableNpcs.addAll(createRandomHirableNpcs(quantity-numberOfUniqueGirls));

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
    public void sellNpc (String npcName){
        sellNpc(hiredNpcs.stream().filter(npc -> npc.getName().equals(npcName)).findAny().get());

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
