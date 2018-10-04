package com.example.Sim.Model;

import com.example.Sim.Model.Jobs.Job;
import com.example.Sim.Model.Jobs.Task;
import com.example.Sim.Services.JobService;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Setter
@Getter
public class Npc implements Serializable {


    private String path;

    private String folder;

    private String Name;

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

    private JobService jobService;

    public Npc(JobService jobService) {
        this.Stats = new HashMap<String, Stat>();
        this.Skills = new HashMap<String, Skill>();
        this.Traits = new ArrayList<Trait>();
        inventory = new ArrayList<>();
        equippedItems = new HashMap<>();
        dayShift = jobService.getFreeTime();
        nightShift = jobService.getFreeTime();
        this.jobService = jobService;

    }

    public void addStat(Stat stat) {
        this.Stats.put(stat.getName(), stat);
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

    public String calculateAverageProficiencyScore(Npc npc) {

        List<String> relevantSkills = npc.getDayShift().getRelevantSkills();
        List<String> relevantStats = npc.getNightShift().getRelevantStats();

        OptionalDouble averageSkill = this.jobService.calculateAverageSkill(npc,relevantSkills);
        OptionalDouble averageStat = this.jobService.calculateAverageStat(npc,relevantStats);
        if(averageSkill.isPresent() && averageStat.isPresent()){
            Double result = (averageSkill.getAsDouble() + averageStat.getAsDouble())/2.0;
            Integer resultInt = result.intValue();
            return resultInt.toString();
        }
        return "Not a number";
    }
    public Stat getStat(String name){
        return this.getStats().get(name);
    }
    public Skill getSkill(String name){
        return this.getSkills().get(name);
    }
}

