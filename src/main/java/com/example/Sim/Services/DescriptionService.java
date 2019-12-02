package com.example.Sim.Services;

import com.example.Sim.Model.Jobs.Task;
import com.example.Sim.Model.NPC.Npc;
import com.example.Sim.Model.Raport.NpcRoot;
import com.example.Sim.Model.Raport.SingleEventRoot;
import com.example.Sim.Model.TirednessSystem.WorkStatus;

import java.util.*;

public class DescriptionService {

    private String description;

    public NpcRoot addNpcRootDescription(NpcRoot npcRoot, Npc npc) {
        String npcName = npcRoot.getName();
        WorkStatus workStatus = npcRoot.getWorkStatus();
        Task task = npc.getTask();

        if (workStatus == WorkStatus.DEAD_TIRED ) {
            npcRoot.setDescription(addOverworkDeathDescription(npcName));
            return npcRoot;
        }
        if (workStatus == WorkStatus.NORMAL || workStatus == WorkStatus.OVERWORKED || workStatus == WorkStatus.OVERWORKED_NEAR_DEATH) {
            npcRoot.setDescription(addWorkDescription(task, " day ", npcRoot, workStatus));
        } else {
            npcRoot.setDescription(addRefuseWorkDescription(npcRoot, workStatus, npcName));
        }


        addSingleRootNodeDescriptions(npcName, npcRoot.getShiftRapport());

        npcRoot.setDescription(addSkillGainsDescription(npcRoot));
        return npcRoot;
    }

    private String addRefuseWorkDescription(NpcRoot npcRoot, WorkStatus workStatus, String name) {
        description = npcRoot.getDescription();
        if (workStatus == WorkStatus.OVERWORK_REFUSE) {
            return description + "\n" + name + " refused to work this week due to overwork. To avoid this in the future raise her obedience. High love stat also helps.\n";
        }
        if (workStatus == WorkStatus.MORAL_REFUSE) {
            return description + "\n" + name + " refused to work this week the task was too repulsive for her. To avoid this in the future raise her obedience. \n";
        }
        return null;
    }

    private String addWorkDescription(Task task, String shift, NpcRoot npcRoot, WorkStatus workStatus) {
        description = npcRoot.getDescription();
        if (workStatus.equals(WorkStatus.OVERWORKED)) {
            description += "WARNING: " + npcRoot.getName() + " is overworked and loosing health.\n";
        }
        if (workStatus.equals(WorkStatus.OVERWORKED_NEAR_DEATH)) {
            description += "SEVERE: " + npcRoot.getName() + "is overworked and might die VERY soon.\n";
        }
        Integer moneyEarned = null;
        Integer expEarned = null;

        moneyEarned = npcRoot.getMoneyEarned();
        expEarned = npcRoot.getExpGain();



        description += "This week during the" + shift + npcRoot.getName() + " worked as a " + task.getName()
                + ". She earned a total of " + moneyEarned
                + " and gained " + expEarned + " EXP.\n";


        return description;
    }

    private String addOverworkDeathDescription(String name) {
        String response = "\n" + name + " has died this turn due to overwork!";

        return response;
    }

    private String addSkillGainsDescription(NpcRoot root) {
        Map<String, Integer> totalSkillGains = calculateTotalSkillGains(root);
        String name = root.getName();
        String description = root.getDescription();
        String response = "\n" + name + " gained the following skills this turn: \n";
        Iterator it = totalSkillGains.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            response += pair.getKey() + ": " + pair.getValue().toString() + "\n";
            it.remove(); // avoids a ConcurrentModificationException
        }
        return description + response;
    }

    private List<SingleEventRoot> addSingleRootNodeDescriptions(String name, List<SingleEventRoot> singleEventRoots) {
        singleEventRoots.forEach(singleEventRoot -> {
            if (singleEventRoot.getSkillsGain() != null) {
                singleEventRoot.setDescription(name + " has leveled up a skill: " + singleEventRoot.getSkill());
            }
        });
        return singleEventRoots;
    }

    private Map<String, Integer> calculateTotalSkillGains(NpcRoot npcRoot) {
        Map<String, Integer> totalSkillGains = null;
        totalSkillGains = calculateShiftSkillGains(npcRoot.getShiftRapport());
        return totalSkillGains;
    }

    private Map<String, Integer> calculateShiftSkillGains(List<SingleEventRoot> singleEventRootList) {
        List<String> skillGains = new ArrayList<>();
        singleEventRootList.forEach(singleEventRoot -> {
            if (singleEventRoot.getSkillsGain() != null) {
                skillGains.add(singleEventRoot.getSkillsGain());
            }
        });
        Set<String> distinct = new HashSet<>(skillGains);
        Map<String, Integer> skillLvlUpped = new HashMap<String, Integer>();
        distinct.forEach(skillGain -> skillLvlUpped.put(skillGain, Collections.frequency(skillGains, skillGain)));

        return skillLvlUpped;
    }

}