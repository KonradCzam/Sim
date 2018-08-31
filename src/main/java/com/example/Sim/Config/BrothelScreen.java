package com.example.Sim.Config;

import com.example.Sim.screens.Brothel.BrothelController;
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
