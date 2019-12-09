package com.example.Sim.Config;

import com.example.Sim.Scripts.ScriptFunctionCaller;
import com.example.Sim.Scripts.ScriptRunner;
import org.springframework.context.annotation.*;

@Configuration
@Lazy
@PropertySource("application.properties")
@EnableAspectJAutoProxy
public class ScriptConfig
{
    @Bean
    ScriptRunner scriptRunner(){return new ScriptRunner();}
    @Bean
    ScriptFunctionCaller scriptFunctionCaller(){return new ScriptFunctionCaller();}
}
