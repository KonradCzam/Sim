package com.example.Sim.Model.Raport;

import com.example.Sim.Model.TirednessSystem.WorkStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NpcRoot extends GirlEndTurnRapport {
    String name;
    String messageLevel;
    Integer dayMoneyEarned;
    Integer nightMoneyEarned;
    Integer dayExpGain;
    Integer nightExpGain;
    Integer moneyEarned;
    String description;
    String statChange;
    String path;
    String category = "profile";
    WorkStatus dayWorkStatus;
    WorkStatus nightWorkStatus;
    List<SingleEventRoot> dayShiftRapport;
    List<SingleEventRoot> nightShiftRapport;
    Integer obedience;
    Integer love;
}
