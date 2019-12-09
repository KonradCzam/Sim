package com.example.Sim.Model.NPC;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;


@Setter
@Getter
@NoArgsConstructor
public class Trait implements Serializable {
    private String name;
    private String description;
    private String type;
    private Integer inheritChance;
    private Integer randomChance;
    private Map<String, Integer> skillEffects;
    private Map<String, Integer> statEffects;
    private String effect;

    public Trait(String name) {
        this.name = name;
    }

    public Trait(String name, String description, String type, Integer inheritChance, Integer randomChance, Map<String, Integer> skillEffects, Map<String, Integer> statEffects) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.inheritChance = inheritChance;
        this.randomChance = randomChance;
        this.skillEffects = skillEffects;
        this.statEffects = statEffects;
        this.effect = getStringEffect(skillEffects,statEffects);
    }

    private String getStringEffect(Map<String, Integer> skillEffects, Map<String, Integer> statEffectss) {
        String effect = "";
        for (Map.Entry<String, Integer> entry : skillEffects.entrySet()) {
            if(entry.getKey() !="")
            effect += (entry.getKey().substring(0,3) + " + " + entry.getValue() + " ");
        }
        for (Map.Entry<String, Integer> entry : statEffectss.entrySet()) {
            if(entry.getKey() !="")
            effect += (entry.getKey().substring(0,3) + " + " + entry.getValue() + " ");
        }
        return effect;
    }
}
