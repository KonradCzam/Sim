package com.example.Sim.Config;

import com.example.Sim.controllers.library.*;
import org.springframework.context.annotation.*;

@Configuration
@Lazy
@PropertySource("application.properties")
@EnableAspectJAutoProxy
public class LibraryConfig {
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
