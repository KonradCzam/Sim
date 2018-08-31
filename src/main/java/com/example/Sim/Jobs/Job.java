package com.example.Sim.Jobs;

import com.example.Sim.Model.Girl;
import com.example.Sim.Model.Skill;
import com.example.Sim.Model.Stat;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Getter
public class Job {
    List<String> relevantSkills;
    List<String> relevantStats;

    public Double calculateJobPerformance(Girl girl) {
        OptionalDouble averageSkill = calculateAverageSkill(girl);
        OptionalDouble averageStat = calculateAverageStat(girl);

        Double result = (averageSkill.getAsDouble() + averageStat.getAsDouble()) / 2;
        return result;
    }

    public OptionalDouble calculateAverageStat(Girl girl) {
        List<Stat> girlRelevantStats = girl.getStats();
        //filter only the relevant skills
        girlRelevantStats.retainAll(relevantStats);
        //calculate average
        return girlRelevantStats.stream().mapToDouble(stat -> stat.getValue()).average();
    }

    public OptionalDouble calculateAverageSkill(Girl girl) {
        List<Skill> girlRelevantSkills = girl.getSkills();
        //filter only the relevant skills
        List<String> tempList = new ArrayList<>();
        girlRelevantSkills.stream().forEach(skill -> tempList.add(skill.getName()));
        tempList.retainAll(relevantSkills);
        //calculate average
        return girlRelevantSkills.stream().mapToDouble(skill -> skill.getValue()).average();
    }

}
