package com.example.Sim.Model;


import lombok.Getter;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public class Stat implements DetailsInterface, Serializable {
    private String name;
    private Integer min;
    private Integer max;
    private Integer value;
    private Double progress;

    public Stat(String name, Integer value) {
        this.name = name;
        this.value = value;
        this.progress = 0.0;
    }

    public Stat(String name, Integer min, Integer max) {
        this.name = name;
        this.value = ThreadLocalRandom.current().nextInt(min, max + 1);

    }
    public void setValue(Integer value) {
        this.value = value;
    }
    public void setMax(){
        this.value = 100;
    }
    public void setMin(){
        this.value = 0;
    }
    public void changeValue(Integer change) {
        this.value += change;
    }

    public void changeProgress(Double change) {
        this.progress += change;
    }
}
