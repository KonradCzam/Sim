package com.example.Sim.Model.Jobs;

import com.example.Sim.Model.Npc;
import com.example.Sim.Model.Skill;
import com.example.Sim.Model.Stat;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;


@Getter
@Setter
public class Job {
    String name;
    List<Task> tasks;
    Task currentTask =  Task.FREE_TIME;
    public static Double calculateTaskPerformance(Npc npc, Task task) {
       return calculateTaskPerformance(npc,task,null);
    }
    public static Double calculateTaskPerformance(Npc npc, Task task,String category) {
        OptionalDouble averageSkill;
        if(category != null){
            List<String> relevantStats = new ArrayList<>();
            relevantStats.add(category);
            averageSkill = calculateAverageSkill(npc, relevantStats);
        }else{
            averageSkill = calculateAverageSkill(npc, task.getRelevantSkills());
        }

        OptionalDouble averageStat = calculateAverageStat(npc, task.getRelevantStats());
        Double result;
        if(averageSkill.isPresent() && averageStat.isPresent()){
             result = (averageSkill.getAsDouble() + averageStat.getAsDouble()) / 2;
        }else{
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
        if(girlRelevantSkills != null)
            girlRelevantSkills.stream().forEach(skill -> tempList.add(skill.getName()));
        else {
            Double zero = 0.0 ;
            return OptionalDouble.of(zero);
        }
        tempList.retainAll(relevantSkills);
        //calculate average
        return girlRelevantSkills.stream().mapToDouble(skill -> skill.getValue()).average();
    }

}
