package com.example.Sim.Services;

import com.example.Sim.Model.Jobs.Task;
import com.example.Sim.Model.NPC.Npc;
import com.example.Sim.Model.TirednessSystem.WorkStatus;

public class ObedienceService {
    public static WorkStatus calculateIfRefuse(Npc npc) {
        Integer pcLove = npc.getStat("PCLove").getEffectiveValue();
        Integer obedience = npc.getStat("Obedience").getEffectiveValue();
        Integer value = obedience + 100 + (pcLove/2);
        //Do if obedient
        if(npc.getTask().getThreshold() < value)
        {
            return WorkStatus.NORMAL;
        }else{
            return WorkStatus.MORAL_REFUSE;
        }
    }
}
