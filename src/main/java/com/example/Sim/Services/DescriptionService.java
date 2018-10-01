package com.example.Sim.Services;

import com.example.Sim.Model.Jobs.Task;
import com.example.Sim.Model.Npc;
import com.example.Sim.Model.Raport.NpcRoot;
import com.example.Sim.Model.Raport.SingleEventRoot;
import com.example.Sim.Model.TirednessSystem.WorkStatus;

import java.util.Iterator;
import java.util.Map;

public class DescriptionService {
    String description;

    public String createNpcRootDescription(WorkStatus workStatus, String name) {
        if (workStatus == WorkStatus.REFUSED) {
            return name + " refused to work this week. To avoid this in the future raise her obedience. She would also not refuse work is she was either too afraid of you or loved you too much.";
        }
        if (workStatus == WorkStatus.DEAD_TIRED) {
            return name + " died on the job today. She was overworked but didn't dare refuse the work, either due to her love,fear or obedience.";
        }
        return null;
    }

    public String createNpcRootDescription(Task task, Task nightTask, NpcRoot npcRoot, Map<String, Integer> totalSkillsGain, WorkStatus workStatus) {
        description = "";
        if(workStatus.equals(WorkStatus.OVERWORKED)){
            description += "WARNING: " + npcRoot.getName() + " is overworked and loosing health.\n";
        }
        if(workStatus.equals(WorkStatus.OVERWORKED_NEAR_DEATH)){
            description += "SEVERE: " + npcRoot.getName() + "is overworked and might die VERY soon.\n";
        }
        description += "This week during the day " + npcRoot.getName() + " worked as a " + task.getName()
                + ". She earned a total of " + npcRoot.getDayMoneyEarned()
                + " and gained " + npcRoot.getDayExpGain() + " EXP.";
        description += "\nDuring the night she worked as a " + nightTask.getName()
                + ". She earned a total of " + npcRoot.getNightMoneyEarned()
                + " and gained " + npcRoot.getNightExpGain() + " EXP.";
        if (!totalSkillsGain.isEmpty())
            description += createSkillGainsDescription(npcRoot.getName(), totalSkillsGain);
        return description;
    }

    public String createTaskDescription(Npc npc, Task task, SingleEventRoot singleEventRoot) {

        if (task == Task.BROTHEL_WHORE) {
            return createBrothelWhoreDescription(npc, task, singleEventRoot);
        } else if (task == Task.BROTHEL_STREET_WHORE) {
            return createStreetWhoreDescription(npc);
        } else if (task == Task.BROTHEL_STRIP) {
            return createBrothelStripperDescription(npc);
        } else if (task == Task.BROTHEL_BARMAID) {
            return createBrothelBarmaidDescription(npc);
        } else if (task == Task.BROTHEL_WAITRESS) {
            return createBrothelWaitressDescription(npc);
        } else {
            return "No Description";
        }
    }

    public String createSkillGainsDescription(String name, Map<String, Integer> totalSkillGains) {
        String response = "\n" + name + " gained the following skills this turn: \n";
        Iterator it = totalSkillGains.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            response += pair.getKey() + ": " + pair.getValue().toString() + "\n";
            it.remove(); // avoids a ConcurrentModificationException
        }
        return response;
    }

    public String createSingleRootNodeDescription(String name, SingleEventRoot singleEventRoot) {
        return description += name + " has leveled up a skill: " + singleEventRoot.getSkill();
    }

    private String createBrothelWhoreDescription(Npc npc, Task task, SingleEventRoot singleEventRoot) {
        String taskCategory = task.getType();

        return description;
    }

    private String createBrothelStripperDescription(Npc npc) {
        return null;
    }

    private String createStreetWhoreDescription(Npc npc) {
        return null;
    }

    private String createBrothelBarmaidDescription(Npc npc) {
        return null;
    }

    private String createBrothelWaitressDescription(Npc npc) {
        return null;
    }

    private String createFreeTimeDescription(Npc npc) {
        return null;
    }

}