package com.example.Sim.Services;

import com.example.Sim.Model.Item;
import com.example.Sim.Model.NPC.Skill;
import com.example.Sim.Model.NPC.Stat;
import com.example.Sim.Model.NPC.Trait;
import com.example.Sim.Model.Player;
import com.example.Sim.Utilities.TraitLoader;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PlayerService {
    List<Trait> allTraits;
    public PlayerService(TraitLoader traitLoader){
        this.allTraits = traitLoader.readTraits();
    }
    @Resource
    private Player player;


    public void changeSkill(String skillName, Integer delta){
        player.getSkills().get(skillName).changeValue(delta);
    }
    public void changeStat(String statName, Integer delta){
        player.getStats().get(statName).changeValue(delta);
    }
    public Stat getStat(String name){return player.getStats().get(name);}
    public Skill getSkill(String name){return player.getSkills().get(name);}
    public Integer getPlayerGold(){
        return player.getGold();
    }
    public Boolean checkIfCanAfford(Integer price){
        return price > player.getGold() ? false : true;
    }

    public void addTrait(String traitName){
            List<Trait> npcTraits = new ArrayList<>();
            allTraits.forEach(alltrait -> {
                if(alltrait.getName().equals(traitName)){
                    npcTraits.add(alltrait);
                }
            });
            player.addTrait(npcTraits.get(0));
    }
    public void removeTrait(String traitName){
        player.getTraits().removeIf(trait -> traitName.equals(trait.getName()));
    }
    public Boolean checkTrait(String traitName){
        return player.getTraits().stream().anyMatch(trait -> trait.getName().equals(traitName));
    }
    public void setGold(Integer amount){
        player.setGold(Math.max(amount,0));
    }
    public void changeGold(Integer change){
        Integer currentGold = player.getGold();
        player.setGold(Math.max(currentGold + change,0));
    }
}
