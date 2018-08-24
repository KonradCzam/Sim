package com.example.Sim.Config;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBoxBuilder;


public class BrothelScreen extends Pane {
    BrothelController controller;

    BrothelScreen(BrothelController controller){
        this.controller = controller;

        getChildren().add(VBoxBuilder.create()
                .children()

                .build());
    }
}
