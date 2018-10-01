package com.example.Sim.Utilities;

import com.example.Sim.Services.EndTurnService;
import com.example.Sim.Services.NpcService;
import com.example.Sim.Services.PlayerService;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Services {
    NpcService npcService;
    PlayerService playerService;
    SaveAndLoadUtility saveAndLoadUtility;
    EndTurnService endTurnService;

}
