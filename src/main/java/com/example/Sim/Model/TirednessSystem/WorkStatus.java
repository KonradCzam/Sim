package com.example.Sim.Model.TirednessSystem;

import lombok.Getter;

import java.util.Arrays;
@Getter
public enum WorkStatus {
    NORMAL("Normal"),
    REFUSED("Warning"),
    OVERWORKED("Warning"),
    OVERWORKED_NEAR_DEATH("Severe"),
    DEAD_TIRED("Severe");
    String level;
WorkStatus(String level){
    this.level = level;

}
}
