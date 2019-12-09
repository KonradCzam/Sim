package com.example.Sim.Model.NPC;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NPCTaskExpStats {
    String taskName;
    String rank;
    Integer exp;
    Integer rankLevel;
    Integer customersServed;
    Integer thresholds[] = {0,100,300,600,1000,1500,2100,2800,3600,4500};
    String[] rankNames;
    public NPCTaskExpStats(String taskName, String rank, Integer exp, Integer rankLevel, Integer customersServed,String[] rankNames) {
        this.taskName = taskName;
        this.rank = rank;
        this.exp = exp;
        this.rankLevel = rankLevel;
        this.customersServed = customersServed;
        this.rankNames = rankNames;
        this.rank = rankNames[rankLevel-1];
    }

    public void handleTurn(Integer exp, Integer customersServed){
        Integer threshold = thresholds[rankLevel-1];

        this.exp += exp;
        if(this.exp>= threshold){
            this.rankLevel += 1;
        }

        rank = rankNames[rankLevel-1];
        this.customersServed += customersServed;
    }
}
