// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.Model.Raport;

import java.util.List;
import com.example.Sim.Model.TirednessSystem.WorkStatus;

public class NpcRoot extends GirlEndTurnRapport
{
    String name;
    String messageLevel;
    Integer dayMoneyEarned;
    Integer nightMoneyEarned;
    Integer dayExpGain;
    Integer nightExpGain;
    Integer moneyEarned;
    String description;
    String statChange;
    String path;
    String category;
    WorkStatus dayWorkStatus;
    WorkStatus nightWorkStatus;
    List<SingleEventRoot> dayShiftRapport;
    List<SingleEventRoot> nightShiftRapport;
    Integer obedience;
    Integer love;
    
    public NpcRoot() {
        this.category = "profile";
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getMessageLevel() {
        return this.messageLevel;
    }
    
    public Integer getDayMoneyEarned() {
        return this.dayMoneyEarned;
    }
    
    public Integer getNightMoneyEarned() {
        return this.nightMoneyEarned;
    }
    
    public Integer getDayExpGain() {
        return this.dayExpGain;
    }
    
    public Integer getNightExpGain() {
        return this.nightExpGain;
    }
    
    public Integer getMoneyEarned() {
        return this.moneyEarned;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getStatChange() {
        return this.statChange;
    }
    
    public String getPath() {
        return this.path;
    }
    
    public String getCategory() {
        return this.category;
    }
    
    public WorkStatus getDayWorkStatus() {
        return this.dayWorkStatus;
    }
    
    public WorkStatus getNightWorkStatus() {
        return this.nightWorkStatus;
    }
    
    public List<SingleEventRoot> getDayShiftRapport() {
        return (List<SingleEventRoot>)this.dayShiftRapport;
    }
    
    public List<SingleEventRoot> getNightShiftRapport() {
        return (List<SingleEventRoot>)this.nightShiftRapport;
    }
    
    public Integer getObedience() {
        return this.obedience;
    }
    
    public Integer getLove() {
        return this.love;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setMessageLevel(final String messageLevel) {
        this.messageLevel = messageLevel;
    }
    
    public void setDayMoneyEarned(final Integer dayMoneyEarned) {
        this.dayMoneyEarned = dayMoneyEarned;
    }
    
    public void setNightMoneyEarned(final Integer nightMoneyEarned) {
        this.nightMoneyEarned = nightMoneyEarned;
    }
    
    public void setDayExpGain(final Integer dayExpGain) {
        this.dayExpGain = dayExpGain;
    }
    
    public void setNightExpGain(final Integer nightExpGain) {
        this.nightExpGain = nightExpGain;
    }
    
    public void setMoneyEarned(final Integer moneyEarned) {
        this.moneyEarned = moneyEarned;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public void setStatChange(final String statChange) {
        this.statChange = statChange;
    }
    
    public void setPath(final String path) {
        this.path = path;
    }
    
    public void setCategory(final String category) {
        this.category = category;
    }
    
    public void setDayWorkStatus(final WorkStatus dayWorkStatus) {
        this.dayWorkStatus = dayWorkStatus;
    }
    
    public void setNightWorkStatus(final WorkStatus nightWorkStatus) {
        this.nightWorkStatus = nightWorkStatus;
    }
    
    public void setDayShiftRapport(final List<SingleEventRoot> dayShiftRapport) {
        this.dayShiftRapport = dayShiftRapport;
    }
    
    public void setNightShiftRapport(final List<SingleEventRoot> nightShiftRapport) {
        this.nightShiftRapport = nightShiftRapport;
    }
    
    public void setObedience(final Integer obedience) {
        this.obedience = obedience;
    }
    
    public void setLove(final Integer love) {
        this.love = love;
    }
}
