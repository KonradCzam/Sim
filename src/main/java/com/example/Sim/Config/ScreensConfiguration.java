package com.example.Sim.Config;
/*
 * Copyright (c) 2012, Stephen Chin
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL STEPHEN CHIN OR ORACLE CORPORATION BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Scripts.ScriptGenerator.ScriptGeneratorController;
import com.example.Sim.controllers.*;
import com.example.Sim.controllers.library.LibraryController;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.context.annotation.*;

@Configuration
@Lazy
@PropertySource("application.properties")
@EnableAspectJAutoProxy
public class ScreensConfiguration {
    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showScreen(Parent screen) {
        primaryStage.setScene(new Scene(screen, 1000, 800));
        primaryStage.show();
    }

    @Bean
    public FXMLDialog loginDialog() {
        return new FXMLDialog(controller(), getClass().getClassLoader().getResource("gallery.fxml"), primaryStage, StageStyle.DECORATED);
    }

    @Bean
    GalleryController controller() {
        return new GalleryController(this);
    }

    @Bean

    public FXMLDialog hubDialog() {
        return new FXMLDialog(hubController(), getClass().getClassLoader().getResource("hub.fxml"), primaryStage, StageStyle.DECORATED);
    }

    @Bean
    HubController hubController() {
        return new HubController(this);
    }

    @Bean
    public FXMLDialog npcDetailsDialog() {
        return new FXMLDialog(npcDetailController(), getClass().getClassLoader().getResource("npcDetails.fxml"), primaryStage, StageStyle.DECORATED);
    }

    @Bean
    NpcDetailsController npcDetailController() {
        return new NpcDetailsController(this);
    }

    @Bean
    public FXMLDialog hireDialog() {
        return new FXMLDialog(hireController(), getClass().getClassLoader().getResource("hire.fxml"), primaryStage, StageStyle.DECORATED);
    }

    @Bean
    HireController hireController() {
        return new HireController(this);
    }

    @Bean
    public FXMLDialog playerDialog() {
        return new FXMLDialog(playerController(), getClass().getClassLoader().getResource("playerDetails.fxml"), primaryStage, StageStyle.DECORATED);
    }

    @Bean
    PlayerDetailsController playerController() {
        return new PlayerDetailsController(this);
    }

    @Bean
    public FXMLDialog endTurnDialog() {
        return new FXMLDialog(endTurnController(), getClass().getClassLoader().getResource("endTurn.fxml"), primaryStage, StageStyle.DECORATED);
    }

    @Bean
    EndTurnController endTurnController() {
        return new EndTurnController(this);
    }

    @Bean
    public FXMLDialog startDialog() {
        return new FXMLDialog(startController(), getClass().getClassLoader().getResource("start.fxml"), primaryStage, StageStyle.DECORATED);
    }

    @Bean
    StartController startController() {
        return new StartController(this);
    }

    @Bean
    public FXMLDialog saveLoadDialog() {
        return new FXMLDialog(saveLoadController(), getClass().getClassLoader().getResource("saveLoad.fxml"), primaryStage, StageStyle.DECORATED);
    }

    @Bean
    SaveLoadController saveLoadController() {
        return new SaveLoadController(this);
    }

    @Bean
    public FXMLDialog optionsDialog() {
        return new FXMLDialog(optionsController(), getClass().getClassLoader().getResource("options.fxml"), primaryStage, StageStyle.DECORATED);
    }
    @Bean
    OptionsController optionsController() {
        return new OptionsController(this);
    }

    @Bean
    public FXMLDialog libraryDialog() {
        return new FXMLDialog(libraryController(), getClass().getClassLoader().getResource("library/library.fxml"), primaryStage, StageStyle.DECORATED);
    }
    @Bean
    LibraryController libraryController() {
        return new LibraryController(this);
    }

    @Bean
    public FXMLDialog interactionDialog() {
        return new FXMLDialog(interactionController(), getClass().getClassLoader().getResource("interaction.fxml"), primaryStage, StageStyle.DECORATED);
    }
    @Bean
    InteractionController interactionController() {
        return new InteractionController(this);
    }

    @Bean
    public FXMLDialog achievementsDialog() {
        return new FXMLDialog(achievementsController(), getClass().getClassLoader().getResource("achievements.fxml"), primaryStage, StageStyle.DECORATED);
    }
    @Bean
    AchievementsController achievementsController() {
        return new AchievementsController(this);
    }

    @Bean
    public FXMLDialog scriptGeneraorDialog() {
        return new FXMLDialog(scriptGeneraorController(), getClass().getClassLoader().getResource("scriptGenerator.fxml"), primaryStage, StageStyle.DECORATED);
    }
    @Bean
    ScriptGeneratorController scriptGeneraorController() {
        return new ScriptGeneratorController(this);
    }






}