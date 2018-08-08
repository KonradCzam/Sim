package com.example.Sim.Model;

import java.util.concurrent.ThreadLocalRandom;

public class Skill {
    private String name;
    private Integer min;
    private Integer max;
    private Integer value;
    public Skill (String name,Integer value){
        this.name = name;
        this.value = value;

    }
    public Skill (String name,Integer min,Integer max){
        this.name = name;
        this.value = ThreadLocalRandom.current().nextInt(min, max + 1);

    }
}
