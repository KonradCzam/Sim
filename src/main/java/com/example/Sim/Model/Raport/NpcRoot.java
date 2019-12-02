package com.example.Sim.Model.Raport;

import com.example.Sim.Model.TirednessSystem.WorkStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

@NoArgsConstructor
public class NpcRoot extends GirlEndTurnRapport {
    String name;
    String messageLevel;
    Integer moneyEarned;
    Integer expGain;
    String description;
    String statChange;
    String path;
    String category = "profile";
    WorkStatus workStatus;
    List<SingleEventRoot> shiftRapport;
    Integer obedience;
    Integer love;
}
