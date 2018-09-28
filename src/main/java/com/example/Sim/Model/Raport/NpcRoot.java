package com.example.Sim.Model.Raport;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NpcRoot extends  EndTurnRapport{
    String name;
    Integer dayMoneyEarned;
    Integer nightMoneyEarned;
    Integer dayExpGain;
    Integer nightExpGain;
    Integer moneyEarned;
    String description;
    String statChange;
    String path;
    String category = "profile";
    List<SingleEventRoot> dayShiftRapport;
    List<SingleEventRoot> nightShiftRapport;
}
