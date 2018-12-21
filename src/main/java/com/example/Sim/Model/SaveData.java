package com.example.Sim.Model;

import com.example.Sim.Model.NPC.Npc;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@AllArgsConstructor
public class SaveData implements Serializable {
    public String name;
    public String date;
    public Integer turn;
    private Integer hired;
    private List<Npc> hiredNpcs;
    private List<Npc> hirableNpcs;
    private Player player;

    public void setName(String name) {
        this.name = name;
    }
}
