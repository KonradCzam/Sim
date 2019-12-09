package com.example.Sim.Services;

import com.example.Sim.Model.Upgrade;

import java.util.ArrayList;
import java.util.List;

public class UpgradesService {
    private List<Upgrade> upgradesList = new ArrayList<>();

    public UpgradesService() {
        upgradesList.add(new Upgrade("Library","Lets you train girls at your compound. Reduces the Intelligence training price to 0.",50000, 500, false,null));
        upgradesList.add(new Upgrade("Mage section","Adds another section to you library. Reduces the Magic training price to 0.",50000,500,false,"Library"));
        upgradesList.add(new Upgrade("Dungeon","Reduces the Obedience training price to 0.",500,200,false,""));
        upgradesList.add(new Upgrade("Sex toys for dungeon","Reduces the Libido training price to 0.",50000,0,false,""));
        upgradesList.add(new Upgrade("","Reduces the Constitution training price to 0.",50000,0,false,""));
        upgradesList.add(new Upgrade("","Reduces the Strength training price to 0.",50000,0,false,""));
        upgradesList.add(new Upgrade("","Reduces the Charisma training price to 0.",50000,0,false,""));

        upgradesList.add(new Upgrade("Additional Rooms 1","Gives you 5 additional rooms for your girls. Price grows with each consecutive upgrade.",50000,0,false,""));
        upgradesList.add(new Upgrade("","",50000,0,false,""));
        upgradesList.add(new Upgrade("","",50000,0,false,""));

        upgradesList.add(new Upgrade("Gambling house","",50000,0,false,""));

        upgradesList.add(new Upgrade("Brothel","",50000,0,false,""));
        upgradesList.add(new Upgrade("","",50000,0,false,""));
        upgradesList.add(new Upgrade("Luxuious interior","You get 10% more customers in your brothel.",50000,0,false,""));
        upgradesList.add(new Upgrade("Spiked drinks","All your customers will spend 10% more money in the brothel.",50000,0,false,""));


        upgradesList.add(new Upgrade("Smithy","Unlocks the smith job.",50000,0,false,""));
        upgradesList.add(new Upgrade("Workshop","Lets you buy crafting upgrades.",50000,0,false,""));
        upgradesList.add(new Upgrade("Taylor tools","Unlocks the taylor job.",50000,0,false,"Workshop"));
        upgradesList.add(new Upgrade("Jeweler tools","Unlocks the jeweler job.",50000,0,false,""));
        upgradesList.add(new Upgrade("Herb drying rack","Unlocks the herb gatherer job.",50000,0,false,""));
        upgradesList.add(new Upgrade("Magic tools","Unlocks enchanter job.",50000,0,false,""));
        upgradesList.add(new Upgrade("Brewing kit","Unlocks brewer job.",50000,0,false,""));

        upgradesList.add(new Upgrade("Hospital","Reduces the cost of sending girls to hospital to 0. Generates revenue",50000,-1000,false,""));

        upgradesList.add(new Upgrade("Magic den","Reduces the cost of sending girls to hospital to 0. Generates revenue",50000,-1000,false,""));

    }
}
