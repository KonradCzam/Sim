package com.example.Sim.Model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Setter
@Getter
public class Girl {

private String Name;

private List<Stat> Stats;

private List<Trait> Traits;

private String Human;

private String Catacomb;

private List<Skill> Skills;

private Integer Gold;

private String Desc;

public void addStat (Stat stat){
    this.Stats.add(stat);
}

public void addTrait (Trait trait) {
    this.Traits.add(trait);
}
    public void addSkill (Skill skill){
        this.Skills.add(skill);
    }
}
