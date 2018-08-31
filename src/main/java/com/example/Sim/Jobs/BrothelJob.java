package com.example.Sim.Jobs;

import com.example.Sim.Model.Girl;
import com.example.Sim.Model.Skill;
import com.example.Sim.Model.Stat;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class BrothelJob extends Job {

    public BrothelJob(){
        relevantSkills = Arrays.asList( "Performance","NormalSex","Anal","BDSM","Beastiality","Lesbian","Strip","Group","OralSex","TittySex","Handjob","Footjob");
        relevantStats = Arrays.asList("Level", "Fame", "House","Health","Happiness","Tiredness","PCLove","PCFear","PCHate","Lactation","Charisma","Beauty","Refinement","Agility","Intelligence","Morality","Dignity","Confidence","Obedience","Spirit","Libido");

    }

    public Integer calculateTaskPerformance(Girl girl,String task){
        Skill relevantSkill = girl.getSkills().stream().filter(skill -> skill.getName().contains(task)).collect(Collectors.toList()).get(0);
        Double d = calculateAverageStat(girl).getAsDouble();
        Double taskPerformance = d / relevantSkill.getValue();
        return taskPerformance.intValue();
    }


}
