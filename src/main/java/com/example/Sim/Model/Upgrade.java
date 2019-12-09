package com.example.Sim.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Upgrade {
    String name;
    String description;
    Integer price;
    Integer upkeep;
    Boolean bought;
    String requires;
}
