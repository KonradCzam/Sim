// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.Exceptions;

public class ImageNotFound extends Exception
{
    private String textMessage;
    
    public String getTextMessage() {
        return this.textMessage;
    }
    
    public ImageNotFound(final String textMessage) {
        this.textMessage = textMessage;
    }
}
