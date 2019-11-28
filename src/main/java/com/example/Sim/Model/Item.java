// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.Model;

import java.io.Serializable;

public class Item implements Serializable
{
    private String name;
    private String description;
    private String path;
    
    public Item(final String path) {
        this.path = path;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getPath() {
        return this.path;
    }
}
