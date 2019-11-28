// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.Services;

import com.example.Sim.Factors.FactorStatus;
import java.util.Set;
import java.util.Collections;
import java.util.HashMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import com.example.Sim.Model.Jobs.Task;
import com.example.Sim.Model.Raport.SingleEventRoot;
import java.util.List;
import com.example.Sim.Model.TirednessSystem.WorkStatus;
import com.example.Sim.Model.NPC.Npc;
import com.example.Sim.Model.Raport.NpcRoot;
import javax.annotation.Resource;

public class DescriptionService
{
    @Resource
    FactorService factorService;
    String description;
    
    public NpcRoot addNpcRootDescription(final NpcRoot npcRoot, final Npc npc) {
        final String npcName = npcRoot.getName();
        final WorkStatus dayWorkStatus = npcRoot.getDayWorkStatus();
        final WorkStatus nightWorkStatus = npcRoot.getNightWorkStatus();
        final Task dayTask = npc.getDayShift();
        final Task nightTask = npc.getNightShift();
        if (dayWorkStatus == WorkStatus.DEAD_TIRED || nightWorkStatus == WorkStatus.DEAD_TIRED) {
            npcRoot.setDescription(this.addOverworkDeathDescription(npcName));
            return npcRoot;
        }
        if (dayWorkStatus == WorkStatus.NORMAL || dayWorkStatus == WorkStatus.OVERWORKED || dayWorkStatus == WorkStatus.OVERWORKED_NEAR_DEATH) {
            npcRoot.setDescription(this.addWorkDescription(dayTask, " day ", npcRoot, dayWorkStatus));
        }
        else {
            npcRoot.setDescription(this.addRefuseWorkDescription(npcRoot, dayWorkStatus, npcName));
        }
        if (nightWorkStatus == WorkStatus.NORMAL || nightWorkStatus == WorkStatus.OVERWORKED || nightWorkStatus == WorkStatus.OVERWORKED_NEAR_DEATH) {
            npcRoot.setDescription(this.addWorkDescription(nightTask, " night ", npcRoot, nightWorkStatus));
        }
        else {
            npcRoot.setDescription(this.addRefuseWorkDescription(npcRoot, nightWorkStatus, npcName));
        }
        this.addSingleRootNodeDescriptions(npcName, npcRoot.getDayShiftRapport());
        this.addSingleRootNodeDescriptions(npcName, npcRoot.getNightShiftRapport());
        npcRoot.setDescription(this.addSkillGainsDescription(npcRoot));
        return npcRoot;
    }
    
    public String addRefuseWorkDescription(final NpcRoot npcRoot, final WorkStatus workStatus, final String name) {
        this.description = npcRoot.getDescription();
        if (workStatus == WorkStatus.OVERWORK_REFUSE) {
            return this.description + "\n" + name + " refused to work this week due to overwork. To avoid this in the future raise her obedience. High love stat also helps.\n";
        }
        if (workStatus == WorkStatus.MORAL_REFUSE) {
            return this.description + "\n" + name + " refused to work this week the task was too repulsive for her. To avoid this in the future raise her obedience. \n";
        }
        return null;
    }
    
    public String addWorkDescription(final Task task, final String shift, final NpcRoot npcRoot, final WorkStatus workStatus) {
        this.description = npcRoot.getDescription();
        if (workStatus.equals((Object)WorkStatus.OVERWORKED)) {
            this.description = this.description + "WARNING: " + npcRoot.getName() + " is overworked and loosing health.\n";
        }
        if (workStatus.equals((Object)WorkStatus.OVERWORKED_NEAR_DEATH)) {
            this.description = this.description + "SEVERE: " + npcRoot.getName() + "is overworked and might die VERY soon.\n";
        }
        Integer moneyEarned = null;
        Integer expEarned = null;
        if (" day ".equals(shift)) {
            moneyEarned = npcRoot.getDayMoneyEarned();
            expEarned = npcRoot.getDayExpGain();
        }
        else {
            moneyEarned = npcRoot.getNightMoneyEarned();
            expEarned = npcRoot.getNightExpGain();
        }
        return this.description = this.description + "This week during the" + shift + npcRoot.getName() + " worked as a " + task.getName() + ". She earned a total of " + moneyEarned + " and gained " + expEarned + " EXP.\n";
    }
    
    public String addOverworkDeathDescription(final String name) {
        final String response = "\n" + name + " has died this turn due to overwork!";
        return response;
    }
    
    public String addSkillGainsDescription(final NpcRoot root) {
        final Map<String, Integer> totalSkillGains = this.calculateTotalSkillGains(root);
        final String name = root.getName();
        final String description = root.getDescription();
        String response = "\n" + name + " gained the following skills this turn: \n";
        final Iterator it = totalSkillGains.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry pair = it.next();
            response = response + pair.getKey() + ": " + pair.getValue().toString() + "\n";
            it.remove();
        }
        return description + response;
    }
    
    public List<SingleEventRoot> addSingleRootNodeDescriptions(final String name, final List<SingleEventRoot> singleEventRoots) {
        singleEventRoots.forEach(singleEventRoot -> {
            if (singleEventRoot.getSkillsGain() != null) {
                singleEventRoot.setDescription(name + " has leveled up a skill: " + singleEventRoot.getSkill());
            }
            return;
        });
        return singleEventRoots;
    }
    
    private Map<String, Integer> calculateTotalSkillGains(final NpcRoot npcRoot) {
        Map<String, Integer> totalSkillGains = null;
        totalSkillGains = this.calculateShiftSkillGains(npcRoot.getDayShiftRapport());
        totalSkillGains.putAll(this.calculateShiftSkillGains(npcRoot.getNightShiftRapport()));
        return totalSkillGains;
    }
    
    private Map<String, Integer> calculateShiftSkillGains(final List<SingleEventRoot> singleEventRootList) {
        final List<String> skillGains = new ArrayList<String>();
        final List<String> list;
        singleEventRootList.forEach(singleEventRoot -> {
            if (singleEventRoot.getSkillsGain() != null) {
                list.add(singleEventRoot.getSkillsGain());
            }
            return;
        });
        final Set<String> distinct = new HashSet<String>(skillGains);
        final Map<String, Integer> skillLvlUpped = new HashMap<String, Integer>();
        final Integer n;
        distinct.forEach(skillGain -> n = skillLvlUpped.put(skillGain, Collections.frequency(skillGains, skillGain)));
        return skillLvlUpped;
    }
    
    public String genStatusDesc(final Npc currentNpc) {
        String description = "";
        for (final Map.Entry<String, Integer> entry : currentNpc.getFactors().entrySet()) {
            description += this.generateSingleFactorDesc(currentNpc, entry.getKey(), entry.getValue());
        }
        return description;
    }
    
    public String generateSingleFactorDesc(final Npc npc, final String factorId, final Integer factorLvl) {
        final FactorStatus factorEffects = this.factorService.checkFactorEffect(npc, factorId, factorLvl);
        String description = "\n\n" + npc.getName() + "'s " + factorId + " is set to " + factorEffects.getFactorLevelName() + " this will ";
        if (factorEffects.getLoveChange() == 0) {
            description += "have no effect on her Love";
        }
        else if (factorEffects.getLoveChange() > 0) {
            if (factorEffects.getLoveAxisStatus().contains("Love")) {
                description += "increase her Love";
            }
            else {
                description += "decrease her Fear";
            }
        }
        else if (factorEffects.getLoveAxisStatus().contains("Love")) {
            description += "decrease her Love";
        }
        else {
            description += "increase her Fear";
        }
        if (factorEffects.getLoveChange() == 0) {
            description += " and no effect on her Obedience";
        }
        else if (factorEffects.getObedienceChange() > 0) {
            if (factorEffects.getObedienceAxisStatus().contains("Obedience")) {
                description += " and increase her Obedience";
            }
            else {
                description += " and decrease her Rebelliousness";
            }
        }
        else if (factorEffects.getObedienceAxisStatus().contains("Obedience")) {
            description += " and decrease her Obedience.";
        }
        else {
            description += " and increase her Rebelliousness.";
        }
        return description;
    }
}
