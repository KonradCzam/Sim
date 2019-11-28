// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.Services;

import com.example.Sim.Model.TirednessSystem.WorkStatus;
import com.example.Sim.Model.Jobs.Task;
import com.example.Sim.Model.NPC.Npc;

public class TirednessService
{
    public WorkStatus handleTiredness(final Npc npc, final Task task) {
        Integer tempTiredness = npc.getStat("Tiredness").getValue();
        final Integer tirednessChange = this.calculateDeltaTiredness(npc, task);
        npc.getStat("Tiredness").changeValue(tirednessChange);
        tempTiredness += tirednessChange;
        if (tempTiredness >= 100) {
            return this.handleOverwork(npc, tempTiredness);
        }
        return WorkStatus.NORMAL;
    }
    
    private Integer calculateDeltaTiredness(final Npc npc, final Task task) {
        Integer change = 0;
        final Integer constitution = npc.getStat("Constitution").getValue();
        final Integer tiring = task.getTiring();
        change = tiring - constitution / 10;
        return change;
    }
    
    private WorkStatus handleOverwork(final Npc npc, final Integer tempTiredness) {
        Integer overwork = tempTiredness - 100;
        final Integer obedience = npc.getStat("Obedience").getValue();
        final Integer health = npc.getStat("Health").getValue();
        if (obedience <= 100 - health) {
            return WorkStatus.OVERWORK_REFUSE;
        }
        overwork = Math.min(40, overwork);
        npc.getStat("Health").changeValue(Integer.valueOf(-overwork));
        if (npc.getStat("Health").getValue() <= 0) {
            return WorkStatus.DEAD_TIRED;
        }
        if (npc.getStat("Health").getValue() <= 40) {
            return WorkStatus.OVERWORKED_NEAR_DEATH;
        }
        return WorkStatus.OVERWORKED;
    }
}
