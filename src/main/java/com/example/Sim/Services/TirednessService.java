package com.example.Sim.Services;

import com.example.Sim.Model.NPC.Npc;
import com.example.Sim.Model.TirednessSystem.WorkStatus;

import javax.annotation.Resource;

public class TirednessService {
@Resource
NpcService npcService;

    public WorkStatus handleTiredness(Npc npc) {
        Integer tempTiredness = npc.getStat("Tiredness").getEffectiveValue();
        Integer tirednessChange = calculateDeltaTiredness(npc);
        npc.getStat("Tiredness").changeValue(tirednessChange);
        tempTiredness += tirednessChange;
        if (tempTiredness >= 100) {
            return handleOverwork(npc, tempTiredness);
        }
        return WorkStatus.NORMAL;
    }

    private Integer calculateDeltaTiredness(Npc npc) {
        Integer change = 0;
        Integer constitution = npc.getStat("Constitution").getEffectiveValue();
        Integer tiring = npc.getTask().getTiring();
        change = tiring - constitution/10;
        return change;
    }

    private WorkStatus handleOverwork(Npc npc, Integer tempTiredness) {
        Integer overwork = tempTiredness - 100;
        Integer obedience = npc.getStat("Obedience").getEffectiveValue();
        Integer health = npc.getStat("Health").getEffectiveValue();
        if ((obedience ) > (100 - health)) {
            overwork = Math.min(40,overwork);
            npc.getStat("Health").changeValue(-overwork);
        }else {
            return WorkStatus.OVERWORK_REFUSE;
        }

        if (npc.getStat("Health").getEffectiveValue() <= 0) {
            npcService.killNpc(npc);
            return WorkStatus.DEAD_TIRED;
        } else {
            if(npc.getStat("Health").getEffectiveValue() <= 40){
                return WorkStatus.OVERWORKED_NEAR_DEATH;
            }
            return WorkStatus.OVERWORKED;
        }

    }
}
