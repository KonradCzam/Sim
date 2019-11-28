// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.Config;

import com.example.Sim.controllers.library.TirednessController;
import com.example.Sim.controllers.library.ObedienceAndMoralityController;
import com.example.Sim.controllers.library.KnownIssuesController;
import com.example.Sim.controllers.library.SkillsAndStatsController;
import com.example.Sim.controllers.library.FaqController;
import com.example.Sim.controllers.library.ChangelogController;
import com.example.Sim.controllers.library.MoneyController;
import org.springframework.context.annotation.Bean;
import com.example.Sim.controllers.library.TaskController;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Configuration;

@Configuration
@Lazy
@PropertySource({ "application.properties" })
@EnableAspectJAutoProxy
public class LibraryConfig
{
    @Bean
    TaskController taskController() {
        return new TaskController();
    }
    
    @Bean
    MoneyController moneyController() {
        return new MoneyController();
    }
    
    @Bean
    ChangelogController changelogController() {
        return new ChangelogController();
    }
    
    @Bean
    FaqController faqController() {
        return new FaqController();
    }
    
    @Bean
    SkillsAndStatsController skillsAndStatsController() {
        return new SkillsAndStatsController();
    }
    
    @Bean
    KnownIssuesController knownIssuesController() {
        return new KnownIssuesController();
    }
    
    @Bean
    ObedienceAndMoralityController obedienceAndMoralityController() {
        return new ObedienceAndMoralityController();
    }
    
    @Bean
    TirednessController tirednessController() {
        return new TirednessController();
    }
}
