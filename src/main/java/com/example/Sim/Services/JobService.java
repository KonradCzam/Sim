// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.Services;

import com.example.Sim.Model.Customer;
import com.example.Sim.Model.Jobs.JobCustomers;
import java.util.Collections;
import com.example.Sim.Exceptions.NoConcernFoundInJob;
import java.util.Iterator;
import com.example.Sim.Model.Jobs.JobStat;
import com.example.Sim.Model.NPC.Skill;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Collection;
import com.example.Sim.Model.NPC.Stat;
import java.util.OptionalDouble;
import java.util.ArrayList;
import com.example.Sim.Model.NPC.Npc;
import java.util.function.Consumer;
import java.util.HashMap;
import java.util.function.Predicate;
import javax.annotation.Resource;
import com.example.Sim.Utilities.JobLoader;
import com.example.Sim.Model.Jobs.Task;
import java.util.Map;
import com.example.Sim.Model.Jobs.Job;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class JobService
{
    private List<Job> jobList;
    public static Map<String, Task> allTasks;
    private Task freeTime;
    @Resource
    JobLoader jobLoader;
    @Resource
    NpcService npcService;
    
    public JobService(final JobLoader jobLoader) {
        this.jobLoader = jobLoader;
        this.jobList = this.jobLoader.loadJobs();
        this.freeTime = this.jobList.get(0).getTasks().get(0);
        JobService.allTasks = this.calcAllTasks();
    }
    
    public Job getJobByName(final String jobName) {
        return (Job)this.jobList.stream().filter(JobService::lambda$getJobByName$0).findFirst().orElse(null);
    }
    
    private Map<String, Task> calcAllTasks() {
        final Map<String, Task> allTasks = new HashMap<String, Task>();
        this.jobList.forEach(JobService::lambda$calcAllTasks$2);
        return allTasks;
    }
    
    public static Double calculateTaskPerformance(final Npc npc, final String taskName) {
        return calculateTaskPerformance(npc, (Task)JobService.allTasks.get(taskName), (String)null);
    }
    
    public static Double calculateTaskPerformance(final Npc npc, final Task task) {
        return calculateTaskPerformance(npc, task, (String)null);
    }
    
    public static Double calculateTaskPerformance(final Npc npc, final Task task, final String category) {
        OptionalDouble averageSkill;
        if (category != null) {
            final List<String> relevantStats = new ArrayList<String>();
            relevantStats.add(category);
            averageSkill = calculateAverageSkill(npc, (List)relevantStats);
        }
        else {
            averageSkill = calculateAverageSkill(npc, task.getRelevantSkills());
        }
        final OptionalDouble averageStat = calculateAverageStat(npc, task.getRelevantStats());
        Double result;
        if (averageSkill.isPresent() && averageStat.isPresent()) {
            result = (averageSkill.getAsDouble() + averageStat.getAsDouble()) / 2.0;
        }
        else {
            result = 0.0;
        }
        return result;
    }
    
    public static OptionalDouble calculateAverageStat(final Npc npc, final List<String> relevantStats) {
        List<Stat> girlRelevantStats = new ArrayList<Stat>(npc.getStats().values());
        girlRelevantStats = girlRelevantStats.stream().filter(JobService::lambda$calculateAverageStat$3).collect((Collector<? super Object, ?, List<Stat>>)Collectors.toList());
        return girlRelevantStats.stream().mapToDouble((ToDoubleFunction<? super Object>)JobService::lambda$calculateAverageStat$4).average();
    }
    
    public static OptionalDouble calculateAverageSkill(final Npc npc, final List<String> relevantSkills) {
        List<Skill> girlRelevantSkills = new ArrayList<Skill>(npc.getSkills().values());
        girlRelevantSkills = girlRelevantSkills.stream().filter(JobService::lambda$calculateAverageSkill$5).collect((Collector<? super Object, ?, List<Skill>>)Collectors.toList());
        return girlRelevantSkills.stream().mapToDouble((ToDoubleFunction<? super Object>)JobService::lambda$calculateAverageSkill$6).average();
    }
    
    public String calculateAverageProficiencyScore(final Npc npc) {
        final List<String> relevantSkillsDay = (List<String>)npc.getDayShift().getRelevantSkills();
        final List<String> relevantStatsDay = (List<String>)npc.getDayShift().getRelevantStats();
        final List<String> relevantSkillsNight = (List<String>)npc.getDayShift().getRelevantSkills();
        final List<String> relevantStatsNight = (List<String>)npc.getDayShift().getRelevantStats();
        final OptionalDouble averageSkillDay = calculateAverageSkill(npc, (List)relevantSkillsDay);
        final OptionalDouble averageStatDay = calculateAverageStat(npc, (List)relevantStatsDay);
        final OptionalDouble averageSkillNight = calculateAverageSkill(npc, (List)relevantSkillsNight);
        final OptionalDouble averageStatNight = calculateAverageStat(npc, (List)relevantStatsNight);
        if (averageSkillDay.isPresent() && averageStatDay.isPresent() && averageSkillNight.isPresent() && averageStatNight.isPresent()) {
            final Double result = (averageSkillDay.getAsDouble() + averageStatDay.getAsDouble() + averageSkillNight.getAsDouble() + averageStatNight.getAsDouble()) / 4.0;
            final Integer resultInt = result.intValue();
            return resultInt.toString();
        }
        return "Not a number";
    }
    
    public void calculateAllJobsStats() {
        this.jobList.forEach(this::lambda$calculateAllJobsStats$7);
    }
    
    public void calculateJobAllStats(final Job job) {
        job.getJobStats().forEach(this::lambda$calculateJobAllStats$8);
    }
    
    public Double calculateJobStat(final String statName, final Boolean day, final Job job) {
        Double response = 0.0;
        try {
            final JobStat jobStat = this.getJobStatByName(statName, job);
            for (final Map.Entry<String, Integer> entry : jobStat.getTasks().entrySet()) {
                final List<Npc> npcsWorkingOnTask = (List<Npc>)this.getNpcsWorkingOnTask(entry.getKey(), day);
                final List<Double> partialProf = (List<Double>)this.getNpcProficiencyOnTask(entry.getKey(), npcsWorkingOnTask);
                response += this.getWeightedStat(partialProf) * (entry.getValue() / 100.0);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
    
    private JobStat getJobStatByName(final String name, final Job job) throws NoConcernFoundInJob {
        final JobStat jobStat = job.getJobStats().stream().filter(JobService::lambda$getJobStatByName$9).findFirst().orElse(null);
        if (jobStat == null) {
            throw new NoConcernFoundInJob();
        }
        return jobStat;
    }
    
    private List<Npc> getNpcsWorkingOnTask(final String name, final Boolean day) {
        List<Npc> npcsWorkingOnTask = new ArrayList<Npc>();
        if (day) {
            npcsWorkingOnTask = (List<Npc>)this.npcService.getHiredNpcs().stream().filter(JobService::lambda$getNpcsWorkingOnTask$10).collect(Collectors.toList());
        }
        else {
            npcsWorkingOnTask = (List<Npc>)this.npcService.getHiredNpcs().stream().filter(JobService::lambda$getNpcsWorkingOnTask$11).collect(Collectors.toList());
        }
        return npcsWorkingOnTask;
    }
    
    private Double getWeightedStat(final List<Double> partialProf) {
        Double result = 0.0;
        Collections.sort(partialProf);
        Collections.reverse(partialProf);
        final Integer size = partialProf.size();
        if (size > 0) {
            result += partialProf.get(0) * 0.5;
        }
        if (size > 1) {
            result += partialProf.get(1) * 0.25;
        }
        if (size > 2) {
            result += partialProf.get(2) * 0.25;
        }
        if (size > 3) {
            result += partialProf.get(3) * 0.1;
        }
        if (size > 4) {
            result += partialProf.get(4) * 0.1;
        }
        if (size > 5) {
            for (int i = 5; i < size; ++i) {
                result += partialProf.get(i) * 0.05;
            }
        }
        return result;
    }
    
    public List<Double> getNpcProficiencyOnTask(final String name, final List<Npc> npcsWorkingOnTask) {
        final List<Double> partialProf = new ArrayList<Double>();
        npcsWorkingOnTask.forEach(JobService::lambda$getNpcProficiencyOnTask$12);
        Collections.sort(partialProf);
        return partialProf;
    }
    
    public JobCustomers handleEndTurn(final JobCustomers jobCustomers) {
        List<Customer> tempCustList = new ArrayList<Customer>();
        tempCustList = jobCustomers.getDayLowCustomesList();
        jobCustomers.setDayLowCustomesList(this.calcCustomerList(tempCustList, true));
        tempCustList = jobCustomers.getDayMidCustomesList();
        jobCustomers.setDayMidCustomesList(this.calcCustomerList(tempCustList, true));
        tempCustList = jobCustomers.getNightHighCustomesList();
        jobCustomers.setDayHighCustomesList(this.calcCustomerList(tempCustList, true));
        tempCustList = jobCustomers.getNightLowCustomesList();
        jobCustomers.setNightLowCustomesList(this.calcCustomerList(tempCustList, false));
        tempCustList = jobCustomers.getNightMidCustomesList();
        jobCustomers.setNightMidCustomesList(this.calcCustomerList(tempCustList, false));
        tempCustList = jobCustomers.getNightHighCustomesList();
        jobCustomers.setNightHighCustomesList(this.calcCustomerList(tempCustList, false));
        return jobCustomers;
    }
    
    private List<Customer> calcCustomerList(final List<Customer> tempCustList, final Boolean day) {
        tempCustList.stream().forEach(this::lambda$calcCustomerList$13);
        return tempCustList;
    }
    
    private Integer calculateAmountSpent(final Customer customer) {
        final Double result = customer.getMoney() * customer.getHappiness();
        if (customer.getTier().equals("low") && customer.getHappiness() == 0.5) {
            return 0;
        }
        if (customer.getTier().equals("mid") && customer.getHappiness() == 0.25) {
            return 0;
        }
        return result.intValue();
    }
    
    private Customer setCustomerHappiness(final Customer customer, final Boolean day) {
        Double happiness = 0.0;
        final Job targetJob = this.getJobByName(customer.getTargetJob());
        final String mainConcern = customer.getMainConcern().getName();
        final String secondaryConcern = customer.getMainConcern().getName();
        try {
            Double mainConcernStat;
            Double secondaryConcernStat;
            if (day) {
                mainConcernStat = targetJob.getJobStat(mainConcern).getDayValue();
                secondaryConcernStat = targetJob.getJobStat(secondaryConcern).getDayValue();
            }
            else {
                mainConcernStat = targetJob.getJobStat(mainConcern).getNightValue();
                secondaryConcernStat = targetJob.getJobStat(secondaryConcern).getDayValue();
            }
            happiness = (mainConcernStat * 0.75 + secondaryConcernStat * 0.25) / 100.0;
        }
        catch (Exception e) {
            System.out.println(targetJob.getName() + "  " + customer.getMainConcern().getName());
            e.printStackTrace();
        }
        if (customer.getTier().equals("low")) {
            happiness += 0.5;
        }
        else if (customer.getTier().equals("mid")) {
            happiness += 0.25;
        }
        customer.setHappiness(happiness);
        return customer;
    }
    
    public String getJobNameByTask(final Task task) {
        final String taskname = task.getName();
        return ((Job)this.jobList.stream().filter(JobService::lambda$getJobNameByTask$14).findFirst().orElse(null)).getName();
    }
    
    public List<Job> getJobList() {
        return (List<Job>)this.jobList;
    }
    
    public Task getFreeTime() {
        return this.freeTime;
    }
    
    public JobLoader getJobLoader() {
        return this.jobLoader;
    }
    
    public NpcService getNpcService() {
        return this.npcService;
    }
}
