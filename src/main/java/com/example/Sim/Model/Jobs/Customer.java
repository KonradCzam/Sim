package com.example.Sim.Model.Jobs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    String targetJob;
    String tier;
    Integer money;
    Double happiness;
    Integer moneySpent;
}
