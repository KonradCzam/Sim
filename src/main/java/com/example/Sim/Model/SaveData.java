// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.Model;

import com.example.Sim.Model.NPC.Npc;
import java.util.List;
import java.io.Serializable;

public class SaveData implements Serializable
{
    public String name;
    public String date;
    public Integer turn;
    private Integer hired;
    private List<Npc> hiredNpcs;
    private List<Npc> hirableNpcs;
    private Player player;
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getDate() {
        return this.date;
    }
    
    public Integer getTurn() {
        return this.turn;
    }
    
    public Integer getHired() {
        return this.hired;
    }
    
    public List<Npc> getHiredNpcs() {
        return (List<Npc>)this.hiredNpcs;
    }
    
    public List<Npc> getHirableNpcs() {
        return (List<Npc>)this.hirableNpcs;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public SaveData(final String name, final String date, final Integer turn, final Integer hired, final List<Npc> hiredNpcs, final List<Npc> hirableNpcs, final Player player) {
        this.name = name;
        this.date = date;
        this.turn = turn;
        this.hired = hired;
        this.hiredNpcs = hiredNpcs;
        this.hirableNpcs = hirableNpcs;
        this.player = player;
    }
}
