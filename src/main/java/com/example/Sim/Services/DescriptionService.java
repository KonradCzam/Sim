package com.example.Sim.Services;

import com.example.Sim.Factors.FactorStatus;
import com.example.Sim.Model.Jobs.Task;
import com.example.Sim.Model.NPC.Npc;
import com.example.Sim.Model.Raport.NpcRoot;
import com.example.Sim.Model.Raport.SingleEventRoot;
import com.example.Sim.Model.TirednessSystem.WorkStatus;

import javax.annotation.Resource;
import java.util.*;

public class DescriptionService {
    @Resource
    private
    FactorService factorService;
    private String description;

    public NpcRoot addNpcRootDescription(NpcRoot npcRoot, Npc npc) {
        String npcName = npcRoot.getName();
        WorkStatus dayWorkStatus = npcRoot.getDayWorkStatus();
        WorkStatus nightWorkStatus = npcRoot.getNightWorkStatus();
        Task dayTask = npc.getDayShift();
        Task nightTask = npc.getNightShift();

        if (dayWorkStatus == WorkStatus.DEAD_TIRED || nightWorkStatus == WorkStatus.DEAD_TIRED) {
            npcRoot.setDescription(addOverworkDeathDescription(npcName));
            return npcRoot;
        }
        if (dayWorkStatus == WorkStatus.NORMAL || dayWorkStatus == WorkStatus.OVERWORKED || dayWorkStatus == WorkStatus.OVERWORKED_NEAR_DEATH) {
            npcRoot.setDescription(addWorkDescription(dayTask, " day ", npcRoot, dayWorkStatus));
        } else {
            npcRoot.setDescription(addRefuseWorkDescription(npcRoot, dayWorkStatus, npcName));
        }

        if (nightWorkStatus == WorkStatus.NORMAL || nightWorkStatus == WorkStatus.OVERWORKED || nightWorkStatus == WorkStatus.OVERWORKED_NEAR_DEATH) {
            npcRoot.setDescription(addWorkDescription(nightTask, " night ", npcRoot, nightWorkStatus));
        } else {
            npcRoot.setDescription(addRefuseWorkDescription(npcRoot, nightWorkStatus, npcName));
        }

        addSingleRootNodeDescriptions(npcName, npcRoot.getDayShiftRapport());

        addSingleRootNodeDescriptions(npcName, npcRoot.getNightShiftRapport());

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
        if (" day ".equals(shift)) {
            moneyEarned = npcRoot.getDayMoneyEarned();
            expEarned = npcRoot.getDayExpGain();
        } else {
            moneyEarned = npcRoot.getNightMoneyEarned();
            expEarned = npcRoot.getNightExpGain();
        }


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
        totalSkillGains = calculateShiftSkillGains(npcRoot.getDayShiftRapport());
        totalSkillGains.putAll(calculateShiftSkillGains(npcRoot.getNightShiftRapport()));
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


    public String genStatusDesc(Npc currentNpc) {
        String description = "";
        for (Map.Entry<String, Integer> entry : currentNpc.getFactors().entrySet()) {
            description += generateSingleFactorDesc(currentNpc, entry.getKey(), entry.getValue());
        }
        return description;
    }

    private String generateSingleFactorDesc(Npc npc, String factorId, Integer factorLvl) {
        FactorStatus factorEffects = factorService.checkFactorEffect(npc, factorId, factorLvl);
        String description = "\n\n" + npc.getName() + "'s " + factorId + " is set to " + factorEffects.getFactorLevelName() + " this will ";


        if (factorEffects.getLoveChange() == 0) {
            description += "have no effect on her Love";
        } else if (factorEffects.getLoveChange() > 0) {
            if (npc.getStat("PCLove").getEffectiveValue() > 0) {
                description += "increase her Love";
            } else {
                description += "decrease her Fear";
            }
        } else {
            if (npc.getStat("PCLove").getEffectiveValue() > 0) {
                description += "decrease her Love";
            } else {
                description += "increase her Fear";
            }
        }
        if (factorEffects.getLoveChange() == 0) {
            description += " and no effect on her Obedience";
        } else if (factorEffects.getObedienceChange() > 0) {
            if (npc.getStat("Obedience").getEffectiveValue() > 0) {
                description += " and increase her Obedience";
            } else {
                description += " and decrease her Rebelliousness";
            }
        } else {
            if (npc.getStat("Obedience").getEffectiveValue() > 0) {
                description += " and decrease her Obedience.";
            } else {
                description += " and increase her Rebelliousness.";
            }
        }
        return description;
    }
}