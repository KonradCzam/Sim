package com.example.Sim.Model;

import lombok.Getter;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public class Skill implements DetailsInterface, Serializable {
    private String name;
    private Integer value;
    private Double progress;
    private String category;

    public Skill(String name, Integer value) {
        this.name = name.toLowerCase();
        this.value = value;
        this.progress = 0.0;
        this.category = mapNametoCategory(name);
    }

    public Skill(String name, Integer min, Integer max) {
        this.name = name.toLowerCase();
        this.value = ThreadLocalRandom.current().nextInt(min, max + 1);
        this.progress = 0.0;
        this.category = mapNametoCategory(name);

    }

    public void changeValue(Integer change) {
        this.value += change;
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
            case "AnimalHandling":
                return "profile";
            case "Cooking":
                return "profile";
            case "NormalSex":
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
            case "Group":
                return "group";
            case "OralSex":
                return "oral";
            case "TittySex":
                return "titty";
            case "Handjob":
                return "hand";
            case "Footjob":
                return "foot";
            default:
                return "profile";
        }

    }
}
