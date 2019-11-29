package com.example.Sim.Model;

import com.example.Sim.Model.NPC.Skill;
import com.example.Sim.Model.NPC.Stat;
import com.example.Sim.Model.NPC.Trait;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Getter
@Setter
@NoArgsConstructor
public class Player implements Serializable {



    String name;
    List<Trait> traits = new ArrayList<>();
    List<Item> inventory = new ArrayList<>();
    Map<String, Item> equippedItems = new TreeMap<String, Item>();
    Integer gold;
    Map<String,Skill> skills = new TreeMap<>();
    Map<String,Stat> stats = new TreeMap<>();

    public Player(List<String> skillsList, List<String> statsList) {
        name = "Name";
        gold = 10000;
        statsList.forEach(statName -> stats.put(statName,new Stat(statName, 100)));
        skillsList.forEach(statName -> skills.put(statName,new Skill(statName, 0)));

    }
    public void addTrait(Trait trait){
        traits.add(trait);
    }

}
