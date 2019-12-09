package com.example.Sim.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Achievement {
    String name;
    String tooltip;
    Boolean finished;
    String pathFinished;
    String pathUnFinished;

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }
}
