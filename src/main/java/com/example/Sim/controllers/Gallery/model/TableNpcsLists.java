// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.controllers.Gallery.model;

import java.util.List;

public class TableNpcsLists
{
    private List<TableNpc> normalTableNpcs;
    private List<TableNpc> randomTableNpcs;
    
    public List<TableNpc> getNormalTableNpcs() {
        return this.normalTableNpcs;
    }
    
    public List<TableNpc> getRandomTableNpcs() {
        return this.randomTableNpcs;
    }
    
    public TableNpcsLists(final List<TableNpc> normalTableNpcs, final List<TableNpc> randomTableNpcs) {
        this.normalTableNpcs = normalTableNpcs;
        this.randomTableNpcs = randomTableNpcs;
    }
}
