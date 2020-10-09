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
import com.example.Sim.Services.NpcService;
import com.example.Sim.controllers.*;
import com.example.Sim.controllers.library.LibraryController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Configuration
@Lazy
@PropertySource("application.properties")
@EnableAspectJAutoProxy
@Getter
public class ScreensConfiguration implements ApplicationContextAware {
    ApplicationContext context;
    public Stage primaryStage;
    @Lazy
    @Autowired
    private List<Pane> createBeans;
    private HashMap<String, Pane> screenMap = new HashMap<>();
    private Scene scene;
    public void setPrimaryStage(Stage primaryStage) {

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("start.fxml"));
        try {
            Pane pane = loader.load();
            primaryStage.setScene(new Scene(pane));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.primaryStage = primaryStage;
    }
    @Bean
    Pane galleryPane() throws IOException {
        Pane pane = loadPane("gallery.fxml",galleryController(this));
        return pane;
    }
    @Bean
    GalleryController galleryController(ScreensConfiguration sc) {
        return new GalleryController(sc);
    }

    @Bean
    Pane hubPane() throws IOException {
        Pane pane = loadPane("hub.fxml",hubController(this));
        return pane;
    }
    @Bean
    HubController hubController(ScreensConfiguration sc) {
        return new HubController(sc);
    }

    @Bean
    Pane npcDetailsPane() throws IOException {
        Pane pane = loadPane("npcDetails.fxml",npcDetailController(this));
        return pane;
    }
    @Bean
    NpcDetailsController npcDetailController(ScreensConfiguration sc) {
        return new NpcDetailsController(sc);
    }
    @Bean
    Pane hirePane() throws IOException {
        Pane pane = loadPane("hire.fxml",hireController(this));
        return pane;
    }
    @Bean
    HireController hireController(ScreensConfiguration sc) {
        return new HireController(sc);
    }

    @Bean
    Pane playerPane() throws IOException {
        Pane pane = loadPane("playerDetail.fxml",playerController(this));
        return pane;
    }
    @Bean
    PlayerDetailsController playerController(ScreensConfiguration sc) {
        return new PlayerDetailsController(sc);
    }

    @Bean
    Pane endTurnPane() throws IOException {
        Pane pane = loadPane("endTurn.fxml",playerController(this));
        return pane;
    }
    @Bean
    EndTurnController endTurnController(ScreensConfiguration sc) {
        return new EndTurnController(sc);
    }

    @Bean
    Pane startPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("startNoController.fxml"));
        loader.setController(startController(this));
        Pane pane = loader.load();
        addScreen("start",pane);
        return pane;
    }
    @Bean
    StartController startController(ScreensConfiguration sc) {
        return new StartController(sc);
    }

    @Lazy
    @Bean
    Pane saveLoadPane() throws IOException {
        Pane pane = loadPane("saveLoad.fxml",saveLoadController(this));
        return pane;
    }
    @Bean
    SaveLoadController saveLoadController(ScreensConfiguration sc) {
        return new SaveLoadController(sc);
    }

    @Bean
    Pane optionsPane() throws IOException {
        Pane pane = loadPane("options.fxml",optionsController(this));
        return pane;
    }
    @Bean
    OptionsController optionsController(ScreensConfiguration sc) {
        return new OptionsController(sc);
    }

    @Bean
    Pane libraryPane() throws IOException {
        Pane pane = loadPane("library.fxml",libraryController(this));
        return pane;
    }
    @Bean
    LibraryController libraryController(ScreensConfiguration sc) {
        return new LibraryController(sc);
    }

    @Bean
    Pane interactionPane() throws IOException {
        Pane pane = loadPane("interaction.fxml",interactionController(this));
        return pane;
    }
    @Bean
    InteractionController interactionController(ScreensConfiguration sc) {
        return new InteractionController(sc);
    }

    @Bean
    Pane achievementsPane() throws IOException {
        Pane pane = loadPane("achievements.fxml",achievementsController(this));
        return pane;
    }
    @Bean
    AchievementsController achievementsController(ScreensConfiguration sc) {
        return new AchievementsController(sc);
    }
    @Bean
    Pane criptGeneraorPane() throws IOException {
        Pane pane = loadPane("criptGeneraor.fxml",scriptGeneraorController(this));
        return pane;
    }
    @Bean
    ScriptGeneratorController scriptGeneraorController(ScreensConfiguration sc) {
        return new ScriptGeneratorController(sc);
    }
    protected void addScreen(String name, Pane pane) {
        screenMap.put(name, pane);
    }

    public Scene getScene() {
        return scene;
    }

    public void activate(String name) {

        primaryStage.getScene().setRoot((Pane)context.getBean(name+"Pane"));
    }
    private Pane loadPane(String path,Object controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(path));
        loader.setController(controller);
        Pane pane = loader.load();
        addScreen(path.substring(0,path.length()-5),pane);
        return pane;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}