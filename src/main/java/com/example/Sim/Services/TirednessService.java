package com.example.Sim.Services;

import com.example.Sim.Model.Jobs.Task;
import com.example.Sim.Model.NPC.Npc;
import com.example.Sim.Model.TirednessSystem.WorkStatus;

public class TirednessService {


    public WorkStatus handleTiredness(Npc npc, Task task) {
        Integer tempTiredness = npc.getStat("Tiredness").getValue();
        Integer tirednessChange = calculateDeltaTiredness(npc,task);
        npc.getStat("Tiredness").changeValue(tirednessChange);
        tempTiredness += tirednessChange;
        if (tempTiredness >= 100) {
            return handleOverwork(npc, tempTiredness);
        }
        return WorkStatus.NORMAL;
    }

    private Integer calculateDeltaTiredness(Npc npc, Task task) {
        Integer change = 0;
        Integer constitution = npc.getStat("Constitution").getValue();
        Integer tiring = task.getTiring();
        change = tiring - constitution/10;
        return change;
    }

    private WorkStatus handleOverwork(Npc npc, Integer tempTiredness) {
        Integer overwork = tempTiredness - 100;
        Integer obedience = npc.getStat("Obedience").getValue();
        Integer health = npc.getStat("Health").getValue();
        if ((obedience ) > (100-health)) {
            overwork = Math.min(40,overwork);
            npc.getStat("Health").changeValue(-overwork);
        }else {
            return WorkStatus.OVERWORK_REFUSE;
        }

        if (npc.getStat("Health").getValue() <= 0) {
            return WorkStatus.DEAD_TIRED;
        } else {
            if(npc.getStat("Health").getValue() <= 40){
                return WorkStatus.OVERWORKED_NEAR_DEATH;
            }
            return WorkStatus.OVERWORKED;
        }

    }
}
