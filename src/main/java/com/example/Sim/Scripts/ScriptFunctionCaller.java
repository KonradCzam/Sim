package com.example.Sim.Scripts;

import com.example.Sim.Exceptions.NpcCreationException;
import com.example.Sim.Model.NPC.Npc;
import com.example.Sim.Scripts.ScriptGenerator.FunctionCall;
import com.example.Sim.Scripts.ScriptGenerator.FunctionCalls.*;
import com.example.Sim.Services.NpcService;
import com.example.Sim.Services.PlayerService;
import com.example.Sim.Utilities.NpcCreator;

import javax.annotation.Resource;


public class ScriptFunctionCaller {
    @Resource
    PlayerService playerService;
    @Resource
    NpcService npcService;
    @Resource
    NpcCreator npcCreator;

    public String call(FunctionCall functionCall) {
        switch (functionCall.getFunction()) {
            case "HireNpc":
                return hireNpc((HireNpcNode) functionCall);
            case "SellNpc":
                return sellNpc((SellNpcNode) functionCall);
            case "CreateNpc":
                return createNpc((CreateNpcNode) functionCall);
            case "KillNpc":
                return killNpc((KillNpcNode) functionCall);
            case "ChangeNpcStat":
                return changeNpcStat((ChangeNpcStatNode) functionCall);
            case "ChangeNpcSkill":
                return changeNpcSkill((ChangeNpcSkillNode) functionCall);
            case "ChangeNpcName":
                return changeNpcName((ChangeNpcNameNode) functionCall);
            case "CheckNpcTrait":
                return checkNpcTrait((CheckNpcTraitNode) functionCall);
            case "AddNpcTrait":
                return addNpcTrait((AddNpcTraitNode) functionCall);
            case "RemoveNpcTrait":
                return removeNpcTrait((RemoveNpcTraitNode) functionCall);
            case "CompareNpcValue":
                return compareNpc(functionCall);
            case "ChangePlayerStat":
                return changePlayerStat((ChangePlayerStatNode) functionCall);

            case "ChangePlayerSkill":
                return changePlayerSkill((ChangePlayerSkillNode) functionCall);
            case "ChangeGold":
                return changeGold((ChangeGoldNode) functionCall);
            case "CheckPlayerTrait":
                return checkPlayerTrait((CheckPlayerTraitNode) functionCall);
            case "AddPlayerTrait":
                return addPlayerTrait((AddPlayerTraitNode) functionCall);
            case "RemovePlayerTrait":
                return removePlayerTrait((RemovePlayerTraitNode) functionCall);
            case "ComparePlayerValue":
                return comparePlayerValue((ComparePlayerValueNode) functionCall);
            case "Skip":
                return skip(functionCall);
            case "End":
                return end(functionCall);
            default:
                return end(functionCall);
        }
    }

    private String hireNpc(HireNpcNode hireNpcNode) {
        String npcName = hireNpcNode.getNpcName();
        Npc npcToHire = npcService.getHirableNpcs().stream().filter(npc -> npc.getName().equals(npcName)).findAny().orElse(null);
        npcService.hireNpcNoHiredChange(npcToHire);
        return hireNpcNode.getNextNode();
    }

    private String sellNpc(SellNpcNode sellNpcNode) {
        String npcName = sellNpcNode.getNpcName();
        npcService.sellNpc(npcName);
        return sellNpcNode.getNextNode();
    }

    private String createNpc(CreateNpcNode createNpcNode) {
        String npcName = createNpcNode.getPath();
        Npc npc = null;
        try {
            npc = npcCreator.createNpc(npcName);
        } catch (NpcCreationException e) {
            e.printStackTrace();
        }
        npcService.getHiredNpcs().add(npc);
        return createNpcNode.getNextNode();
    }

    private String killNpc(KillNpcNode killNpcNode) {
        //TODO
        String npcName = killNpcNode.getNpcName();
        return killNpcNode.getNextNode();
    }

    private String changeNpcStat(ChangeNpcStatNode changeNpcStatNode) {
        String npcName = changeNpcStatNode.getNpcName();
        String statName = changeNpcStatNode.getStatName();

        Integer statChange = Integer.parseInt(changeNpcStatNode.getStatChange());
        Npc npc = findNpc(npcName);
        npc.getStat(statName).changeValue(statChange);
        return changeNpcStatNode.getNextNode();
    }

    private String changeNpcSkill(ChangeNpcSkillNode changeNpcSkillNode) {
        String npcName = changeNpcSkillNode.getNpcName();
        String skillName = changeNpcSkillNode.getSkillName();
        Integer skillChange = Integer.parseInt(changeNpcSkillNode.getSkillChange());
        Npc npc = findNpc(npcName);
        npc.getSkill(skillName).changeValue(skillChange);
        return changeNpcSkillNode.getNextNode();
    }

    private String changeNpcName(ChangeNpcNameNode changeNpcNameNode) {
        String npcName = changeNpcNameNode.getNpcName();
        String npcNewName = changeNpcNameNode.getNewNpcName();

        Npc npc = findNpc(npcName);
        npc.setName(npcNewName);

        return changeNpcNameNode.getNextNode();
    }

    private String checkNpcTrait(CheckNpcTraitNode checkNpcTraitNode) {
        String npcName = checkNpcTraitNode.getNpcName();
        String traitName = checkNpcTraitNode.getTraitName();

        Npc npc = findNpc(npcName);
        if (npc.checkTrait(traitName)) {
            return checkNpcTraitNode.getNextNodePositive();
        } else {
            return checkNpcTraitNode.getNextNodeNegative();
        }
    }

    private String addNpcTrait(AddNpcTraitNode addNpcTraitNode) {
        String npcName = addNpcTraitNode.getNpcName();
        String traitName = addNpcTraitNode.getTraitName();

        Npc npc = findNpc(npcName);
        npc.addTrait(npcCreator.getTrait(traitName).get(0));

        return addNpcTraitNode.getNextNode();
    }

    private String removeNpcTrait(RemoveNpcTraitNode removeNpcTraitNode) {
        String npcName = removeNpcTraitNode.getNpcName();
        String traitName = removeNpcTraitNode.getTraitName();

        Npc npc = findNpc(npcName);
        npc.removeTrait(traitName);

        return removeNpcTraitNode.getNextNode();
    }

    private String compareNpc(FunctionCall functionCall) {
        //TODO
        return null;
    }

    private String changePlayerStat(ChangePlayerStatNode changePlayerStatNode) {
        String stat = changePlayerStatNode.getStat();
        Integer statChange = Integer.parseInt(changePlayerStatNode.getStatChange());
        playerService.changeStat(stat, statChange);
        return changePlayerStatNode.getNextNode();
    }

    private String changePlayerSkill(ChangePlayerSkillNode changePlayerSkillNode) {
        String stat = changePlayerSkillNode.getSkill();
        Integer statChange = Integer.parseInt(changePlayerSkillNode.getSkillChange());
        playerService.changeSkill(stat, statChange);
        return changePlayerSkillNode.getNextNode();
    }

    private String changeGold(ChangeGoldNode changeGoldNode) {
        Integer goldChange = Integer.parseInt(changeGoldNode.getGoldChange());
        playerService.changeGold(goldChange);
        return changeGoldNode.getNextNode();
    }

    private String checkPlayerTrait(CheckPlayerTraitNode checkPlayerTraitNode) {
        String traitName = checkPlayerTraitNode.getTraitName();
        if (playerService.checkTrait(traitName)) {
            return checkPlayerTraitNode.getNextNodePositive();
        } else {
            return checkPlayerTraitNode.getNextNodeNegative();
        }
    }

    private String addPlayerTrait(AddPlayerTraitNode addPlayerTraitNode) {
        String traitName = addPlayerTraitNode.getTraitName();

        playerService.addTrait(traitName);
        return addPlayerTraitNode.getNextNode();
    }

    private String removePlayerTrait(RemovePlayerTraitNode removePlayerTraitNode) {
        String traitName = removePlayerTraitNode.getTraitName();
        playerService.removeTrait(traitName);
        return removePlayerTraitNode.getNextNode();
    }

    private String comparePlayerValue(ComparePlayerValueNode functionCall) {

        //TODO compare

        return functionCall.getNextNode();
    }

    private String skip(FunctionCall functionCall) {
        return functionCall.getNextNode();
    }

    private String end(FunctionCall functionCall) {

        return "End";
    }

    private Npc findNpc(String npcName) {
        Npc npcToHire = npcService.getHirableNpcs().stream().filter(npc -> npc.getName().equals(npcName)).findAny().orElse(null);
        if (npcToHire == null)
            npcToHire = npcService.getHiredNpcs().stream().filter(npc -> npc.getName().equals(npcName)).findAny().orElse(null);
        return npcToHire;
    }

}
