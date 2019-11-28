// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.Config;

import com.example.Sim.controllers.AchievementsController;
import com.example.Sim.controllers.InteractionController;
import com.example.Sim.controllers.library.LibraryController;
import com.example.Sim.controllers.OptionsController;
import com.example.Sim.controllers.SaveLoadController;
import com.example.Sim.controllers.StartController;
import com.example.Sim.controllers.EndTurnController;
import com.example.Sim.controllers.PlayerDetailsController;
import com.example.Sim.controllers.HireController;
import com.example.Sim.controllers.NpcDetailsController;
import com.example.Sim.controllers.HubController;
import com.example.Sim.controllers.GalleryController;
import org.springframework.context.annotation.Bean;
import javafx.stage.Window;
import com.example.Sim.FXML.DialogController;
import javafx.stage.StageStyle;
import com.example.Sim.FXML.FXMLDialog;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Configuration;

@Configuration
@Lazy
@PropertySource({ "application.properties" })
@EnableAspectJAutoProxy
public class ScreensConfiguration
{
    private Stage primaryStage;
    
    public void setPrimaryStage(final Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    
    public void showScreen(final Parent screen) {
        this.primaryStage.setScene(new Scene(screen, 1000.0, 800.0));
        this.primaryStage.show();
    }
    
    @Bean
    public FXMLDialog loginDialog() {
        return new FXMLDialog((DialogController)this.controller(), this.getClass().getClassLoader().getResource("gallery.fxml"), (Window)this.primaryStage, StageStyle.DECORATED);
    }
    
    @Bean
    GalleryController controller() {
        return new GalleryController(this);
    }
    
    @Bean
    public FXMLDialog hubDialog() {
        return new FXMLDialog((DialogController)this.hubController(), this.getClass().getClassLoader().getResource("hub.fxml"), (Window)this.primaryStage, StageStyle.DECORATED);
    }
    
    @Bean
    HubController hubController() {
        return new HubController(this);
    }
    
    @Bean
    public FXMLDialog npcDetailsDialog() {
        return new FXMLDialog((DialogController)this.npcDetailController(), this.getClass().getClassLoader().getResource("npcDetails.fxml"), (Window)this.primaryStage, StageStyle.DECORATED);
    }
    
    @Bean
    NpcDetailsController npcDetailController() {
        return new NpcDetailsController(this);
    }
    
    @Bean
    public FXMLDialog hireDialog() {
        return new FXMLDialog((DialogController)this.hireController(), this.getClass().getClassLoader().getResource("hire.fxml"), (Window)this.primaryStage, StageStyle.DECORATED);
    }
    
    @Bean
    HireController hireController() {
        return new HireController(this);
    }
    
    @Bean
    public FXMLDialog playerDialog() {
        return new FXMLDialog((DialogController)this.playerController(), this.getClass().getClassLoader().getResource("playerDetails.fxml"), (Window)this.primaryStage, StageStyle.DECORATED);
    }
    
    @Bean
    PlayerDetailsController playerController() {
        return new PlayerDetailsController(this);
    }
    
    @Bean
    public FXMLDialog endTurnDialog() {
        return new FXMLDialog((DialogController)this.endTurnController(), this.getClass().getClassLoader().getResource("endTurn.fxml"), (Window)this.primaryStage, StageStyle.DECORATED);
    }
    
    @Bean
    EndTurnController endTurnController() {
        return new EndTurnController(this);
    }
    
    @Bean
    public FXMLDialog startDialog() {
        return new FXMLDialog((DialogController)this.startController(), this.getClass().getClassLoader().getResource("start.fxml"), (Window)this.primaryStage, StageStyle.DECORATED);
    }
    
    @Bean
    StartController startController() {
        return new StartController(this);
    }
    
    @Bean
    public FXMLDialog saveLoadDialog() {
        return new FXMLDialog((DialogController)this.saveLoadController(), this.getClass().getClassLoader().getResource("saveLoad.fxml"), (Window)this.primaryStage, StageStyle.DECORATED);
    }
    
    @Bean
    SaveLoadController saveLoadController() {
        return new SaveLoadController(this);
    }
    
    @Bean
    public FXMLDialog optionsDialog() {
        return new FXMLDialog((DialogController)this.optionsController(), this.getClass().getClassLoader().getResource("options.fxml"), (Window)this.primaryStage, StageStyle.DECORATED);
    }
    
    @Bean
    OptionsController optionsController() {
        return new OptionsController(this);
    }
    
    @Bean
    public FXMLDialog libraryDialog() {
        return new FXMLDialog((DialogController)this.libraryController(), this.getClass().getClassLoader().getResource("library/library.fxml"), (Window)this.primaryStage, StageStyle.DECORATED);
    }
    
    @Bean
    LibraryController libraryController() {
        return new LibraryController(this);
    }
    
    @Bean
    public FXMLDialog interactionDialog() {
        return new FXMLDialog((DialogController)this.interactionController(), this.getClass().getClassLoader().getResource("interaction.fxml"), (Window)this.primaryStage, StageStyle.DECORATED);
    }
    
    @Bean
    InteractionController interactionController() {
        return new InteractionController(this);
    }
    
    @Bean
    public FXMLDialog achievementsDialog() {
        return new FXMLDialog((DialogController)this.achievementsController(), this.getClass().getClassLoader().getResource("achievements.fxml"), (Window)this.primaryStage, StageStyle.DECORATED);
    }
    
    @Bean
    AchievementsController achievementsController() {
        return new AchievementsController(this);
    }
}
