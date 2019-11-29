package com.example.Sim.Config;

import com.example.Sim.Model.Player;
import com.example.Sim.Services.*;
import com.example.Sim.Utilities.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import java.util.List;

/**
 * @author Mehmet Sunkur <mehmetsunkur@gmail.com>
 */

@Configuration
@ComponentScan("com.example.Sim")
@Import({ScreensConfiguration.class, LibraryConfig.class, ScriptConfig.class})
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
        return new NpcCreator(traitLoader());
    }

    @Bean
    public Player player(@Value("#{'${skills.player}'.split(',')}")
                                 List<String> skillsList,
                         @Value("#{'${stats.player}'.split(',')}")
                                 List<String> statsList) {
        return new Player(skillsList, statsList);
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
        return new PlayerService(traitLoader());
    }

    @Bean
    public SaveAndLoadUtility saveAndLoadUtility() {
        return new SaveAndLoadUtility();
    }

    @Bean
    public TirednessService tirednessService() {
        return new TirednessService();
    }
    @Bean
    public JobLoader jobLoader() {
        return new JobLoader();
    }
    @Bean
    public JobService jobService() {
        return new JobService(jobLoader());
    }

    @Bean
    public ObedienceService obedienceService() {
        return new ObedienceService();
    }

    @Bean
    public AchievementService achievmentService() {
        return new AchievementService();
    }

    @Bean
    public TraitLoader traitLoader() {
        return new TraitLoader();
    }
    @Bean
    public CustomerService customerService() {
        return new CustomerService();
    }




}
