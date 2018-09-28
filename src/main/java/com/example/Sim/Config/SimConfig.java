package com.example.Sim.Config;

import com.example.Sim.Model.Player;
import com.example.Sim.Model.SaveSlot;
import com.example.Sim.Services.DescriptionService;
import com.example.Sim.Services.EndTurnService;
import com.example.Sim.Services.NpcService;
import com.example.Sim.Services.PlayerService;
import com.example.Sim.Utilities.FileUtility;
import com.example.Sim.Utilities.ImageHandler;
import com.example.Sim.Utilities.NpcCreator;
import com.example.Sim.Utilities.SaveAndLoadUtility;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mehmet Sunkur <mehmetsunkur@gmail.com>
 */

@Configuration
@ComponentScan("com.example.Sim")
@Import(ScreensConfiguration.class)
@EnableAspectJAutoProxy
public class SimConfig {



}
