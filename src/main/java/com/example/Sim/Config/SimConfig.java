package com.example.Sim.Config;

import com.example.Sim.Girls.GirlCreator;
import com.example.Sim.Girls.GirlService;
import com.example.Sim.Utilities.FileUtility;
import com.example.Sim.Utilities.ImageHandler;
import org.springframework.context.annotation.*;

/**
 *
 * @author Mehmet Sunkur <mehmetsunkur@gmail.com>
 */

@Configuration
@ComponentScan("com.example.Sim")
@Import(ScreensConfiguration.class)
public class SimConfig {

    @Bean
    public ImageHandler imageHandler(){
        return new ImageHandler();
    }
    @Bean
    public FileUtility girlOpener(){
        return new FileUtility();
    }
    @Bean
    public GirlService girlService(){
        return new GirlService();
    }
    @Bean
    public GirlCreator girlCreator(){
        return new GirlCreator();
    }



}
