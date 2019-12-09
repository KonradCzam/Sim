package com.example.Sim.Model.Raport;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FinanceEndTurnRapport {
    String name;
    List<JobRoot> financeRootList;

    public String getPath() {
        return null;
    }

    public String getCategory() {
        return null;
    }

    public String getDescription() {
        return null;
    }
}
