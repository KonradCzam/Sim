package com.example.Sim.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.Duration;

@AllArgsConstructor
@Getter
@Setter
public class RowData {
    Boolean isFree;
    Boolean isDyzur;
    Boolean isAfterDyzur;
    String lp;
    String kodNieob;
    String godzPracy;
    Duration iloscGodzNieob;
    Duration iloscGodzPracy;
    String gotowosc;
    String wezwanie;
    String dod50;
    String dod100;
    String sumNadgodz;
    String dod20;

}
