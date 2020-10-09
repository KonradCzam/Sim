package com.example.Sim.Model.NPC;

import com.example.Sim.Model.Item;
import com.example.Sim.Model.Jobs.Task;
import com.example.Sim.Services.JobService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

@Setter
@Getter
@NoArgsConstructor
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
    private String generatedDesc;
    private List<Item> inventory;

    private Map<String, Item> equippedItems;

    private Task task;



    private Map<String, Integer> factors = new TreeMap<>();
    Map<String, List<NPCTaskExpStats>> jobExperience = new TreeMap<>();



    public Npc(JobService jobService) {
        this.Stats = new TreeMap<String, Stat>();
        this.LightStats = new TreeMap<String, Stat>();
        this.HeavyStats = new TreeMap<String, Stat>();
        this.Skills = new TreeMap<String, Skill>();
        this.Traits = new ArrayList<Trait>();
        inventory = new ArrayList<>();
        equippedItems = new TreeMap<>();
        task = jobService.getFreeTime();
        generateJobExpStructure(jobService);
    }

    private void generateJobExpStructure(JobService jobService) {

        jobService.getJobList().forEach(job -> {
            List<NPCTaskExpStats> tempList = new ArrayList<>();
            job.getTasks().forEach(task -> {

                NPCTaskExpStats NPCTaskExpStats = new NPCTaskExpStats(task.getName(), "", 0, 1, 0,jobService.getRankName(task.getName()));
                tempList.add(NPCTaskExpStats);

            });
            jobExperience.put(job.getName(), tempList);
        });

    }

    public void addHeavyStat(Stat stat) {
        this.HeavyStats.put(stat.getName(), stat);
    }

    public void addLightStat(Stat stat) {
        this.LightStats.put(stat.getName(), stat);
    }
    public void addSkill(Skill skill) {
        this.Skills.put(skill.getName(), skill);
    }
    public void addTrait(Trait trait) {
        this.Traits.add(trait);
        changeSkillTraitBonus(trait,true);
    }
    public void changeSkillTraitBonus(Trait trait, Boolean add){
        Map<String, Integer> traitStatEffects =  trait.getStatEffects();
        Map<String, Integer> traitskillEffects =  trait.getSkillEffects();
        for (Map.Entry<String, Integer> entry : traitStatEffects.entrySet())
        {
            if(!entry.getKey().equals("")) {
                if (add)
                    this.getStat(entry.getKey()).changeTraitBonus(entry.getValue());
                else
                    this.getStat(entry.getKey()).changeTraitBonus(-entry.getValue());
            }
        }
        for (Map.Entry<String, Integer> entry : traitskillEffects.entrySet())
        {
            if(!entry.getKey().equals("") && !entry.getKey().equals("Beastiality")){
                if(add)
                    this.getSkill(entry.getKey()).changeTraitBonus(entry.getValue());
                else
                    this.getSkill(entry.getKey()).changeTraitBonus(-entry.getValue());
            }

        }
    }
    public void addTrait(Trait trait, Integer percent) {
        Integer treshold = ThreadLocalRandom.current().nextInt(0, 100);
        if (percent > treshold){
            this.Traits.add(trait);
            changeSkillTraitBonus(trait,true);
        }

    }

    public void removeTrait(String traitName) {
        getTraits().removeIf(trait -> {
            if (traitName.equals(trait.getName())){
                changeSkillTraitBonus(trait,false);
                return true;
            }
            return false;
        });

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

    public Stat getStat(String name) {

        if (this.getHeavyStats().get(name) != null)
            return this.getHeavyStats().get(name);
        else
            return this.getLightStats().get(name);

    }

    public Skill getSkill(String name) {
        return this.getSkills().get(name);
    }

    public boolean checkTrait(String name) {
        return getTraits().stream().anyMatch(trait -> trait.getName().equals(name));
    }
    public Map<String, Stat> getStats() {
        Map<String, Stat> allStats = new TreeMap<String, Stat>();
        allStats.putAll(this.LightStats);
        allStats.putAll(this.HeavyStats);
        return allStats;
    }
    public void addJobExp(String jobName,Task task, Integer noOfClients){
       NPCTaskExpStats npcTaskExpStats = jobExperience.get(jobName).stream().filter(npcTaskExpStat -> npcTaskExpStat.getTaskName().equals(task.getName())).findFirst().orElse(null);
       npcTaskExpStats.handleTurn(task.getExpGain(),noOfClients);


    }
    public void setDefaultFactors(){
        HashMap<String,Integer> factors = new HashMap<>();
        factors.put("HousingFactor",0);
        this.setFactors(factors);
    }
}

