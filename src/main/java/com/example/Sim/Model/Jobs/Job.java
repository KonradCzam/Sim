// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.Model.Jobs;

import java.util.List;
import java.io.Serializable;

public class Job implements Serializable
{
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
    
    public Job(final String name) {
        this.popularityDayLow = 0;
        this.popularityDayMid = 0;
        this.popularityDayHigh = 0;
        this.popularityNightLow = 0;
        this.popularityNightMid = 0;
        this.popularityNightHigh = 0;
        this.name = name;
    }
    
    public JobStat getJobStat(final String statName) {
        return this.jobStats.stream().filter(jobStat -> jobStat.getStatName().equals(statName)).findFirst().orElse(null);
    }
    
    public String getName() {
        return this.name;
    }
    
    public List<Task> getTasks() {
        return this.tasks;
    }
    
    public Task getCurrentTask() {
        return this.currentTask;
    }
    
    public Integer getPopularityDayLow() {
        return this.popularityDayLow;
    }
    
    public Integer getPopularityDayMid() {
        return this.popularityDayMid;
    }
    
    public Integer getPopularityDayHigh() {
        return this.popularityDayHigh;
    }
    
    public Integer getPopularityNightLow() {
        return this.popularityNightLow;
    }
    
    public Integer getPopularityNightMid() {
        return this.popularityNightMid;
    }
    
    public Integer getPopularityNightHigh() {
        return this.popularityNightHigh;
    }
    
    public List<JobStat> getJobStats() {
        return this.jobStats;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setTasks(final List<Task> tasks) {
        this.tasks = tasks;
    }
    
    public void setCurrentTask(final Task currentTask) {
        this.currentTask = currentTask;
    }
    
    public void setPopularityDayLow(final Integer popularityDayLow) {
        this.popularityDayLow = popularityDayLow;
    }
    
    public void setPopularityDayMid(final Integer popularityDayMid) {
        this.popularityDayMid = popularityDayMid;
    }
    
    public void setPopularityDayHigh(final Integer popularityDayHigh) {
        this.popularityDayHigh = popularityDayHigh;
    }
    
    public void setPopularityNightLow(final Integer popularityNightLow) {
        this.popularityNightLow = popularityNightLow;
    }
    
    public void setPopularityNightMid(final Integer popularityNightMid) {
        this.popularityNightMid = popularityNightMid;
    }
    
    public void setPopularityNightHigh(final Integer popularityNightHigh) {
        this.popularityNightHigh = popularityNightHigh;
    }
    
    public void setJobStats(final List<JobStat> jobStats) {
        this.jobStats = jobStats;
    }
}
