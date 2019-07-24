package com.example.Sim.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class Item implements Serializable {

    private String name;
    private String description;
    private String path;

    public Item(String path) {
        this.path = path;
    }
}
