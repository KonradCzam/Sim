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

    public String createPartialNpcRootDescription(WorkStatus workStatus, String name) {
        if (workStatus == WorkStatus.OVERWORK_REFUSE) {
            return name + " refused to work this week due to overwork. To avoid this in the future raise her obedience. She would also not refuse work is she was either too afraid of you or loved you too much.\n";
        }
        if (workStatus == WorkStatus.DEAD_TIRED) {
            return name + " died on the job today. She was overworked but didn't dare refuse the work, either due to her love,fear or obedience.\n";
        }
        if (workStatus == WorkStatus.MORAL_REFUSE) {
            return name + " refused to work this week the task was too repulsive for her. To avoid this in the future raise her obedience. She would also not refuse work is she was either too afraid of you or loved you too much. Or you could lower her morality.\n";
        }
        return null;
    }

    public String createPartialNpcRootDescription(Task task, String shift, NpcRoot npcRoot, Map<String, Integer> totalSkillsGain, WorkStatus workStatus) {
        description = "";
        if(workStatus.equals(WorkStatus.OVERWORKED)){
            description += "WARNING: " + npcRoot.getName() + " is overworked and loosing health.\n";
        }
        if(workStatus.equals(WorkStatus.OVERWORKED_NEAR_DEATH)){
            description += "SEVERE: " + npcRoot.getName() + "is overworked and might die VERY soon.\n";
        }
        Integer moneyEarned = null;
        Integer expEarned = null;
        if (" day ".equals(shift)){
            moneyEarned = npcRoot.getDayMoneyEarned();
            expEarned = npcRoot.getDayExpGain();
        }

        else{
            moneyEarned = npcRoot.getNightMoneyEarned();
            expEarned = npcRoot.getNightExpGain();
        }


        description += "This week during the"+ shift + npcRoot.getName() + " worked as a " + task.getName()
                + ". She earned a total of " + moneyEarned
                + " and gained " + expEarned + " EXP.\n";
        if (" night ".equals(shift)){
            if (!totalSkillsGain.isEmpty())
                description += createSkillGainsDescription(npcRoot.getName(), totalSkillsGain);
        }

        return description;
    }

    public String createTaskDescription(Npc npc, Task task, SingleEventRoot singleEventRoot) {

        if ("Brothel whore".equals(task.getName())) {
            return createBrothelWhoreDescription(npc, task, singleEventRoot);
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
        return  name + " has leveled up a skill: " + singleEventRoot.getSkill();
    }

    private String createBrothelWhoreDescription(Npc npc, Task task, SingleEventRoot singleEventRoot) {
        String taskCategory = task.getType();

        return null;
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