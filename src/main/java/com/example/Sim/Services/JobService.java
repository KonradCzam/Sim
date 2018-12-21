package com.example.Sim.Services;

import com.example.Sim.Exceptions.NoConcernFoundInJob;
import com.example.Sim.Model.Customer;
import com.example.Sim.Model.Jobs.Job;
import com.example.Sim.Model.Jobs.JobCustomers;
import com.example.Sim.Model.Jobs.JobStat;
import com.example.Sim.Model.Jobs.Task;
import com.example.Sim.Model.NPC.Npc;
import com.example.Sim.Model.NPC.Skill;
import com.example.Sim.Model.NPC.Stat;
import com.example.Sim.Utilities.JobLoader;
import lombok.Getter;
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

        List<String> relevantSkillsDay = npc.getDayShift().getRelevantSkills();
        List<String> relevantStatsDay = npc.getDayShift().getRelevantStats();

        List<String> relevantSkillsNight = npc.getDayShift().getRelevantSkills();
        List<String> relevantStatsNight = npc.getDayShift().getRelevantStats();


        OptionalDouble averageSkillDay = calculateAverageSkill(npc,relevantSkillsDay);
        OptionalDouble averageStatDay = calculateAverageStat(npc,relevantStatsDay);

        OptionalDouble averageSkillNight = calculateAverageSkill(npc,relevantSkillsNight);
        OptionalDouble averageStatNight = calculateAverageStat(npc,relevantStatsNight);

        if(averageSkillDay.isPresent() && averageStatDay.isPresent() && averageSkillNight.isPresent() && averageStatNight.isPresent()){
            Double result = (averageSkillDay.getAsDouble() + averageStatDay.getAsDouble() + averageSkillNight.getAsDouble() + averageStatNight.getAsDouble())/4.0;
            Integer resultInt = result.intValue();
            return resultInt.toString();
        }
        return "Not a number";
    }

    public void calculateAllJobsStats(){
        jobList.forEach(job -> {
            calculateJobAllStats(job);
        });
    }
    public void calculateJobAllStats(Job job){
        job.getJobStats().forEach(jobStat ->{
            jobStat.setDayValue(this.calculateJobStat(jobStat.getStatName(),true,job));
            jobStat.setNightValue(this.calculateJobStat(jobStat.getStatName(),false,job));
        } );
    }
    public Double calculateJobStat(String statName,Boolean day,Job job) {
        Double response = 0.0;
        try {
            JobStat jobStat = getJobStatByName(statName,job);
            for (Map.Entry<String, Integer> entry : jobStat.getTasks().entrySet())
            {
                List<Npc> npcsWorkingOnTask = getNpcsWorkingOnTask(entry.getKey(), day);
                List<Double> partialProf = getNpcProficiencyOnTask(entry.getKey(), npcsWorkingOnTask);
                response += getWeightedStat(partialProf) * (entry.getValue() / 100.0);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return response;


    }
    private JobStat getJobStatByName(String name,Job job) throws NoConcernFoundInJob {
        JobStat jobStat = job.getJobStats().stream().filter(jobsStat -> jobsStat.getStatName().equals(name)).findFirst().orElse(null);
        if (jobStat == null)
            throw new NoConcernFoundInJob();
        return jobStat;
    }

    private List<Npc> getNpcsWorkingOnTask(String name, Boolean day) {
        List<Npc> npcsWorkingOnTask = new ArrayList();
        if(day)
            npcsWorkingOnTask = npcService.getHiredNpcs().stream().filter(npc -> npc.getDayShift().getName().equals(name)).collect(Collectors.toList());
        else
            npcsWorkingOnTask = npcService.getHiredNpcs().stream().filter(npc -> npc.getNightShift().getName().equals(name)).collect(Collectors.toList());
        return npcsWorkingOnTask;
    }

    private Double getWeightedStat(List<Double> partialProf) {
        Double result = 0.0;
        Collections.sort(partialProf);
        Collections.reverse(partialProf);
        Integer size = partialProf.size();
        if (size > 0){
            result += partialProf.get(0) * 0.5;
        }
        if (size > 1){
            result += partialProf.get(1) * 0.25;
        }
        if (size > 2){
            result += partialProf.get(2) * 0.25;
        }
        if (size > 3){
            result += partialProf.get(3) * 0.1;
        }
        if (size > 4){
            result += partialProf.get(4) * 0.1;
        }
        if (size > 5){
            for (int i =5; i<size;i++)
                result += partialProf.get(i) * 0.05;
        }
        return result;
    }

    public List<Double> getNpcProficiencyOnTask(String name, List<Npc> npcsWorkingOnTask) {
        List<Double> partialProf = new ArrayList<>();
        npcsWorkingOnTask.forEach(npc ->{
            partialProf.add(calculateTaskPerformance(npc,name));
        });
        Collections.sort(partialProf);
        return partialProf;
    }
    public JobCustomers handleEndTurn(JobCustomers jobCustomers){
        List<Customer> tempCustList = new ArrayList<>();

        tempCustList = jobCustomers.getDayLowCustomesList();
        jobCustomers.setDayLowCustomesList(calcCustomerList( tempCustList,true));

        tempCustList = jobCustomers.getDayMidCustomesList();
        jobCustomers.setDayMidCustomesList(calcCustomerList( tempCustList,true));

        tempCustList = jobCustomers.getNightHighCustomesList();
        jobCustomers.setDayHighCustomesList(calcCustomerList( tempCustList,true));

        tempCustList = jobCustomers.getNightLowCustomesList();
        jobCustomers.setNightLowCustomesList(calcCustomerList( tempCustList,false));

        tempCustList = jobCustomers.getNightMidCustomesList();
        jobCustomers.setNightMidCustomesList(calcCustomerList( tempCustList,false));

        tempCustList = jobCustomers.getNightHighCustomesList();
        jobCustomers.setNightHighCustomesList(calcCustomerList( tempCustList,false));


        return jobCustomers;
    }

    private List<Customer> calcCustomerList(List<Customer> tempCustList,Boolean day) {
        tempCustList.stream().forEach(customer -> {
            setCustomerHappiness(customer,day);
            customer.setMoneySpent(calculateAmountSpent(customer));
        });
        return tempCustList;
    }

    private Integer calculateAmountSpent(Customer customer){
        Double result =  (customer.getMoney() * customer.getHappiness());
        if(customer.getTier().equals("low") && customer.getHappiness() == 0.5)
            return 0;
        if(customer.getTier().equals("mid") && customer.getHappiness() == 0.25)
            return 0;
        return result.intValue();
    }
    private Customer setCustomerHappiness(Customer customer,Boolean day) {
        Double happiness = 0.0;
        Job targetJob = getJobByName(customer.getTargetJob());
        Double mainConcernStat;
        Double secondaryConcernStat;
        String mainConcern = customer.getMainConcern().getName();
        String secondaryConcern = customer.getMainConcern().getName();
        try {
        if(day){

                mainConcernStat = targetJob.getJobStat(mainConcern).getDayValue();
                secondaryConcernStat = targetJob.getJobStat(secondaryConcern).getDayValue();

        }else{
            mainConcernStat = targetJob.getJobStat(mainConcern).getNightValue();
            secondaryConcernStat = targetJob.getJobStat(secondaryConcern).getDayValue();
        }

        happiness = (mainConcernStat * 0.75 + secondaryConcernStat * 0.25)/100.0;
        }catch (Exception e){
            System.out.println(targetJob.getName() + "  " + customer.getMainConcern().getName());
            e.printStackTrace();
        }
        if(customer.getTier().equals("low")){
            happiness+= 0.5;
        }else if (customer.getTier().equals("mid")){
            happiness+= 0.25;
        }
        customer.setHappiness(happiness);
        return customer;
    }
    public String getJobNameByTask(Task task){
        String taskname = task.getName();
        return jobList.stream().filter(job -> job.getTasks().contains(task)).findFirst().orElse(null).getName();
    }
}
