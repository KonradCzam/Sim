package com.example.Sim.Model.NPC;

import com.example.Sim.Model.DetailsInterface;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@NoArgsConstructor
public class Skill implements DetailsInterface, Serializable {
    private String name;
    private Integer value;
    private Integer traitBonus = 0;
    private Double progress;
    private String category;

    public Skill(String name, Integer value) {
        this.name = name;
        this.value = value;
        this.progress = 0.0;
        this.category = mapNametoCategory(name);
    }

    public Skill(String name, Integer min, Integer max) {
        this.name = name;
        this.value = ThreadLocalRandom.current().nextInt(min, max + 1);
        this.progress = 0.0;
        this.category = mapNametoCategory(name);

    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public void setValue(Integer min, Integer max) {
        this.value = ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public void changeValue(Integer change) {
        this.value += change;
        if (this.value > 100)
            this.value = 100;
        if (this.value < 0)
            this.value = 0;

    }

    public void changeProgress(Double change) {
        progress += change;
    }

    public boolean checkSkillLvlUp(String skill) {
        if (progress >= 1.0) {
            progress -= 1.0;
            changeValue(1);
            return true;
        }
        return false;
    }

    public void changeTraitBonus(Integer value) {
        this.traitBonus += value;
    }

    public Integer getEffectiveValue() {
        return value + traitBonus;
    }

    private String mapNametoCategory(String name) {
        switch (name) {
            case "Combat":
                return "combat";
            case "Medicine":
                return "nurse";
            case "Performance":
                return "custom";
            case "Crafting":
                return "profile";
            case "Farming":
                return "profile";
            case "Herbalism":
                return "profile";
            case "Brewing":
                return "profile";
            case "Animal Handling":
                return "profile";
            case "Cooking":
                return "profile";
            case "Normal Sex":
                return "sex";
            case "Anal":
                return "anal";
            case "BDSM":
                return "bdsm";
            case "Beastiality":
                return "beastiality";
            case "Lesbian":
                return "les";
            case "Strip":
                return "strip";
            case "Group Sex":
                return "group";
            case "Oral Sex":
                return "oral";
            case "Titty Sex":
                return "titty";
            case "Handjob":
                return "hand";
            case "Masturbation":
                return "finger";
            case "Deep":
                return "oral";
            case "Dildo":
                return "dildo";
            case "Footjob":
                return "foot";
            default:
                return "profile";
        }

    }
}
