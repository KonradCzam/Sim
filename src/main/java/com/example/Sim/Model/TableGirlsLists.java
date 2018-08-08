package com.example.Sim.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TableGirlsLists {
    private List<TableGirl> normalTableGirls;
    private List<TableGirl> randomTableGirls;

}
