// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.Utilities;

import com.example.Sim.Services.EndTurnService;
import com.example.Sim.Services.PlayerService;
import com.example.Sim.Services.NpcService;

public class Services
{
    NpcService npcService;
    PlayerService playerService;
    SaveAndLoadUtility saveAndLoadUtility;
    EndTurnService endTurnService;
    
    public Services(final NpcService npcService, final PlayerService playerService, final SaveAndLoadUtility saveAndLoadUtility, final EndTurnService endTurnService) {
        this.npcService = npcService;
        this.playerService = playerService;
        this.saveAndLoadUtility = saveAndLoadUtility;
        this.endTurnService = endTurnService;
    }
    
    public NpcService getNpcService() {
        return this.npcService;
    }
    
    public PlayerService getPlayerService() {
        return this.playerService;
    }
    
    public SaveAndLoadUtility getSaveAndLoadUtility() {
        return this.saveAndLoadUtility;
    }
    
    public EndTurnService getEndTurnService() {
        return this.endTurnService;
    }
}
