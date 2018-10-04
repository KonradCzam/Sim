package com.example.Sim.Model.Jobs;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public class Task implements Serializable{
    private String name;
    private String description;
    private List<String> relevantSkills;
    private List<String> relevantStats;
    private String type;
    private Integer tiring;
    private Double moneyCoefficient;
    private Integer expGain;
    private String defaultCat;
    private String thresholdType;
    private Integer threshold;
}
