package com.example.Sim.Services;

import com.example.Sim.Model.Jobs.Job;
import com.example.Sim.Model.Jobs.Task;
import com.example.Sim.Model.Npc;
import com.example.Sim.Model.Skill;
import com.example.Sim.Model.Stat;
import com.example.Sim.Model.TirednessSystem.WorkStatus;
import com.example.Sim.Utilities.JobLoader;
import lombok.Getter;

import java.util.*;

@Getter
public class JobService {
    private List<Job> jobList;
    private Map<String,Task> allTasks;
    private Task freeTime ;
    public JobService(){
        JobLoader jobLoader = new JobLoader();
        this.jobList = jobLoader.generateJobs();
        this.freeTime = jobList.get(0).getTasks().get(0);
        this.allTasks = calcAllTasks();
    }
    private Map<String,Task> calcAllTasks(){
        Map<String,Task> allTasks = new HashMap<>();
        jobList.forEach(job -> job.getTasks().forEach(task -> allTasks.put(task.getName(),task)));
        return allTasks;
    }

    public static Double calculateTaskPerformance(Npc npc, Task task) {
        return calculateTaskPerformance(npc, task, null);
    }

    public static Double calculateTaskPerformance(Npc npc, Task task, String category) {
        OptionalDouble averageSkill;
        if (category != null) {
            List<String> relevantStats = new ArrayList<>();
            relevantStats.add(category);
            averageSkill = calculateAverageSkill(npc, relevantStats);
        } else {
            averageSkill = calculateAverageSkill(npc, task.getRelevantSkills());
        }

        OptionalDouble averageStat = calculateAverageStat(npc, task.getRelevantStats());
        Double result;
        if (averageSkill.isPresent() && averageStat.isPresent()) {
            result = (averageSkill.getAsDouble() + averageStat.getAsDouble()) / 2;
        } else {
            result = 0.0;
        }

        return result;
    }

    public static OptionalDouble calculateAverageStat(Npc npc, List<String> relevantStats) {
        List<Stat> girlRelevantStats = new ArrayList<Stat>(npc.getStats().values());
        //filter only the relevant skills
        List<String> tempList = new ArrayList<>();
        girlRelevantStats.stream().forEach(skill -> tempList.add(skill.getName()));
        tempList.retainAll(relevantStats);
        //calculate average
        return girlRelevantStats.stream().mapToDouble(stat -> stat.getValue()).average();
    }

    public static OptionalDouble calculateAverageSkill(Npc npc, List<String> relevantSkills) {
        List<Skill> girlRelevantSkills = new ArrayList<Skill>(npc.getSkills().values());
        //filter only the relevant skills
        List<String> tempList = new ArrayList<>();
        if (girlRelevantSkills != null)
            girlRelevantSkills.stream().forEach(skill -> tempList.add(skill.getName()));
        else {
            Double zero = 0.0;
            return OptionalDouble.of(zero);
        }
        tempList.retainAll(relevantSkills);
        //calculate average
        return girlRelevantSkills.stream().mapToDouble(skill -> skill.getValue()).average();
    }
    public static WorkStatus calculateIfRefuse(Npc npc, Task task) {
        Integer pcLove = npc.getStat("PCLove").getValue();
        Integer pcFear = npc.getStat("PCFear").getValue();
        Integer morality = npc.getStat("Morality").getValue();
        Integer obedience = npc.getStat("Obedience").getValue();
        Integer value = pcFear + pcLove + obedience;
        //Do if obedient
        if(task.getThreshold() + morality < value)
        {
            //if obedience close to treshold morality down.
            if(task.getThreshold() + morality < value - 20){
                npc.getStat("Morality").changeValue(-1);
            }
            //Obedience grows when facing difficult task
            if(obedience < (task.getThreshold()/2)){
                npc.getStat("Obedience").changeValue(+1);
            }
            return WorkStatus.NORMAL;
        }else{
            return WorkStatus.MORAL_REFUSE;
        }
    }
    public String calculateAverageProficiencyScore(Npc npc) {

        List<String> relevantSkillsDay = npc.getDayShift().getRelevantSkills();
        List<String> relevantStatsDay = npc.getDayShift().getRelevantStats();

        List<String> relevantSkillsNight = npc.getDayShift().getRelevantSkills();
        List<String> relevantStatsNight = npc.getDayShift().getRelevantStats();


        OptionalDouble averageSkillDay = calculateAverageSkill(npc,relevantSkillsDay);
        OptionalDouble averageStatDay = calculateAverageStat(npc,relevantStatsDay);

        OptionalDouble averageSkillNight = calculateAverageSkill(npc,relevantSkillsNight);
        OptionalDouble averageStatNight = calculateAverageStat(npc,relevantStatsNight);

        if(averageSkillDay.isPresent() && averageStatDay.isPresent() && averageSkillNight.isPresent() && averageStatNight.isPresent()){
            Double result = (averageSkillDay.getAsDouble() + averageStatDay.getAsDouble() + averageSkillNight.getAsDouble() + averageStatNight.getAsDouble())/4.0;
            Integer resultInt = result.intValue();
            return resultInt.toString();
        }
        return "Not a number";
    }
}
