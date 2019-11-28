// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.Services;

import java.util.function.Consumer;
import java.util.ArrayList;
import com.example.Sim.Model.NPC.Stat;
import com.example.Sim.Model.NPC.Skill;
import com.example.Sim.Model.Item;
import com.example.Sim.Utilities.TraitLoader;
import javax.annotation.Resource;
import com.example.Sim.Model.Player;
import com.example.Sim.Model.NPC.Trait;
import java.util.List;

public class PlayerService
{
    List<Trait> allTraits;
    @Resource
    private Player player;
    
    public PlayerService(final TraitLoader traitLoader) {
        this.allTraits = traitLoader.readTraits();
    }
    
    public void addItemToInvetory(final Item item) {
        this.player.getInventory().add(item);
    }
    
    public void removeItemFromInvetory(final Item item) {
        this.player.getInventory().remove(item);
    }
    
    public void changeSkill(final String skillName, final Integer delta) {
        this.player.getSkills().get(skillName).changeValue(delta);
    }
    
    public void changeStat(final String statName, final Integer delta) {
        this.player.getStats().get(statName).changeValue(delta);
    }
    
    public Stat getStat(final String name) {
        return this.player.getStats().get(name);
    }
    
    public Skill getSkill(final String name) {
        return this.player.getSkills().get(name);
    }
    
    public Integer getPlayerGold() {
        return this.player.getGold();
    }
    
    public Boolean checkIfCanAfford(final Integer price) {
        return price <= this.player.getGold();
    }
    
    public void addTrait(final String traitName) {
        final List<Trait> npcTraits = new ArrayList<Trait>();
        this.allTraits.forEach(PlayerService::lambda$addTrait$0);
        this.player.addTrait(npcTraits.get(0));
    }
    
    public void addTrait(final Trait trait) {
        this.player.addTrait(trait);
    }
    
    public void removeTrait(final String traitName) {
        this.player.getTraits().removeIf(PlayerService::lambda$removeTrait$1);
    }
    
    public Boolean checkTrait(final String traitName) {
        return this.player.getTraits().stream().anyMatch(PlayerService::lambda$checkTrait$2);
    }
    
    public void setGold(final Integer amount) {
        this.player.setGold(Math.max(amount, 0));
    }
    
    public void changeGold(final Integer change) {
        final Integer currentGold = this.player.getGold();
        this.player.setGold(Math.max(currentGold + change, 0));
    }
    
    public List<Trait> getAllTraits() {
        return (List<Trait>)this.allTraits;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public void setAllTraits(final List<Trait> allTraits) {
        this.allTraits = allTraits;
    }
    
    public void setPlayer(final Player player) {
        this.player = player;
    }
}
