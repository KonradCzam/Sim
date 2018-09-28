package com.example.Sim.controllers.Gallery.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TableNpcsLists {
    private List<TableNpc> normalTableNpcs;
    private List<TableNpc> randomTableNpcs;

}
