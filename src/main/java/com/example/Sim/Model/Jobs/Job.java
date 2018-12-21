package com.example.Sim.Model.Jobs;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
public class Job implements Serializable {
    String name;
    List<Task> tasks;
    Task currentTask;
    Integer popularityDayLow;
    Integer popularityDayMid;
    Integer popularityDayHigh;
    Integer popularityNightLow;
    Integer popularityNightMid;
    Integer popularityNightHigh;
    List<JobStat> jobStats;


    public Job(String name)
    {
        this.popularityDayLow = 0;
        this.popularityDayMid = 0;
        this.popularityDayHigh = 0;
        this.popularityNightLow = 0;
        this.popularityNightMid = 0;
        this.popularityNightHigh = 0;
        this.name = name;
    }
    public JobStat getJobStat(String statName){
        return jobStats.stream().filter(jobStat -> jobStat.getStatName().equals(statName)).findFirst().orElse(null);
    }






}
