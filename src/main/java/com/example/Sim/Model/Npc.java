package com.example.Sim.Model;

import com.example.Sim.Model.Jobs.Task;
import com.example.Sim.Services.JobService;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

@Setter
@Getter
public class Npc implements Serializable {


    private String path;

    private String folder;

    private String Name;
    private Map<String, Stat> HeavyStats;
    private Map<String, Stat> LightStats;
    private Map<String, Stat> Stats;

    private List<Trait> Traits;

    private Map<String, Skill> Skills;

    private String Human;

    private String Catacomb;

    private Integer price;

    private Integer Gold;

    private String Desc;

    private List<Item> inventory;

    private Map<String, Item> equippedItems;

    private Task dayShift;

    private Task nightShift;


    public Npc(JobService jobService) {
        this.Stats = new TreeMap<String, Stat>();
        this.LightStats = new TreeMap<String, Stat>();
        this.HeavyStats = new TreeMap<String, Stat>();
        this.Skills = new TreeMap<String, Skill>();
        this.Traits = new ArrayList<Trait>();
        inventory = new ArrayList<>();
        equippedItems = new TreeMap<>();
        dayShift = jobService.getFreeTime();
        nightShift = jobService.getFreeTime();

    }

    public void addHeavyStat(Stat stat) {
        this.HeavyStats.put(stat.getName(), stat);
    }
    public void addLightStat(Stat stat) {
        this.LightStats.put(stat.getName(), stat);
    }
    public void addTrait(Trait trait) {
        this.Traits.add(trait);
    }

    public void addTrait(Trait trait, Integer percent) {
        Integer treshold = ThreadLocalRandom.current().nextInt(0, 100);
        if (percent > treshold)
            this.Traits.add(trait);
    }

    public void addSkill(Skill skill) {
        this.Skills.put(skill.getName(), skill);
    }

    public Integer getGold() {
        return Gold;
    }

    public void setGold(Integer gold) {
        Gold = gold;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }



    public Integer calculateValue() {
        Integer statValue = calculateAverageWeightedStat();
        Integer skillValue = calculateAverageSkill();
        return statValue + skillValue;
    }

    private Integer calculateAverageSkill() {
        AtomicReference<Integer> value = new AtomicReference<>(0);
        this.getSkills().forEach((skillName, skill) -> value.set(value.get() + skill.getValue()));
        return value.get();
    }

    private Integer calculateAverageWeightedStat() {
        AtomicReference<Integer> value = new AtomicReference<>(0);
        this.getStats().forEach((statName, stat) -> value.set(value.get() + (stat.getValue() * stat.getWeight())));
        return value.get();
    }
    public Integer calculateLevel() {
        AtomicReference<Integer> value = new AtomicReference<>(0);
        this.getHeavyStats().forEach((statName, stat) -> value.set(value.get() + stat.getValue()));
        return value.get()/10;
    }
    public Stat getStat(String name){
        if(this.getHeavyStats().get(name) != null)
            return this.getHeavyStats().get(name);
        else
            return this.getLightStats().get(name);

    }
    public Skill getSkill(String name){
        return this.getSkills().get(name);
    }

    public Map<String, Stat> getStats(){
        Map<String, Stat> allStats = new TreeMap<String, Stat>();
        allStats.putAll(this.LightStats);
        allStats.putAll(this.HeavyStats);
        return allStats;
    }
}

