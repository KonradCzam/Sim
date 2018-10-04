package com.example.Sim.Config;

import com.example.Sim.Model.Player;
import com.example.Sim.Services.*;
import com.example.Sim.Utilities.FileUtility;
import com.example.Sim.Utilities.ImageHandler;
import com.example.Sim.Utilities.NpcCreator;
import com.example.Sim.Utilities.SaveAndLoadUtility;
import com.example.Sim.controllers.library.TaskController;
import org.springframework.context.annotation.*;

/**
 * @author Mehmet Sunkur <mehmetsunkur@gmail.com>
 */

@Configuration
@ComponentScan("com.example.Sim")
@Import({ScreensConfiguration.class, LibraryConfig.class})
@EnableAspectJAutoProxy
public class SimConfig {
    @Bean
    public ImageHandler imageHandler() {
        return new ImageHandler();
    }

    @Bean
    public FileUtility fileUtility() {
        return new FileUtility();
    }

    @Bean
    public NpcService npcService() {
        return new NpcService();
    }

    @Bean
    public NpcCreator npcCreator() {
        return new NpcCreator();
    }

    @Bean
    public Player player() {
        return new Player();
    }

    @Bean
    public EndTurnService endTurnService() {
        return new EndTurnService();
    }

    @Bean
    public DescriptionService descriptionService() {
        return new DescriptionService();
    }

    @Bean
    public PlayerService playerService() {
        return new PlayerService();
    }

    @Bean
    public SaveAndLoadUtility saveAndLoadUtility() {
        return new SaveAndLoadUtility();
    }
    @Bean
    public TirednessService tirednessService(){return new TirednessService();}
    @Bean
    public JobService jobService(){return new JobService();}

}
