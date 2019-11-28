// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.Model;

import java.util.TreeMap;
import java.util.ArrayList;
import com.example.Sim.Model.NPC.Stat;
import com.example.Sim.Model.NPC.Skill;
import java.util.Map;
import com.example.Sim.Model.NPC.Trait;
import java.util.List;
import java.io.Serializable;

public class Player implements Serializable
{
    String name;
    List<Trait> traits;
    List<Item> inventory;
    Map<String, Item> equippedItems;
    Integer gold;
    Map<String, Skill> skills;
    Map<String, Stat> stats;
    
    public Player(final List<String> skillsList, final List<String> statsList) {
        this.traits = new ArrayList<Trait>();
        this.inventory = new ArrayList<Item>();
        this.equippedItems = new TreeMap<String, Item>();
        this.skills = new TreeMap<String, Skill>();
        this.stats = new TreeMap<String, Stat>();
        this.name = "Name";
        this.gold = 10000;
        final Stat stat;
        statsList.forEach(statName -> stat = this.stats.put(statName, new Stat(statName, Integer.valueOf(100))));
        final Skill skill;
        skillsList.forEach(statName -> skill = this.skills.put(statName, new Skill(statName, Integer.valueOf(0))));
    }
    
    public void addTrait(final Trait trait) {
        this.traits.add(trait);
    }
    
    public String getName() {
        return this.name;
    }
    
    public List<Trait> getTraits() {
        return this.traits;
    }
    
    public List<Item> getInventory() {
        return this.inventory;
    }
    
    public Map<String, Item> getEquippedItems() {
        return this.equippedItems;
    }
    
    public Integer getGold() {
        return this.gold;
    }
    
    public Map<String, Skill> getSkills() {
        return this.skills;
    }
    
    public Map<String, Stat> getStats() {
        return this.stats;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setTraits(final List<Trait> traits) {
        this.traits = traits;
    }
    
    public void setInventory(final List<Item> inventory) {
        this.inventory = inventory;
    }
    
    public void setEquippedItems(final Map<String, Item> equippedItems) {
        this.equippedItems = equippedItems;
    }
    
    public void setGold(final Integer gold) {
        this.gold = gold;
    }
    
    public void setSkills(final Map<String, Skill> skills) {
        this.skills = skills;
    }
    
    public void setStats(final Map<String, Stat> stats) {
        this.stats = stats;
    }
}
