// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.Model.Jobs;

import java.util.List;
import java.io.Serializable;

public class Task implements Serializable
{
    private String name;
    private String description;
    private List<String> relevantSkills;
    private List<String> relevantStats;
    private String type;
    private Integer tiring;
    private Double moneyCoefficient;
    private Integer expGain;
    private String defaultCat;
    private Integer threshold;
    private Integer value;
    
    public String getName() {
        return this.name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public List<String> getRelevantSkills() {
        return (List<String>)this.relevantSkills;
    }
    
    public List<String> getRelevantStats() {
        return (List<String>)this.relevantStats;
    }
    
    public String getType() {
        return this.type;
    }
    
    public Integer getTiring() {
        return this.tiring;
    }
    
    public Double getMoneyCoefficient() {
        return this.moneyCoefficient;
    }
    
    public Integer getExpGain() {
        return this.expGain;
    }
    
    public String getDefaultCat() {
        return this.defaultCat;
    }
    
    public Integer getThreshold() {
        return this.threshold;
    }
    
    public Integer getValue() {
        return this.value;
    }
    
    public Task(final String name, final String description, final List<String> relevantSkills, final List<String> relevantStats, final String type, final Integer tiring, final Double moneyCoefficient, final Integer expGain, final String defaultCat, final Integer threshold, final Integer value) {
        this.name = name;
        this.description = description;
        this.relevantSkills = relevantSkills;
        this.relevantStats = relevantStats;
        this.type = type;
        this.tiring = tiring;
        this.moneyCoefficient = moneyCoefficient;
        this.expGain = expGain;
        this.defaultCat = defaultCat;
        this.threshold = threshold;
        this.value = value;
    }
}
