package com.example.Sim.Model.Jobs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class Job implements Serializable {
    String name;
    List<Task> tasks;
    Task currentTask;

    public Job(String name)
    {
        this.name = name;
    }
}
