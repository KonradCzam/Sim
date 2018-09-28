package com.example.Sim.Model.Jobs;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum Task {
    EVERYTHING("Ev","Ev",
            Arrays.asList( "Performance","NormalSex","Anal","BDSM","Beastiality","Lesbian","Strip","Group","OralSex","TittySex","Handjob","Footjob"),
            Arrays.asList("Level", "Fame", "House","Health","Happiness","Tiredness","PCLove","PCFear","PCHate","Lactation","Charisma","Beauty","Refinement","Agility","Intelligence","Morality","Dignity","Confidence","Obedience","Spirit","Libido"),
            "single",
            2,1.,10,"sex"),

    BROTHEL_STRIP("Pole Dancer", "Strip",
            Arrays.asList( "Performance","NormalSex","Anal","BDSM","Beastiality","Lesbian","Strip","Group","OralSex","TittySex","Handjob","Footjob"),
            Arrays.asList("Level", "Fame", "House","Health","Happiness","Tiredness","PCLove","PCFear","PCHate","Lactation","Charisma","Beauty","Refinement","Agility","Intelligence","Morality","Dignity","Confidence","Obedience","Spirit","Libido"),
            "single",
            2,1.,10,"sex"),

    BROTHEL_STREET_WHORE("Whore on the street", "Street Whore",
            Arrays.asList( "Performance","NormalSex","Anal","BDSM","Beastiality","Lesbian","Strip","Group","OralSex","TittySex","Handjob","Footjob"),
            Arrays.asList("Level", "Fame", "House","Health","Happiness","Tiredness","PCLove","PCFear","PCHate","Lactation","Charisma","Beauty","Refinement","Agility","Intelligence","Morality","Dignity","Confidence","Obedience","Spirit","Libido"),
            "single",
            2,1.,10,"sex"),

    BROTHEL_WHORE("Whore in brothel, requires room", "Whore",
            Arrays.asList( "Performance","NormalSex","Anal","BDSM","Beastiality","Lesbian","Strip","Group","OralSex","TittySex","Handjob","Footjob"),
            Arrays.asList("Level", "Fame", "House","Health","Happiness","Tiredness","PCLove","PCFear","PCHate","Lactation","Charisma","Beauty","Refinement","Agility","Intelligence","Morality","Dignity","Confidence","Obedience","Spirit","Libido"),
            "multiple",
            2,1.,10,"sex"),

    BROTHEL_BARMAID("Barmaid", "Barmaid",
            Arrays.asList( "Performance","NormalSex","Anal","BDSM","Beastiality","Lesbian","Strip","Group","OralSex","TittySex","Handjob","Footjob"),
            Arrays.asList("Level", "Fame", "House","Health","Happiness","Tiredness","PCLove","PCFear","PCHate","Lactation","Charisma","Beauty","Refinement","Agility","Intelligence","Morality","Dignity","Confidence","Obedience","Spirit","Libido"),
            "single",
            2,1.,10,"sex"),

    BROTHEL_WAITRESS("Waitress", "Waitress",
            Arrays.asList( "Performance","NormalSex","Anal","BDSM","Beastiality","Lesbian","Strip","Group","OralSex","TittySex","Handjob","Footjob"),
            Arrays.asList("Level", "Fame", "House","Health","Happiness","Tiredness","PCLove","PCFear","PCHate","Lactation","Charisma","Beauty","Refinement","Agility","Intelligence","Morality","Dignity","Confidence","Obedience","Spirit","Libido"),
            "single",
            2,1.,10,"sex"),
    FREE_TIME("Free time", "Free time",
            Arrays.asList(""),
            Arrays.asList("Level"),
            "single",
            -5,0.0,0,"profile");
    private String descritption;
    private String name;
    private List<String> relevantSkills;
    private List<String> relevantStats;
    private String type;
    private Integer tiring;
    private Double moneyCoefficient;
    private Integer expGain;
    private String defaultCat;

    private Task(String descritption, String name,List<String> relevantSkills, List<String> relevantStats,String type, Integer tiring, Double moneyCoefficient,Integer expGain,String defaultCat){
        this.descritption = descritption;
        this.name = name;
        this.relevantSkills = relevantSkills;
        this.relevantStats = relevantStats;
        this.type = type;
        this.tiring = tiring;
        this.moneyCoefficient = moneyCoefficient;
        this.expGain = expGain;
        this.defaultCat = defaultCat;
    }


}
