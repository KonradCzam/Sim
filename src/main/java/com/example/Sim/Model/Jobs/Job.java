package com.example.Sim.Model.Jobs;

import com.example.Sim.Model.Npc;
import com.example.Sim.Model.Skill;
import com.example.Sim.Model.Stat;
import com.example.Sim.Model.TirednessSystem.WorkStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;


@Getter
@Setter
public class Job implements Serializable {
    String name;
    List<Task> tasks;
    Task currentTask;
    public Job(String name) {
        this.name = name;
    }



}
