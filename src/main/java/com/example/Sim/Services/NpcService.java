// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.Services;

import java.util.Collections;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import com.example.Sim.Model.NPC.Trait;
import com.example.Sim.controllers.Gallery.model.TableNpc;
import com.example.Sim.Exceptions.NpcCreationException;
import java.util.ArrayList;
import com.example.Sim.Model.NPC.Npc;
import java.util.List;
import com.example.Sim.Utilities.FileUtility;
import javax.annotation.Resource;
import com.example.Sim.Utilities.NpcCreator;
import org.springframework.stereotype.Service;

@Service
public class NpcService
{
    @Resource
    NpcCreator npcCreator;
    @Resource
    FileUtility fileUtility;
    @Resource
    private transient PlayerService playerService;
    @Resource
    private transient JobService jobService;
    String[] files;
    List<Npc> normalNpcs;
    List<Npc> randomNpcs;
    List<String> randomTemplates;
    List<Npc> hiredNpcs;
    List<Npc> hirableNpcs;
    String npcName;
    String folderPresent;
    Npc currentNpc;
    private Integer hired;
    
    public void createNpcs() {
        this.normalNpcs = new ArrayList();
        this.randomNpcs = new ArrayList();
        this.files = this.fileUtility.getFileArray();
        for (int i = 0; i < this.files.length; ++i) {
            if (this.files[i].endsWith(".girlsx")) {
                this.addToNormalList(this.files[i]);
            }
            if (this.files[i].endsWith(".rgirlsx")) {
                this.addToTemplateList(this.files[i]);
            }
        }
    }
    
    private void addToTemplateList(final String filePath) {
        this.npcName = filePath.substring(0, filePath.length() - 8);
        this.folderPresent = this.setFolder(this.files, this.npcName, true);
        Npc randomNpc = null;
        try {
            randomNpc = this.npcCreator.createRandomNpc(filePath);
        }
        catch (NpcCreationException e) {
            this.showAlert(e, filePath);
        }
        randomNpc.setName(this.npcName);
        randomNpc.setFolder(this.folderPresent);
        randomNpc.setPrice(randomNpc.calculateValue());
        this.randomNpcs.add(randomNpc);
    }
    
    private void addToNormalList(final String filePath) {
        this.npcName = filePath.substring(0, filePath.length() - 7);
        this.folderPresent = this.setFolder(this.files, this.npcName, true);
        Npc npc = null;
        try {
            npc = this.npcCreator.createNpc(filePath);
        }
        catch (NpcCreationException e) {
            this.showAlert(e, filePath);
        }
        npc.setName(this.npcName);
        npc.setFolder(this.folderPresent);
        npc.setPrice(npc.calculateValue());
        this.normalNpcs.add(npc);
    }
    
    private void addToRandomList(final String filePath, final String npcName) {
        this.folderPresent = this.setFolder(this.files, npcName, true);
        Npc randomNpc = null;
        try {
            randomNpc = this.npcCreator.createRandomNpc(filePath);
        }
        catch (NpcCreationException e) {
            this.showAlert(e, filePath);
        }
        randomNpc.setName(npcName);
        randomNpc.setFolder(this.folderPresent);
        randomNpc.setPrice(randomNpc.calculateValue());
        this.randomNpcs.add(randomNpc);
    }
    
    public List<TableNpc> getNormalTableNpcs() {
        final List<TableNpc> tableNpcs = new ArrayList<TableNpc>();
        for (int i = 0; i < this.normalNpcs.size(); ++i) {
            this.npcName = this.normalNpcs.get(i).getName();
            this.folderPresent = this.normalNpcs.get(i).getFolder();
            final String girlPath = this.normalNpcs.get(i).getPath();
            final TableNpc tableNpc = new TableNpc(this.npcName, girlPath, this.folderPresent);
            tableNpcs.add(tableNpc);
        }
        return tableNpcs;
    }
    
    public List<Trait> getTrait(final String nodename) {
        return (List<Trait>)this.npcCreator.getTrait(nodename);
    }
    
    private String setFolder(final String[] files, final String girlName, final Boolean random) {
        if (this.contains(files, girlName, true)) {
            return "Present";
        }
        return "Missing";
    }
    
    public void showAlert(final NpcCreationException e, final String filePath) {
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Failed to create npc with name: " + this.npcName + " and filepath: " + filePath + "\n\nThe error was: " + e.getMessage(), new ButtonType[0]);
        alert.showAndWait();
        e.printStackTrace();
    }
    
    private boolean contains(final String[] array, final String v, final Boolean random) {
        if (random) {
            for (final String e : array) {
                if (v != null && v.startsWith(e)) {
                    return true;
                }
            }
            return false;
        }
        for (final String e : array) {
            if (v != null && v.endsWith(e)) {
                return true;
            }
        }
        return false;
    }
    
    public List<Npc> getHirableNpcsList(final Integer quantity) {
        if (this.hirableNpcs.isEmpty()) {
            this.hirableNpcs.addAll(this.normalNpcs);
            this.hirableNpcs.addAll(this.randomNpcs);
            this.shuffleHirable();
        }
        return this.hirableNpcs.subList(0, quantity - this.hired);
    }
    
    public void hireNpc(final Npc npc) {
        this.hiredNpcs.add(npc);
        this.hirableNpcs.remove(npc);
        ++this.hired;
    }
    
    public void hireNpcNoHiredChange(final Npc npc) {
        this.hiredNpcs.add(npc);
        this.hirableNpcs.remove(npc);
    }
    
    public void hireNpcs(final List<Npc> npcs) {
        npcs.forEach(this::lambda$hireNpcs$0);
    }
    
    public void killNpc(final Npc npc) {
        this.hiredNpcs.remove(npc);
    }
    
    public void sellNpcs(final List<Npc> npcList) {
        npcList.forEach(this::lambda$sellNpcs$1);
    }
    
    public void sellNpc(final Npc npc) {
        this.hiredNpcs.remove(npc);
        this.hirableNpcs.add(npc);
        this.playerService.changeGold(npc.calculateValue());
    }
    
    public void shuffleHirable() {
        Collections.shuffle(this.hirableNpcs);
    }
    
    public void setCurrentNpc(final Npc npc) {
        this.currentNpc = npc;
    }
    
    public void setHired(final Integer hired) {
        this.hired = hired;
    }
    
    public void setHiredNpcs(final List<Npc> hiredNpcs) {
        this.hiredNpcs = hiredNpcs;
    }
    
    public void setHirableNpcs(final List<Npc> hirableNpcs) {
        this.hirableNpcs = hirableNpcs;
    }
    
    public NpcCreator getNpcCreator() {
        return this.npcCreator;
    }
    
    public FileUtility getFileUtility() {
        return this.fileUtility;
    }
    
    public PlayerService getPlayerService() {
        return this.playerService;
    }
    
    public JobService getJobService() {
        return this.jobService;
    }
    
    public String[] getFiles() {
        return this.files;
    }
    
    public List<Npc> getNormalNpcs() {
        return (List<Npc>)this.normalNpcs;
    }
    
    public List<Npc> getRandomNpcs() {
        return (List<Npc>)this.randomNpcs;
    }
    
    public List<String> getRandomTemplates() {
        return (List<String>)this.randomTemplates;
    }
    
    public List<Npc> getHiredNpcs() {
        return (List<Npc>)this.hiredNpcs;
    }
    
    public List<Npc> getHirableNpcs() {
        return (List<Npc>)this.hirableNpcs;
    }
    
    public String getNpcName() {
        return this.npcName;
    }
    
    public String getFolderPresent() {
        return this.folderPresent;
    }
    
    public Npc getCurrentNpc() {
        return this.currentNpc;
    }
    
    public Integer getHired() {
        return this.hired;
    }
    
    public NpcService() {
        this.normalNpcs = new ArrayList();
        this.randomNpcs = new ArrayList();
        this.randomTemplates = new ArrayList();
        this.hiredNpcs = new ArrayList();
        this.hirableNpcs = new ArrayList();
        this.hired = 0;
    }
}
