package com.example.Sim.Services;

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
    @Resource
    private transient JobService jobService;
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
        NpcRoot npcRoot = new NpcRoot();
        npcRoot.setName(npc.getName());
        npcRoot.setPath(npc.getPath());
        npcRoot.setDescription("");
        dayTask = npc.getDayShift();
        nightTask = npc.getNightShift();

        WorkStatus dayWorkStatus = calcWorkStatus(npc, dayTask);
        WorkStatus nightWorkStatus = calcWorkStatus(npc, nightTask);

        handleShift(dayWorkStatus, dayTask, npcRoot, npc, " day ");
        handleShift(nightWorkStatus, nightTask, npcRoot, npc, " night ");
        return npcRoot;
    }

    private NpcRoot handleShift(WorkStatus workStatus, Task task, NpcRoot npcRoot, Npc npc, String shift) {
        if (workStatus == WorkStatus.NORMAL || workStatus == WorkStatus.OVERWORKED || workStatus == WorkStatus.OVERWORKED_NEAR_DEATH) {
            npcRoot.setMessageLevel(workStatus.getLevel());
            npcRoot = handleNormalWork(npcRoot, npc, workStatus, shift);
            Map<String, Integer> totalSkillGains = null;
            if (" night ".equals(shift)) {
                totalSkillGains = calculateShiftSkillGains(npcRoot.getDayShiftRapport());
                totalSkillGains.putAll(calculateShiftSkillGains(npcRoot.getNightShiftRapport()));
            }
            npcRoot.setDescription(npcRoot.getDescription() + descriptionService.createPartialNpcRootDescription(task, shift, npcRoot, totalSkillGains, workStatus));
            return npcRoot;
        } else {
            npcRoot.setMessageLevel(workStatus.getLevel());
            npcRoot.setDescription(npcRoot.getDescription() + descriptionService.createPartialNpcRootDescription(workStatus, npc.getName()));
            setNoWork(npcRoot);
            return npcRoot;
        }
    }

    private WorkStatus calcWorkStatus(Npc npc, Task task) {
        WorkStatus response = jobService.calculateIfRefuse(npc, task);
        if (response == WorkStatus.NORMAL)
            return tirednessService.handleTiredness(npc);
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

    private NpcRoot handleNormalWork(NpcRoot npcRoot, Npc npc, WorkStatus workStatus, String shift) {
        if (" day ".equals(shift)) {
            npcRoot.setDayShiftRapport(createEventRoots(npc, dayTask));
        } else {
            npcRoot.setNightShiftRapport(createEventRoots(npc, nightTask));
        }


        npcRoot = calcTotalGoldAndExp(npcRoot, shift);
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

    private Map<String, Integer> calculateShiftSkillGains(List<SingleEventRoot> singleEventRootList) {
        List<String> skillGains = new ArrayList<>();
        singleEventRootList.forEach(singleEventRoot -> {
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
        return singleEventRoot;
    }

    private NpcRoot calcTotalGoldAndExp(NpcRoot npcRoot, String shift) {
        Integer money = 0;
        Integer exp = 0;
        List<SingleEventRoot> shiftRapport;
        if (" day ".equals(shift)) {
            shiftRapport = npcRoot.getDayShiftRapport();
        } else {
            shiftRapport = npcRoot.getNightShiftRapport();
        }
        for (int i = 0; i < shiftRapport.size(); i++) {
            money += shiftRapport.get(i).getMoneyEarned();
            exp += shiftRapport.get(i).getExpGain();
        }

        if (" day ".equals(shift)) {
            npcRoot.setDayExpGain(exp);
            npcRoot.setDayMoneyEarned(money);
            npcRoot.setMoneyEarned(money);
        } else {
            npcRoot.setNightExpGain(exp);
            npcRoot.setNightMoneyEarned(money);
            npcRoot.setMoneyEarned(npcRoot.getMoneyEarned() + money);
        }

        return npcRoot;
    }

    private SingleEventRoot handleGoldEarned(Npc npc, Task task, SingleEventRoot singleEventRoot) {
        Double taskPerformance;
        Double sumEarned;
        taskPerformance = jobService.calculateTaskPerformance(npc, task, singleEventRoot.getCategory());
        sumEarned = (task.getMoneyCoefficient() * taskPerformance);
        sumEarned += ThreadLocalRandom.current().nextInt(-15, 15);
        singleEventRoot.setMoneyEarned(Math.max(sumEarned.intValue(), 0));
        return singleEventRoot;
    }

    private SingleEventRoot selectCategory(Npc npc, Task task, SingleEventRoot singleEventRoot) {
        List<String> possibleCategories = fileUtility.checkNpcTypes(npc.getPath());
        String selectedSkill = null;
        if (task.getRelevantSkills().size() != 0) {
            selectedSkill = task.getRelevantSkills().get(ThreadLocalRandom.current().nextInt(task.getRelevantSkills().size()));
        }

        String selectedCat;
        if (npc.getSkills().get(selectedSkill) != null) {
            selectedCat = npc.getSkills().get(selectedSkill).getCategory();

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
        Skill trained = npc.getSkills().get(singleEventRoot.getSkill());

        singleEventRoot.setDescription(description);
        if (trained != null) {
            Double progress = 10.0 / trained.getValue();

            progress = Math.min(progress, 1.0);

            trained.changeProgress(progress);

            if (trained.checkSkillLvlUp(category)) {
                singleEventRoot.setDescription(descriptionService.createSingleRootNodeDescription(npc.getName(), singleEventRoot));
                singleEventRoot.setSkillsGain(skillName);
            }
        }
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
