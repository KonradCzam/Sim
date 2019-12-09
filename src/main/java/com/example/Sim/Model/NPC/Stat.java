package com.example.Sim.Model.NPC;


import com.example.Sim.Model.DetailsInterface;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@NoArgsConstructor
public class Stat implements DetailsInterface, Serializable {
    private String name;
    private Integer traitBonus = 0;
    private Integer min;
    private Integer max;
    private Integer value;
    private Integer weight;
    private Double progress;

    public Stat(String name, Integer value) {
        this.name = name;
        this.value = value;
        this.progress = 0.0;
        weight = mapNameToWeight(name);
    }

    public Stat(String name, Integer min, Integer max) {
        this.name = name;
        this.value = ThreadLocalRandom.current().nextInt(min, max + 1);
        this.progress = 0.0;
        weight = mapNameToWeight(name);
    }
    public void changeTraitBonus(Integer value){
        this.traitBonus += value;
    }
    public void setValue(Integer value) {
        this.value = value;
    }

    public void setValue(Integer min, Integer max) {
        this.value = ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public void setMax() {
        this.value = 100;
    }

    public void setMin() {
        this.value = 0;
    }

    public void changeValue(Integer change) {
        this.value += change;
        if (value > 100) {
            setMax();
        }
        Integer threshold = 0;
        if( this.getName().equals("Obedience") || this.getName().equals("Love")){
            threshold= -100;
        }
        if (value < threshold) {
            this.value = threshold;
        }
    }
    public Integer getEffectiveValue(){
        return value + traitBonus;
    }
    public void changeProgress(Double change) {
        this.progress += change;
    }

    private Integer mapNameToWeight(String name) {
        switch (name) {
            case "Fame":
                return 10;
            case "Charisma":
                return 5;
            case "Beauty":
                return 5;
            case "Refinement":
                return 5;
            case "Agility":
                return 5;
            case "Strength":
                return 5;
            case "Constitution":
                return 5;
            case "Intelligence":
                return 5;
            case "Confidence":
                return 5;
            case "Obedience":
                return 10;
            case "Spirit":
                return -5;
            case "Libido":
                return 5;
            case "Dignity":
                return 5;

            default:
                return 0;
        }
    }
}
