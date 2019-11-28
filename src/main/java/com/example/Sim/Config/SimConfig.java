package com.example.Sim.Config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

/**
 * @author Mehmet Sunkur <mehmetsunkur@gmail.com>
 */

@Configuration
@ComponentScan("com.example.Sim")
@Import({ScreensConfiguration.class})
@EnableAspectJAutoProxy
public class SimConfig {


}
