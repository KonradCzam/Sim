package com.example.Sim.Services;

import com.example.Sim.Model.Jobs.Task;
import com.example.Sim.Model.Npc;
import com.example.Sim.Model.TirednessSystem.WorkStatus;

public class TirednessService {
    Task dayTask;
    Task nightTask;

    public WorkStatus handleTiredness(Npc npc) {
        dayTask = npc.getDayShift();
        nightTask = npc.getNightShift();
        npc.getStats().get("Tiredness").changeValue(calculateDeltaTiredness(npc));
        if (npc.getStat("Tiredness").getValue() >= 100) {
            return handleOverwork(npc);
        } else if(npc.getStat("Tiredness").getValue() < 0 ){
            npc.getStat("Tiredness").setMin();
        }
        return WorkStatus.NORMAL;
    }

    private Integer calculateDeltaTiredness(Npc npc) {
        Integer change = 0;
        Integer constitution = npc.getStats().get("Constitution").getValue();
        Integer tiring = ((dayTask.getTiring() + nightTask.getTiring()) * 20);

        change = tiring - constitution;

        return change;
    }

    private WorkStatus handleOverwork(Npc npc) {
        Integer overwork = npc.getStats().get("Tiredness").getValue() - 100;
        Integer obedience = npc.getStats().get("Obedience").getValue();
        Integer pcFear = npc.getStats().get("PCFear").getValue();
        Integer health = npc.getStats().get("Health").getValue();
        if ((obedience + pcFear) > (100-health)) {
            npc.getStats().get("Tiredness").setMax();
            overwork = Math.min(40,overwork);
            npc.getStats().get("Health").changeValue(-overwork);
        }else {
            npc.getStats().get("Tiredness").setMax();
            return WorkStatus.OVERWORK_REFUSE;
        }

        if (npc.getStats().get("Health").getValue() <= 0) {
            return WorkStatus.DEAD_TIRED;
        } else {
            if(npc.getStats().get("Health").getValue() <= 40){
                return WorkStatus.OVERWORKED_NEAR_DEATH;
            }
            return WorkStatus.OVERWORKED;
        }

    }
}
