package com.example.Sim.Model.Raport;


import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class SingleEventRoot extends  EndTurnRapport{
    String description;
    Integer moneyEarned;
    String name = "Event";
    String job ;
    String path;
    String category;
    String skill;
    Integer expGain;
    String skillsGain ;
    Map<String,Double> statProgress = new HashMap<String, Double>();


}
