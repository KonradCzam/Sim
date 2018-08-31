package com.example.Sim.screens.Brothel.model;

import com.example.Sim.Jobs.BrothelJob;
import com.example.Sim.Model.Girl;
import com.example.Sim.Model.Stat;
import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class BrothelTableGirl {
    String name;
    String dayShift;
    String nightShift;
    String tiredness;
    String avgSex;
    String path;

    public BrothelTableGirl(Girl girl) {
        this.name = girl.getName();
        this.dayShift = "Free time";
        this.nightShift = "Free time";

        this.avgSex = calculateAverageSexScore(girl).toString();
        Integer tirednessInt = girl.getStats().stream()
                .filter(stat -> "Tiredness".equals(stat.getName()))
                .map(Stat::getValue)
                .findAny()
                .orElse(0);

        this.tiredness = tirednessInt.toString();
        this.path = girl.getPath();
    }

    public Integer calculateAverageSexScore(Girl girl) {
        girl.getStats();
        BrothelJob brothelJob = new BrothelJob();
        Double avgSkill = brothelJob.calculateAverageSkill(girl).getAsDouble();
        return avgSkill.intValue();

    }
}
