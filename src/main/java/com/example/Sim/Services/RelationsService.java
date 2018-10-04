package com.example.Sim.Services;

import com.example.Sim.Model.Jobs.Task;
import com.example.Sim.Model.Npc;
import com.example.Sim.Model.TirednessSystem.WorkStatus;

import java.util.Map;

public class RelationsService {
    Task dayTask;
    Task nightTask;

    public Map<String,Integer> handleRelationShip(Npc npc) {
        dayTask = npc.getDayShift();
        nightTask = npc.getNightShift();
        Integer pcLove = npc.getStat("PCLove").getValue();
        Integer pcFear = npc.getStat("PCFear").getValue();
        Integer obedience = npc.getStat("Obedience").getValue();

        return null;
    }


}
