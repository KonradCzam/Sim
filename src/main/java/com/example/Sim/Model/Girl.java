package com.example.Sim.Model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Setter
@Getter
public class Girl {

private String path;

private String folder;

private String Name;

private List<Stat> Stats;

private List<Trait> Traits;

private String Human;

private String Catacomb;

private List<Skill> Skills;

private Integer Gold;

private String Desc;

public Girl (){
    this.Stats = new ArrayList<Stat>();
    this.Skills = new ArrayList<Skill>();
    this.Traits = new ArrayList<Trait>();
}
public void addStat (Stat stat){
    this.Stats.add(stat);
}

public void addTrait (Trait trait) {
        this.Traits.add(trait);
    }
    public void addTrait (Trait trait,Integer percent) {
        Integer treshold= ThreadLocalRandom.current().nextInt(0, 100);
        if (percent > treshold)
            this.Traits.add(trait);
    }
    public void addSkill (Skill skill){
        this.Skills.add(skill);
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
}

