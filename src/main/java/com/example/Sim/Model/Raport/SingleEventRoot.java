// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.Model.Raport;

import java.util.HashMap;
import java.util.Map;

public class SingleEventRoot extends GirlEndTurnRapport
{
    String description;
    Integer moneyEarned;
    String name;
    String npcName;
    String job;
    String path;
    String category;
    String skill;
    String skillsGain;
    Map<String, Double> statProgress;
    
    public SingleEventRoot() {
        this.name = "Event";
        this.statProgress = new HashMap();
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public Integer getMoneyEarned() {
        return this.moneyEarned;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getNpcName() {
        return this.npcName;
    }
    
    public String getJob() {
        return this.job;
    }
    
    public String getPath() {
        return this.path;
    }
    
    public String getCategory() {
        return this.category;
    }
    
    public String getSkill() {
        return this.skill;
    }
    
    public String getSkillsGain() {
        return this.skillsGain;
    }
    
    public Map<String, Double> getStatProgress() {
        return (Map<String, Double>)this.statProgress;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public void setMoneyEarned(final Integer moneyEarned) {
        this.moneyEarned = moneyEarned;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setNpcName(final String npcName) {
        this.npcName = npcName;
    }
    
    public void setJob(final String job) {
        this.job = job;
    }
    
    public void setPath(final String path) {
        this.path = path;
    }
    
    public void setCategory(final String category) {
        this.category = category;
    }
    
    public void setSkill(final String skill) {
        this.skill = skill;
    }
    
    public void setSkillsGain(final String skillsGain) {
        this.skillsGain = skillsGain;
    }
    
    public void setStatProgress(final Map<String, Double> statProgress) {
        this.statProgress = statProgress;
    }
}
