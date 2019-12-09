package com.example.Sim.Utilities;

import com.example.Sim.Model.NPC.Trait;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TraitLoader {

    String directory = "Data/Traits/";

    public List<Trait> readTraits() {
        File dir = new File(directory);
        String[] fileArray = dir.list();
        List<Trait> traits = new ArrayList<>();
        for(int i =0; i< fileArray.length;i++){
            traits.addAll(readTraitFile(directory + fileArray[i]));
        }
        return traits ;

    }

    public List<Trait> readTraitFile(String path) {
        List<Trait> traits = new ArrayList<>();
        try {
            Document doc = openDocument(path);
            NodeList jobsList = doc.getElementsByTagName("Trait");
            for (int i = 0; i < jobsList.getLength(); i++) {
                Node traitNode = jobsList.item(i);
                Trait trait = generateTrait(traitNode);
                traits.add(trait);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return traits;
    }

    public Trait generateTrait(Node traitNode){
        String name = traitNode.getAttributes().getNamedItem("Name").getNodeValue();
        String description = traitNode.getAttributes().getNamedItem("Desc").getNodeValue();
        String type = traitNode.getAttributes().getNamedItem("Type").getNodeValue();
        String inheritChance = traitNode.getAttributes().getNamedItem("InheritChance").getNodeValue();
        String randomChance = traitNode.getAttributes().getNamedItem("RandomChance").getNodeValue();
        String targetSkill = traitNode.getAttributes().getNamedItem("TargetSkill").getNodeValue();
        String targetSkillValue = traitNode.getAttributes().getNamedItem("TargetSkillValue").getNodeValue();
        String targetStat = traitNode.getAttributes().getNamedItem("TargetStat").getNodeValue();
        String targetStatValue = traitNode.getAttributes().getNamedItem("TargetStatValue").getNodeValue();
        Map<String,Integer> skillEffects = new TreeMap<>();
        String[] splitSkill = targetSkill.split(",");
        String[] splitSkillValues = targetSkillValue.split(",");
        String[] splitStat = targetStat.split(",");
        String[] splitStatValues = targetStatValue.split(",");
        for(int i =0; i< splitSkill.length;i++){
            skillEffects.put(splitSkill[i],castValueToInt(splitSkillValues[i]));
        }
        Map<String,Integer> statEffects = new TreeMap<>();
        for(int i =0; i< splitStat.length;i++){
            statEffects.put(splitStat[i],castValueToInt(splitStatValues[i]));
        }

        Trait trait = new Trait(name,description,type,castValueToInt(inheritChance),castValueToInt(randomChance),skillEffects,statEffects);
        return trait;
    }

    public Document openDocument(String path) {
        try {
            File fXmlFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private Integer castValueToInt(String value) {
        Integer result;
        try {
            result = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            result = 0;
        }
        return result;
    }
}
