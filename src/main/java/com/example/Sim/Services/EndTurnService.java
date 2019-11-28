// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.Services;

import com.example.Sim.Model.Jobs.JobCustomers;
import com.example.Sim.Model.Jobs.Task;
import com.example.Sim.Model.NPC.Npc;
import com.example.Sim.Model.NPC.Skill;
import com.example.Sim.Model.Raport.*;
import com.example.Sim.Model.TirednessSystem.WorkStatus;
import com.example.Sim.Utilities.FileUtility;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class EndTurnService
{
    Integer turn;
    Task dayTask;
    Task nightTask;
    Boolean presentOld;
    Integer earnedLastTurn;
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
        this.presentOld = false;
        this.turn = 0;
    }
    
    public EndTurnRapport endTurn() {
        ++this.turn;
        this.npcService.shuffleHirable();
        this.npcService.setHired(Integer.valueOf(0));
        final GirlEndTurnRapport girlEndTurnRapport = this.createGirlEndTurnRapport();
        final FinanceEndTurnRapport financeEndTurnRapport = this.createFinanceEndTurnRapport();
        this.earnedLastTurn = this.calcTotalEarned(financeEndTurnRapport.getFinanceRootList());
        this.playerService.changeGold(this.earnedLastTurn);
        return new EndTurnRapport(financeEndTurnRapport, girlEndTurnRapport);
    }
    
    private FinanceEndTurnRapport createFinanceEndTurnRapport() {
        this.jobService.calculateAllJobsStats();
        this.customerService.generateAllCustomers();
        final FinanceEndTurnRapport financeEndTurnRapport = this.prepareFinancialEndTurnRapport();
        return financeEndTurnRapport;
    }
    
    private FinanceEndTurnRapport prepareFinancialEndTurnRapport() {
        final List<JobRoot> jobRoots = new ArrayList<JobRoot>();
        final Map<String, JobCustomers> customersDividedByTierAndShift = this.customerService.getDividedCustomersByPopularity();
        for (JobCustomers jobsCustomers : customersDividedByTierAndShift.values()) {
            jobsCustomers = this.jobService.handleEndTurn(jobsCustomers);
        }
        for (final Map.Entry<String, JobCustomers> entry : customersDividedByTierAndShift.entrySet()) {
            final JobRoot jobRoot = new JobRoot((String)entry.getKey(), (JobCustomers)entry.getValue());
            jobRoots.add(jobRoot);
        }
        final FinanceEndTurnRapport financeEndTurnRapport = new FinanceEndTurnRapport();
        financeEndTurnRapport.setFinanceRootList((List)jobRoots);
        return financeEndTurnRapport;
    }
    
    private GirlEndTurnRapport createGirlEndTurnRapport() {
        final List<NpcRoot> npcRootList = new ArrayList<NpcRoot>();
        this.npcService.getHiredNpcs().forEach(this::lambda$createGirlEndTurnRapport$0);
        final GirlEndTurnRapport girlEndTurnRapport = new GirlEndTurnRapport();
        girlEndTurnRapport.setNpcRootList((List)npcRootList);
        return girlEndTurnRapport;
    }
    
    private NpcRoot endTurnForNpc(final Npc npc) {
        final NpcRoot npcRoot = new NpcRoot();
        npcRoot.setName(npc.getName());
        npcRoot.setPath(npc.getPath());
        npcRoot.setDescription("");
        this.dayTask = npc.getDayShift();
        this.nightTask = npc.getNightShift();
        final WorkStatus dayWorkStatus = this.calcWorkStatus(npc, this.dayTask);
        npcRoot.setDayWorkStatus(dayWorkStatus);
        this.handleShift(dayWorkStatus, npcRoot, npc, " day ");
        final WorkStatus nightWorkStatus = this.calcWorkStatus(npc, this.nightTask);
        npcRoot.setNightWorkStatus(nightWorkStatus);
        this.handleShift(nightWorkStatus, npcRoot, npc, " night ");
        final Map<String, Integer> changes = (Map<String, Integer>)this.factorService.handleEndTurnFactors(npc);
        npcRoot.setObedience(Integer.valueOf(changes.get("Obedience")));
        npcRoot.setLove(Integer.valueOf(changes.get("Love")));
        this.descriptionService.addNpcRootDescription(npcRoot, npc);
        return npcRoot;
    }
    
    private NpcRoot handleShift(final WorkStatus workStatus, NpcRoot npcRoot, final Npc npc, final String shift) {
        npcRoot.setMessageLevel(workStatus.getLevel());
        if (workStatus == WorkStatus.DEAD_TIRED) {
            this.setNoWork(npcRoot);
            this.npcService.killNpc(npc);
            return npcRoot;
        }
        if (workStatus == WorkStatus.NORMAL || workStatus == WorkStatus.OVERWORKED || workStatus == WorkStatus.OVERWORKED_NEAR_DEATH) {
            npcRoot = this.handleNormalWork(npcRoot, npc, shift);
        }
        else {
            this.setNoWork(npcRoot);
        }
        return npcRoot;
    }
    
    private NpcRoot handleNormalWork(final NpcRoot npcRoot, final Npc npc, final String shift) {
        if (" day ".equals(shift)) {
            npcRoot.setDayShiftRapport(this.createEventRoots(npc, this.dayTask));
        }
        else {
            npcRoot.setNightShiftRapport(this.createEventRoots(npc, this.nightTask));
        }
        return npcRoot;
    }
    
    private WorkStatus calcWorkStatus(final Npc npc, final Task task) {
        final ObedienceService obedienceService = this.obedienceService;
        final WorkStatus response = ObedienceService.calculateIfRefuse(npc, task);
        if (response == WorkStatus.NORMAL) {
            return this.tirednessService.handleTiredness(npc, task);
        }
        return response;
    }
    
    private NpcRoot setNoWork(final NpcRoot npcRoot) {
        npcRoot.setMoneyEarned(Integer.valueOf(0));
        npcRoot.setDayMoneyEarned(Integer.valueOf(0));
        npcRoot.setNightMoneyEarned(Integer.valueOf(0));
        npcRoot.setNightExpGain(Integer.valueOf(0));
        npcRoot.setDayExpGain(Integer.valueOf(0));
        npcRoot.setDayShiftRapport((List)new ArrayList());
        npcRoot.setNightShiftRapport((List)new ArrayList());
        return npcRoot;
    }
    
    private List<SingleEventRoot> createEventRoots(final Npc npc, final Task task) {
        final List<SingleEventRoot> singleEventRootList = new ArrayList<SingleEventRoot>();
        Integer noOfCustomers = 1;
        if ("multiple".equals(task.getType())) {
            noOfCustomers = npc.getStat("Constitution").getValue() / 10;
            if (noOfCustomers == 0) {
                noOfCustomers = 1;
            }
        }
        final String jobName = this.jobService.getJobNameByTask(task);
        npc.addJobExp(jobName, task, noOfCustomers);
        for (int i = 0; i < noOfCustomers; ++i) {
            SingleEventRoot singleEventRoot = new SingleEventRoot();
            singleEventRoot = this.createSingleEventRoot(npc, task, singleEventRoot);
            singleEventRootList.add(singleEventRoot);
        }
        return singleEventRootList;
    }
    
    private SingleEventRoot createSingleEventRoot(final Npc npc, final Task task, SingleEventRoot singleEventRoot) {
        singleEventRoot.setJob(task.getName());
        singleEventRoot.setPath(npc.getPath());
        singleEventRoot.setNpcName(npc.getName());
        if ("training".equals(task.getType())) {
            singleEventRoot = this.handleTraining(npc, task, singleEventRoot);
        }
        else {
            singleEventRoot = this.selectCategory(npc, task, singleEventRoot);
            singleEventRoot = this.handleTips(npc, task, singleEventRoot);
            singleEventRoot = this.handleProgressGain(npc, singleEventRoot);
            singleEventRoot = this.handleProgressGain(npc, singleEventRoot);
        }
        return singleEventRoot;
    }
    
    private SingleEventRoot handleTraining(final Npc npc, final Task task, final SingleEventRoot singleEventRoot) {
        singleEventRoot.setCategory(task.getDefaultCat());
        singleEventRoot.setMoneyEarned(Integer.valueOf(task.getMoneyCoefficient().intValue()));
        final Integer deltaStat = task.getValue();
        final String statName = task.getRelevantStats().get(0);
        npc.getStat(statName).changeValue(deltaStat);
        final Map<String, Double> statProgress = new HashMap<String, Double>();
        statProgress.put(statName, new Double(deltaStat));
        singleEventRoot.setStatProgress((Map)statProgress);
        return singleEventRoot;
    }
    
    private SingleEventRoot handleTips(final Npc npc, final Task task, final SingleEventRoot singleEventRoot) {
        final JobService jobService = this.jobService;
        final Double taskPerformance = JobService.calculateTaskPerformance(npc, task, singleEventRoot.getCategory());
        Double sumEarned = task.getMoneyCoefficient() * taskPerformance;
        sumEarned += (Double)ThreadLocalRandom.current().nextInt(-15, 15);
        singleEventRoot.setMoneyEarned(Integer.valueOf(Math.max(sumEarned.intValue(), 0)));
        return singleEventRoot;
    }
    
    private SingleEventRoot selectCategory(final Npc npc, final Task task, final SingleEventRoot singleEventRoot) {
        final List<String> possibleCategories = (List<String>)this.fileUtility.checkNpcTypes(npc.getPath());
        String selectedSkill = null;
        if (task.getRelevantSkills().size() != 0) {
            selectedSkill = task.getRelevantSkills().get(ThreadLocalRandom.current().nextInt(task.getRelevantSkills().size()));
        }
        String selectedCat;
        if (selectedSkill != null) {
            if (npc.getSkills().get(selectedSkill) != null) {
                selectedCat = npc.getSkills().get(selectedSkill).getCategory();
                if (!possibleCategories.contains(selectedCat)) {
                    selectedCat = task.getDefaultCat();
                }
            }
            else {
                selectedCat = task.getDefaultCat();
            }
        }
        else {
            selectedCat = "profile";
        }
        singleEventRoot.setSkill(selectedSkill);
        singleEventRoot.setCategory(selectedCat);
        return singleEventRoot;
    }
    
    private SingleEventRoot handleProgressGain(final Npc npc, final SingleEventRoot singleEventRoot) {
        final String description = "";
        final String category = singleEventRoot.getCategory();
        final String skillName = singleEventRoot.getSkill();
        if (skillName != null) {
            final Skill trainedSkill = npc.getSkills().get(singleEventRoot.getSkill());
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
    
    public void setTurn(final Integer turn) {
        this.turn = turn;
    }
    
    private Integer calcTotalEarned(final List<JobRoot> financeRootList) {
        Integer result = 0;
        for (final JobRoot jobRoot : financeRootList) {
            result += jobRoot.getMoneyEarned();
        }
        return result;
    }
    
    public void setPresentOld(final Boolean presentOld) {
        this.presentOld = presentOld;
    }
    
    public Integer getTurn() {
        return this.turn;
    }
    
    public Task getDayTask() {
        return this.dayTask;
    }
    
    public Task getNightTask() {
        return this.nightTask;
    }
    
    public Boolean getPresentOld() {
        return this.presentOld;
    }
    
    public Integer getEarnedLastTurn() {
        return this.earnedLastTurn;
    }
    
    public NpcService getNpcService() {
        return this.npcService;
    }
    
    public FileUtility getFileUtility() {
        return this.fileUtility;
    }
    
    public DescriptionService getDescriptionService() {
        return this.descriptionService;
    }
    
    public TirednessService getTirednessService() {
        return this.tirednessService;
    }
    
    public ObedienceService getObedienceService() {
        return this.obedienceService;
    }
    
    public JobService getJobService() {
        return this.jobService;
    }
    
    public PlayerService getPlayerService() {
        return this.playerService;
    }
    
    public FactorService getFactorService() {
        return this.factorService;
    }
    
    public CustomerService getCustomerService() {
        return this.customerService;
    }
    
    public List<String> getWhoreCategories() {
        return (List<String>)this.whoreCategories;
    }
}
