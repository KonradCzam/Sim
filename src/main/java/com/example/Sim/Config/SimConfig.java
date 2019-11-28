// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.Config;

import com.example.Sim.Services.CustomerService;
import com.example.Sim.Utilities.TraitLoader;
import com.example.Sim.Services.AchievementService;
import com.example.Sim.Services.ObedienceService;
import com.example.Sim.Services.JobService;
import com.example.Sim.Utilities.JobLoader;
import com.example.Sim.Services.TirednessService;
import com.example.Sim.Utilities.SaveAndLoadUtility;
import com.example.Sim.Services.PlayerService;
import com.example.Sim.Services.DescriptionService;
import com.example.Sim.Services.EndTurnService;
import com.example.Sim.Model.Player;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;
import com.example.Sim.Utilities.NpcCreator;
import com.example.Sim.Services.NpcService;
import com.example.Sim.Utilities.FileUtility;
import org.springframework.context.annotation.Bean;
import com.example.Sim.Utilities.ImageHandler;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "com.example.Sim" })
@Import({ ScreensConfiguration.class, LibraryConfig.class, ScriptConfig.class, FactorConfig.class })
@EnableAspectJAutoProxy
public class SimConfig
{
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
        return new NpcCreator(this.traitLoader());
    }
    
    @Bean
    public Player player(@Value("#{'${skills.player}'.split(',')}") final List<String> skillsList, @Value("#{'${stats.player}'.split(',')}") final List<String> statsList) {
        return new Player((List)skillsList, (List)statsList);
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
        return new PlayerService(this.traitLoader());
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
        return new JobService(this.jobLoader());
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
