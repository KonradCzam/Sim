package com.example.Sim.Services;

import com.example.Sim.Model.Jobs.Job;
import com.example.Sim.Model.Jobs.Task;
import com.example.Sim.Model.Npc;
import com.example.Sim.Model.Raport.EndTurnRapport;
import com.example.Sim.Model.Raport.NpcRoot;
import com.example.Sim.Model.Raport.SingleEventRoot;
import com.example.Sim.Model.Skill;
import com.example.Sim.Model.TirednessSystem.WorkStatus;
import com.example.Sim.Utilities.FileUtility;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public class EndTurnService {
    Integer turn;
    Task dayTask;
    Task nightTask;
    @Resource
    private NpcService npcService;
    @Resource
    private FileUtility fileUtility;
    @Resource
    private DescriptionService descriptionService;
    @Resource
    private TirednessService tirednessService;
    @Value("#{'${categories.whore}'.split(',')}")
    private List<String> whoreCategories;

    public EndTurnService() {
        turn = 0;
    }

    public EndTurnRapport endTurn() {
        turn += 1;
        npcService.shuffleHirable();
        npcService.setHired(0);
        return prepareEndTurnReport();
    }

    private EndTurnRapport prepareEndTurnReport() {
        List<NpcRoot> npcRootList = new ArrayList<>();
        npcService.getHiredNpcs().forEach(npc -> npcRootList.add(endTurnForNpc(npc)));

        EndTurnRapport endTurnRapport = new EndTurnRapport();
        endTurnRapport.setNpcRootList(npcRootList);
        return endTurnRapport;
    }

    private NpcRoot endTurnForNpc(Npc npc) {

        dayTask = npc.getDayShift().getCurrentTask();
        nightTask = npc.getNightShift().getCurrentTask();

        WorkStatus workStatus = tirednessService.handleTiredness(npc);

        NpcRoot npcRoot = new NpcRoot();
        npcRoot.setName(npc.getName());
        npcRoot.setPath(npc.getPath());
        if (workStatus == WorkStatus.NORMAL || workStatus == WorkStatus.OVERWORKED || workStatus == WorkStatus.OVERWORKED_NEAR_DEATH) {
            npcRoot.setMessageLevel(workStatus.getLevel());
            return handleNormalWork(npcRoot, npc, workStatus);
        }
        else {
            npcRoot.setMessageLevel(workStatus.getLevel());
            npcRoot.setDescription(descriptionService.createNpcRootDescription(workStatus,npc.getName()));
            setNoWork(npcRoot);
            return npcRoot;
        }
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

    private NpcRoot handleNormalWork(NpcRoot npcRoot, Npc npc,WorkStatus workStatus) {
        npcRoot.setDayShiftRapport(createEventRoots(npc, dayTask));
        npcRoot.setNightShiftRapport(createEventRoots(npc, nightTask));

        Map<String, Integer> totalSkillGains = calculateTotalSkillGains(npcRoot.getDayShiftRapport(), npcRoot.getNightShiftRapport());

        npcRoot = calcTotalGoldAndExp(npcRoot);

        npcRoot.setDescription(descriptionService.createNpcRootDescription(dayTask, nightTask, npcRoot, totalSkillGains, workStatus));

        return npcRoot;
    }


    private List<SingleEventRoot> createEventRoots(Npc npc, Task task) {

        List<SingleEventRoot> singleEventRootList = new ArrayList<>();
        Integer noOfCustomers = 1;

        if ("multiple".equals(task.getType())) {
            noOfCustomers = npc.getStats().get("Constitution").getValue() / 10;
            if (noOfCustomers == 0) {
                noOfCustomers = 1;
            }
        }
        for (int i = 0; i < noOfCustomers; i++) {
            SingleEventRoot singleEventRoot = new SingleEventRoot();
            singleEventRoot = createSingleEventRoot(npc, task, singleEventRoot);
            singleEventRootList.add(singleEventRoot);
        }


        return singleEventRootList;
    }

    private Map<String, Integer> calculateTotalSkillGains(List<SingleEventRoot> singleEventRootList, List<SingleEventRoot> singleEventRootList2) {
        List<String> skillGains = new ArrayList<>();
        singleEventRootList.forEach(singleEventRoot -> {
            if (singleEventRoot.getSkillsGain() != null) {
                skillGains.add(singleEventRoot.getSkillsGain());
            }
        });
        singleEventRootList2.forEach(singleEventRoot -> {
            if (singleEventRoot.getSkillsGain() != null) {
                skillGains.add(singleEventRoot.getSkillsGain());
            }
        });
        Set<String> distinct = new HashSet<>(skillGains);
        Map<String, Integer> skillLvlUpped = new HashMap<String, Integer>();
        distinct.forEach(skillGain -> skillLvlUpped.put(skillGain, Collections.frequency(skillGains, skillGain)));

        return skillLvlUpped;
    }

    private SingleEventRoot createSingleEventRoot(Npc npc, Task task, SingleEventRoot singleEventRoot) {

        singleEventRoot.setJob(task.getName());
        singleEventRoot.setPath(npc.getPath());

        singleEventRoot = selectCategory(npc, task, singleEventRoot);
        singleEventRoot = handleGoldEarned(npc, task, singleEventRoot);
        singleEventRoot = handleProgressGain(npc, singleEventRoot);
        singleEventRoot.setExpGain(task.getExpGain());
        singleEventRoot = handleExpGain(npc, task, singleEventRoot);
        singleEventRoot.setDescription(descriptionService.createTaskDescription(npc, task, singleEventRoot));
        return singleEventRoot;
    }

    private NpcRoot calcTotalGoldAndExp(NpcRoot npcRoot) {
        Integer money = 0;
        Integer exp = 0;

        List<SingleEventRoot> dayShiftRapport = npcRoot.getDayShiftRapport();

        List<SingleEventRoot> nightShiftRapport = npcRoot.getNightShiftRapport();
        for (int i = 0; i < dayShiftRapport.size(); i++) {
            money += dayShiftRapport.get(i).getMoneyEarned();
            exp += dayShiftRapport.get(i).getExpGain();
        }
        npcRoot.setDayExpGain(exp);
        npcRoot.setDayMoneyEarned(money);
        npcRoot.setMoneyEarned(money);
        exp = 0;
        money = 0;
        for (int i = 0; i < nightShiftRapport.size(); i++) {
            money += nightShiftRapport.get(i).getMoneyEarned();
            exp += nightShiftRapport.get(i).getExpGain();
        }
        npcRoot.setNightExpGain(exp);
        npcRoot.setNightMoneyEarned(money);
        npcRoot.setMoneyEarned(npcRoot.getMoneyEarned() + money);
        return npcRoot;
    }

    private SingleEventRoot handleGoldEarned(Npc npc, Task task, SingleEventRoot singleEventRoot) {
        Double taskPerformance;
        Double sumEarned;
        taskPerformance = Job.calculateTaskPerformance(npc, task, singleEventRoot.getCategory());
        sumEarned = (task.getMoneyCoefficient() * taskPerformance);
        sumEarned += ThreadLocalRandom.current().nextInt(-15, 15);
        singleEventRoot.setMoneyEarned(Math.max(sumEarned.intValue(), 0));
        return singleEventRoot;
    }

    private SingleEventRoot selectCategory(Npc npc, Task task, SingleEventRoot singleEventRoot) {
        List<String> possibleCategories = fileUtility.checkNpcTypes(npc.getPath());
        String selectedSkill = task.getRelevantSkills().get(ThreadLocalRandom.current().nextInt(task.getRelevantSkills().size()));
        String selectedCat;
        if (npc.getSkills().get(selectedSkill.toLowerCase()) != null) {
            selectedCat = npc.getSkills().get(selectedSkill.toLowerCase()).getCategory();

            if (!possibleCategories.contains(selectedCat))
                selectedCat = task.getDefaultCat();
        } else {
            selectedCat = task.getDefaultCat();

        }
        singleEventRoot.setSkill(selectedSkill);
        singleEventRoot.setCategory(selectedCat);
        return singleEventRoot;
    }

    private SingleEventRoot handleProgressGain(Npc npc, SingleEventRoot singleEventRoot) {
        String description = "";
        String category = singleEventRoot.getCategory();
        String skillName = singleEventRoot.getSkill();
        Skill trained = npc.getSkills().get(singleEventRoot.getSkill().toLowerCase());
        if (trained != null) {
            Double progress = 10.0 / trained.getValue();

            progress = Math.min(progress, 1.0);

            trained.changeProgress(progress);

            if (trained.checkSkillLvlUp(category)) {
                singleEventRoot.setDescription(descriptionService.createSingleRootNodeDescription(npc.getName(), singleEventRoot));
                singleEventRoot.setSkillsGain(skillName);
            }
        }
        singleEventRoot.setDescription(description);
        return singleEventRoot;
    }


    private SingleEventRoot handleExpGain(Npc npc, Task task, SingleEventRoot singleEventRoot) {

        return singleEventRoot;
    }

    public SingleEventRoot checkLvlUp(Npc npc, SingleEventRoot singleEventRoot) {

        return singleEventRoot;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
    }
}
