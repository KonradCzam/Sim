// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.Model.TirednessSystem;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WorkStatus
{
    NORMAL("NORMAL", 0, "Normal"),
    MORAL_REFUSE("MORAL_REFUSE", 1, "Warning"),
    OVERWORK_REFUSE("OVERWORK_REFUSE", 2, "Warning"),
    OVERWORKED("OVERWORKED", 3, "Warning"),
    OVERWORKED_NEAR_DEATH("OVERWORKED_NEAR_DEATH", 4, "Severe"),
    DEAD_TIRED("DEAD_TIRED", 5, "Severe");

    String name;
    public Integer code;
    public String level;


}
