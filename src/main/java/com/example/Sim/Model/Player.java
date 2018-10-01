package com.example.Sim.Model;

import lombok.Getter;

import java.io.Serializable;
import java.util.*;

@Getter
public class Player implements Serializable {

    String name;
    List<Trait> traits = new ArrayList<>();
    List<Item> inventory = new ArrayList<>();
    Map<String, Item> equippedItems = new HashMap<String, Item>();

    List<Skill> skills = Arrays.asList(new Skill("Animal handling", 5),
            new Skill("Brewing", 5),
            new Skill("Combat", 5),
            new Skill("Cooking", 5),
            new Skill("Crafting", 5),
            new Skill("Farming", 5),
            new Skill("Herbalism", 5),
            new Skill("Magic", 5),
            new Skill("Medicine", 5),
            new Skill("Performance", 5),
            new Skill("Service", 5),

            new Skill("Vaginal sex", 5),
            new Skill("Anal", 5),
            new Skill("BSDM", 5),
            new Skill("Beastiality", 5),
            new Skill("Lesbian", 5),
            new Skill("Strip", 5),
            new Skill("Group", 5),
            new Skill("Oral sex", 5),
            new Skill("Titty sex", 5),
            new Skill("Hand job", 5),
            new Skill("Foot job", 5)
    );
    List<Stat> stats = Arrays.asList(
            new Stat("Age", 22),
            new Stat("Level", 1),
            new Stat("Exp", 0),
            new Stat("Health", 100),
            new Stat("Agility", 0),
            new Stat("Fame", 0),
            new Stat("Tiredness", 0),

            new Stat("Agility", 5),
            new Stat("Charisma", 5),
            new Stat("Confidence", 5),
            new Stat("Constitution", 5),
            new Stat("Inteligence", 5),
            new Stat("Libido", 5),
            new Stat("Refinement", 5),
            new Stat("Spirit", 5),
            new Stat("Strength", 5)
    );

    public Player() {
        name = "Name";
        Item item1 = new Item("C:\\Dev\\Sim\\src\\main\\resources\\UI\\Shirt.png");
        Item item2 = new Item("C:\\Dev\\Sim\\src\\main\\resources\\UI\\Shirt.png");
        inventory.add(item1);
        inventory.add(item2);
    }

}
