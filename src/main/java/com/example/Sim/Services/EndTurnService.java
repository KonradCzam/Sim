package com.example.Sim.Services;

import com.example.Sim.Model.Jobs.JobCustomers;
import com.example.Sim.Model.Jobs.Task;
import com.example.Sim.Model.NPC.Npc;
import com.example.Sim.Model.NPC.Skill;
import com.example.Sim.Model.Raport.*;
import com.example.Sim.Model.TirednessSystem.WorkStatus;
import com.example.Sim.Utilities.FileUtility;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public class EndTurnService {
    Integer turn;
    Task dayTask;
    Task nightTask;
    Boolean presentOld = false;
    Integer earnedLastTurn ;
    @Resource
    private NpcService npcService;
    @Resource
    private FileUtility fileUtility;
    @Resource
    private DescriptionService descriptionService;
    @Resource
    private TirednessService tirednessService;
    @Resource
    private ObedienceService obedienceService;
    @Resource
    private transient JobService jobService;
    @Resource
    private transient PlayerService playerService;
    @Resource
    private transient FactorService factorService;

    @Resource
    private transient CustomerService customerService;

    @Value("#{'${categories.whore}'.split(',')}")
    private List<String> whoreCategories;

    public EndTurnService() {
        turn = 0;
    }

    public EndTurnRapport endTurn() {
        turn += 1;
        npcService.shuffleHirable();
        npcService.setHired(0);
        GirlEndTurnRapport girlEndTurnRapport = createGirlEndTurnRapport();
        FinanceEndTurnRapport financeEndTurnRapport = createFinanceEndTurnRapport();
        this.earnedLastTurn = calcTotalEarned(financeEndTurnRapport.getFinanceRootList());
        playerService.changeGold(earnedLastTurn);
        return new EndTurnRapport(financeEndTurnRapport,girlEndTurnRapport);
    }



    private FinanceEndTurnRapport createFinanceEndTurnRapport() {
        jobService.calculateAllJobsStats();
        customerService.generateAllCustomers();
        FinanceEndTurnRapport financeEndTurnRapport = prepareFinancialEndTurnRapport();
        return financeEndTurnRapport;
    }

    private FinanceEndTurnRapport prepareFinancialEndTurnRapport() {
        List<JobRoot> jobRoots = new ArrayList<>();
        Map<String, JobCustomers> customersDividedByTierAndShift = customerService.getDividedCustomersByPopularity();
        //Calculate happines and gold spent
        for(JobCustomers jobsCustomers : customersDividedByTierAndShift.values()){
            jobsCustomers = jobService.handleEndTurn(jobsCustomers);
        }
        //Reformat to JobRoot nodes
        for (Map.Entry<String,JobCustomers> entry : customersDividedByTierAndShift.entrySet()) {
            JobRoot jobRoot = new JobRoot(entry.getKey(),entry.getValue());
            jobRoots.add(jobRoot);
        }
        FinanceEndTurnRapport financeEndTurnRapport = new FinanceEndTurnRapport();
        financeEndTurnRapport.setFinanceRootList(jobRoots);
        return financeEndTurnRapport;
    }


    private GirlEndTurnRapport createGirlEndTurnRapport() {
        List<NpcRoot> npcRootList = new ArrayList<>();
        npcService.getHiredNpcs().forEach(npc -> npcRootList.add(endTurnForNpc(npc)));

        GirlEndTurnRapport girlEndTurnRapport = new GirlEndTurnRapport();
        girlEndTurnRapport.setNpcRootList(npcRootList);
        return girlEndTurnRapport;
    }

    private NpcRoot endTurnForNpc(Npc npc) {
        NpcRoot npcRoot = new NpcRoot();
        npcRoot.setName(npc.getName());
        npcRoot.setPath(npc.getPath());
        npcRoot.setDescription("");
        dayTask = npc.getDayShift();
        nightTask = npc.getNightShift();

        WorkStatus dayWorkStatus = calcWorkStatus(npc, dayTask);
        npcRoot.setDayWorkStatus(dayWorkStatus);

        handleShift(dayWorkStatus, npcRoot, npc, " day ");

        WorkStatus nightWorkStatus = calcWorkStatus(npc, nightTask);
        npcRoot.setNightWorkStatus(nightWorkStatus);

        handleShift(nightWorkStatus, npcRoot, npc, " night ");

        factorService.handleEndTurnFactors(npc);

        descriptionService.addNpcRootDescription(npcRoot,npc);

        return npcRoot;
    }

    private NpcRoot handleShift(WorkStatus workStatus,  NpcRoot npcRoot, Npc npc, String shift) {

        npcRoot.setMessageLevel(workStatus.getLevel());

        if (workStatus == WorkStatus.DEAD_TIRED){
            setNoWork(npcRoot);
            return npcRoot;
        }
        if (workStatus == WorkStatus.NORMAL || workStatus == WorkStatus.OVERWORKED || workStatus == WorkStatus.OVERWORKED_NEAR_DEATH) {
            npcRoot = handleNormalWork(npcRoot, npc, shift);
        } else{
            setNoWork(npcRoot);
        }

        return npcRoot;
    }
    private NpcRoot handleNormalWork(NpcRoot npcRoot, Npc npc, String shift) {
        if (" day ".equals(shift)) {
            npcRoot.setDayShiftRapport(createEventRoots(npc, dayTask));
        } else {
            npcRoot.setNightShiftRapport(createEventRoots(npc, nightTask));
        }
        return npcRoot;
    }
    private WorkStatus calcWorkStatus(Npc npc, Task task) {
        WorkStatus response = obedienceService.calculateIfRefuse(npc, task);
        if (response == WorkStatus.NORMAL)
            return tirednessService.handleTiredness(npc,task);
        else
            return response;

    }

    private NpcRoot setNoWork(NpcRoot npcRoot) {
        npcRoot.setMoneyEarned(0);
        npcRoot.setDayMoneyEarned(0);
        npcRoot.setNightMoneyEarned(0);
        npcRoot.setNightExpGain(0);
        npcRoot.setDayExpGain(0);
        npcRoot.setDayShiftRapport(new ArrayList<SingleEventRoot>());
        npcRoot.setNightShiftRapport(new ArrayList<SingleEventRoot>());

        return npcRoot;
    }

    private List<SingleEventRoot> createEventRoots(Npc npc, Task task) {

        List<SingleEventRoot> singleEventRootList = new ArrayList<>();
        Integer noOfCustomers = 1;

        if ("multiple".equals(task.getType())) {
            noOfCustomers = npc.getStat("Constitution").getEffectiveValue() / 10;
            if (noOfCustomers == 0) {
                noOfCustomers = 1;
            }
        }
        String jobName = jobService.getJobNameByTask(task);
        npc.addJobExp(jobName,task,noOfCustomers);
        for (int i = 0; i < noOfCustomers; i++) {
            SingleEventRoot singleEventRoot = new SingleEventRoot();
            singleEventRoot = createSingleEventRoot(npc, task, singleEventRoot);
            singleEventRootList.add(singleEventRoot);
        }
        return singleEventRootList;
    }



    private SingleEventRoot createSingleEventRoot(Npc npc, Task task, SingleEventRoot singleEventRoot) {

        singleEventRoot.setJob(task.getName());
        singleEventRoot.setPath(npc.getPath());
        singleEventRoot.setNpcName(npc.getName());
        if ("training".equals(task.getType())) {
            singleEventRoot = handleTraining(npc, task, singleEventRoot);
        } else {
            //Category is the sort of picture that will be displayed
            singleEventRoot = selectCategory(npc, task, singleEventRoot);
            singleEventRoot = handleProgressGain(npc, singleEventRoot);
        }
        return singleEventRoot;
    }

    private SingleEventRoot handleTraining(Npc npc, Task task, SingleEventRoot singleEventRoot) {
        singleEventRoot.setCategory(task.getDefaultCat());
        singleEventRoot.setMoneyEarned(task.getMoneyCoefficient().intValue());
        Integer deltaStat = task.getValue();
        String statName = task.getRelevantStats().get(0);
        npc.getStat(statName).changeValue(deltaStat);
        Map<String, Double> statProgress = new HashMap<String, Double>();
        statProgress.put(statName, new Double(deltaStat));
        singleEventRoot.setStatProgress(statProgress);
        return singleEventRoot;
    }



    private SingleEventRoot selectCategory(Npc npc, Task task, SingleEventRoot singleEventRoot) {
        List<String> possibleCategories = fileUtility.checkNpcTypes(npc.getPath());
        String selectedSkill = null;
        if (task.getRelevantSkills().size() != 0) {
            selectedSkill = task.getRelevantSkills().get(ThreadLocalRandom.current().nextInt(task.getRelevantSkills().size()));
        }

        String selectedCat;
        if (selectedSkill != null) {
            if (npc.getSkills().get(selectedSkill) != null) {
                selectedCat = npc.getSkills().get(selectedSkill).getCategory();

                if (!possibleCategories.contains(selectedCat))
                    selectedCat = task.getDefaultCat();
            } else {
                selectedCat = task.getDefaultCat();

            }

        } else {
            selectedCat = "profile";
        }
        singleEventRoot.setSkill(selectedSkill);
        singleEventRoot.setCategory(selectedCat);
        return singleEventRoot;
    }

    private SingleEventRoot handleProgressGain(Npc npc, SingleEventRoot singleEventRoot) {
        String description = "";
        String category = singleEventRoot.getCategory();
        String skillName = singleEventRoot.getSkill();
        if (skillName != null) {
            Skill trainedSkill = npc.getSkills().get(singleEventRoot.getSkill());

            singleEventRoot.setDescription(description);
            if (trainedSkill != null) {
                Double progress = 10.0 / trainedSkill.getValue();

                progress = Math.min(progress, 1.0);

                trainedSkill.changeProgress(progress);

                if (trainedSkill.checkSkillLvlUp(category)) {

                    singleEventRoot.setSkillsGain(skillName);
                }
            }
        }

        return singleEventRoot;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
    }

    private Integer calcTotalEarned(List<JobRoot> financeRootList) {
        Integer result = 0;
        for(JobRoot jobRoot : financeRootList){
            result+=jobRoot.getMoneyEarned();
        }
        return result;
    }

    public void setPresentOld(Boolean presentOld) {
        this.presentOld = presentOld;
    }
}
