package com.example.Sim.Model;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class GirlReader {
    public Girl createGirl (String path) {
        Girl girl = null;
        try {
            File fXmlFile = new File("New folder/Chloe Amour.girlsx");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NamedNodeMap list = doc.getElementsByTagName("Girl").item(0).getAttributes();
            /*for (int i =0 ;i <list.getLength();i++){
                String stats
                girl.addStat("Charisma",new Stat(Integer.parseInt(list.getNamedItem("Charisma").getNodeValue()));
            }
            list.item()*/
            girl.addStat(new Stat("Charisma",Integer.parseInt(list.getNamedItem("Charisma").getNodeValue())));
            girl.addStat(new Stat("Happiness",Integer.parseInt(list.getNamedItem("Happiness").getNodeValue())));
            girl.addStat(new Stat("Libido",Integer.parseInt(list.getNamedItem("Libido").getNodeValue())));
            girl.addStat(new Stat("Constitution",Integer.parseInt(list.getNamedItem("Constitution").getNodeValue())));
            girl.addStat(new Stat("Intelligence",Integer.parseInt(list.getNamedItem("Intelligence").getNodeValue())));
            girl.addStat(new Stat("Confidence",Integer.parseInt(list.getNamedItem("Confidence").getNodeValue())));
            girl.addStat(new Stat("Mana",Integer.parseInt(list.getNamedItem("Mana").getNodeValue())));
            girl.addStat(new Stat("Agility",Integer.parseInt(list.getNamedItem("Agility").getNodeValue())));
            girl.addStat(new Stat("Fame",Integer.parseInt(list.getNamedItem("Fame").getNodeValue())));
            girl.addStat(new Stat("Level",Integer.parseInt(list.getNamedItem("Level").getNodeValue())));
            girl.addStat(new Stat("AskPrice",Integer.parseInt(list.getNamedItem("AskPrice").getNodeValue())));
            girl.addStat(new Stat("House",Integer.parseInt(list.getNamedItem("House").getNodeValue())));
            girl.addStat(new Stat("Exp",Integer.parseInt(list.getNamedItem("Exp").getNodeValue())));
            girl.addStat(new Stat("Age",Integer.parseInt(list.getNamedItem("Age").getNodeValue())));
            girl.addStat(new Stat("Obedience",Integer.parseInt(list.getNamedItem("Obedience").getNodeValue())));
            girl.addStat(new Stat("Spirit",Integer.parseInt(list.getNamedItem("Spirit").getNodeValue())));
            girl.addStat(new Stat("Beauty",Integer.parseInt(list.getNamedItem("Beauty").getNodeValue())));
            girl.addStat(new Stat("Tiredness",Integer.parseInt(list.getNamedItem("Tiredness").getNodeValue())));
            girl.addStat(new Stat("Health",Integer.parseInt(list.getNamedItem("Health").getNodeValue())));
            girl.addStat(new Stat("PCFear",Integer.parseInt(list.getNamedItem("PCFear").getNodeValue())));
            girl.addStat(new Stat("PCLove",Integer.parseInt(list.getNamedItem("PCLove").getNodeValue())));
            girl.addStat(new Stat("PCHate",Integer.parseInt(list.getNamedItem("PCHate").getNodeValue())));
            girl.addStat(new Stat("Morality",Integer.parseInt(list.getNamedItem("Morality").getNodeValue())));
            girl.addStat(new Stat("Refinement",Integer.parseInt(list.getNamedItem("Refinement").getNodeValue())));
            girl.addStat(new Stat("Dignity",Integer.parseInt(list.getNamedItem("Dignity").getNodeValue())));
            girl.addStat(new Stat("Lactation",Integer.parseInt(list.getNamedItem("Lactation").getNodeValue())));

            girl.addSkill(new Skill("Lactation",Integer.parseInt(list.getNamedItem("Lactation").getNodeValue())));
            girl.addSkill(new Skill("Lactation",Integer.parseInt(list.getNamedItem("Lactation").getNodeValue())));
            girl.addSkill(new Skill("Lactation",Integer.parseInt(list.getNamedItem("Lactation").getNodeValue())));
            girl.addSkill(new Skill("Lactation",Integer.parseInt(list.getNamedItem("Lactation").getNodeValue())));
            girl.addSkill(new Skill("Lactation",Integer.parseInt(list.getNamedItem("Lactation").getNodeValue())));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return girl;
    }

}
