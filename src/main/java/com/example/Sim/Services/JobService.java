package com.example.Sim.Services;

import com.example.Sim.Model.Jobs.Customer;
import com.example.Sim.Model.Jobs.Job;
import com.example.Sim.Model.Jobs.JobCustomers;
import com.example.Sim.Model.Jobs.Task;
import com.example.Sim.Model.NPC.Npc;
import com.example.Sim.Model.NPC.Skill;
import com.example.Sim.Model.NPC.Stat;
import com.example.Sim.Utilities.JobLoader;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Service
public class JobService {
    private List<Job> jobList;
    public static Map<String,Task> allTasks;
    private Task freeTime;
    @Resource
    JobLoader jobLoader;
    @Resource
    NpcService npcService;
    @Value("#{'${ranks.brothel.whore}'.split(',')}")
    String[] brothelWhoreRanks;
    @Value("#{'${ranks.brothel.masseuse}'.split(',')}")
    String[] brothelMasseuseRanks;
    @Value("#{'${ranks.brothel.cleaner}'.split(',')}")
    String[] brothelCleanerRanks;
    @Value("#{'${ranks.brothel.barmaid}'.split(',')}")
    String[] brothelBarmaidNames;
    @Value("#{'${ranks.brothel.inviter}'.split(',')}")
    String[] brothelInviterRanks;
    @Value("#{'${ranks.brothel.waitress}'.split(',')}")
    String[] brothelWaitressRanks;
    @Value("#{'${ranks.brothel.stripper}'.split(',')}")
    String[] brothelStripperRanks;

    public JobService(JobLoader jobLoader){
        this.jobLoader = jobLoader;
        this.jobList = this.jobLoader.loadJobs();
        this.freeTime = jobList.get(0).getTasks().get(0);
        this.allTasks = calcAllTasks();
    }
    public Job getJobByName(String jobName){
        return jobList.stream().filter(job -> job.getName().equals(jobName)).findFirst().orElse(null);
    }
    private Map<String,Task> calcAllTasks(){
        Map<String,Task> allTasks = new HashMap<>();
        jobList.forEach(job -> job.getTasks().forEach(task -> allTasks.put(task.getName(),task)));
        return allTasks;
    }
    public JobCustomers handleEndTurn(JobCustomers jobCustomers){
        List<Customer> customers = jobCustomers.getCustomesList();
        for(Customer customer : customers){
            customer.setMoneySpent(5);
        }
        jobCustomers.setCustomesList(customers);
        return jobCustomers;
    }
    public static Double calculateTaskPerformance(Npc npc, String taskName) {
        return calculateTaskPerformance(npc, allTasks.get(taskName), null);
    }

    public static Double calculateTaskPerformance(Npc npc, Task task) {
        return calculateTaskPerformance(npc, task, null);
    }

    public static Double calculateTaskPerformance(Npc npc, Task task, String category) {
        OptionalDouble averageSkill;
        if (category != null) {
            List<String> relevantStats = new ArrayList<>();
            relevantStats.add(category);
            averageSkill = calculateAverageSkill(npc, relevantStats);
        } else {
            averageSkill = calculateAverageSkill(npc, task.getRelevantSkills());
        }

        OptionalDouble averageStat = calculateAverageStat(npc, task.getRelevantStats());
        Double result;
        if (averageSkill.isPresent() && averageStat.isPresent()) {
            result = (averageSkill.getAsDouble() + averageStat.getAsDouble()) / 2;
        } else {
            result = 0.0;
        }

        return result;
    }

    public static OptionalDouble calculateAverageStat(Npc npc, List<String> relevantStats) {
        List<Stat> girlRelevantStats = new ArrayList<Stat>(npc.getStats().values());

        //filter only the relevant skills
        girlRelevantStats = girlRelevantStats.stream().filter(skill -> relevantStats.contains(skill.getName())).collect(Collectors.toList());
        //calculate average
        return girlRelevantStats.stream().mapToDouble(stat -> stat.getValue()).average();
    }

    public static OptionalDouble calculateAverageSkill(Npc npc, List<String> relevantSkills) {
        List<Skill> girlRelevantSkills = new ArrayList<Skill>(npc.getSkills().values());
        //filter only the relevant skills
        girlRelevantSkills = girlRelevantSkills.stream().filter(stat -> relevantSkills.contains(stat.getName())).collect(Collectors.toList());

        //calculate average
        return girlRelevantSkills.stream().mapToDouble(skill -> skill.getValue()).average();
    }

    public String calculateAverageProficiencyScore(Npc npc) {


        List<String> relevantSkillsNight = npc.getTask().getRelevantSkills();


        OptionalDouble averageSkillNight = calculateAverageSkill(npc,relevantSkillsNight);

        return "Not a number";
    }

    public List<Double> getNpcProficiencyOnTask(String name, List<Npc> npcsWorkingOnTask) {
        List<Double> partialProf = new ArrayList<>();
        npcsWorkingOnTask.forEach(npc ->{
            partialProf.add(calculateTaskPerformance(npc,name));
        });
        Collections.sort(partialProf);
        return partialProf;
    }

    public String getJobNameByTask(Task task){
        return jobList.stream().filter(job -> job.getTasks().contains(task)).findFirst().orElse(null).getName();
    }

    public String[] getRankName(String taskName){
        String[] currentRankNames = {"Undefined","Undefined","Undefined","Undefined","Undefined","Undefined","Undefined","Undefined","Undefined","Undefined"};

        switch(taskName){
            case "Brothel Whore":
                currentRankNames = brothelWhoreRanks;
                break;
            case "Brothel Masseuse":
                currentRankNames = brothelMasseuseRanks;
                break;
            case "Brothel Stripper":
                currentRankNames = brothelStripperRanks;
                break;
        }
        return currentRankNames;
    }
}
