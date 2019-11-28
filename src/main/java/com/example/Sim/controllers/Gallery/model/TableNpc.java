// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.controllers.Gallery.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class TableNpc
{
    String displayName;
    String path;
    String folder;
    
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }
    
    public void setPath(final String path) {
        this.path = path;
    }
    
    public void setFolder(final String folder) {
        this.folder = folder;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public String getPath() {
        return this.path;
    }
    
    public String getFolder() {
        return this.folder;
    }
    
    public TableNpc(final String displayName, final String path, final String folder) {
        this.displayName = displayName;
        this.path = path;
        this.folder = folder;
    }
}
