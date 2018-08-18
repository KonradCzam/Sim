package com.example.Sim.Girls;

import com.example.Sim.Model.Girl;
import com.example.Sim.Model.Skill;
import com.example.Sim.Model.Stat;
import com.example.Sim.Model.Trait;
import org.springframework.beans.factory.annotation.Value;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Arrays;
import java.util.List;


public class GirlCreator {
    private String directory  ;
    public void setDirectory(String directory){
        this.directory = directory;
    }
    List<String> skillsList = Arrays.asList("Combat", "Magic", "Service", "Medicine", "Performance","Crafting","Farming","Herbalism","Brewing","AnimalHandling","Cooking","NormalSex","Anal","BDSM","Beastiality","Lesbian","Strip","Group","OralSex","TittySex","Handjob","Footjob");
    List<String> statsList = Arrays.asList("Level", "Exp", "Age", "Fame", "AskPrice","House","Health","Happiness","Tiredness","PCLove","PCFear","PCHate","Lactation","Charisma","Beauty","Refinement","Agility","Strength","Constitution","Intelligence","Mana","Morality","Dignity","Confidence","Obedience","Spirit","Libido","NPCLove");

    public Girl createGirl(String girlFile) {
        Girl girl = new Girl();
        String path = directory + girlFile;
        Document doc = openDocument(path);

        NamedNodeMap list = doc.getElementsByTagName("Girl").item(0).getAttributes();

        for (int i =1 ;i <list.getLength();i++){
            String nodename = list.item(i).getNodeName();
            String nodeValue = list.getNamedItem(nodename).getNodeValue();
            if(nodeValue.equals("")){
                nodeValue = "0";
            }
            if(statsList.contains(nodename))
                girl.addStat(new Stat(nodename,Integer.parseInt(nodeValue)));
            if(skillsList.contains(nodename))
                girl.addSkill(new Skill(nodename,Integer.parseInt(nodeValue)));
            if(nodename.equals("Gold"))
                girl.setGold(Integer.parseInt(nodeValue));
            if(nodename.equals("Desc"))
                girl.setDesc(nodeValue);
            if(nodename.equals("Name"))
                girl.setPath(nodeValue);
            if(nodename.equals("Human"))
                girl.setHuman(nodeValue);
            if(nodename.equals("Catacombs"))
                girl.setCatacomb(nodeValue);
        }

        return girl;
    }
    public Girl createRandomGirl(String girlFile) {
        Girl girl = new Girl();
        String path = directory + girlFile;
        Document doc = openDocument(path);

        NodeList statList = doc.getElementsByTagName("Stat");
        NodeList traitList = doc.getElementsByTagName("Trait");
        NodeList skillList = doc.getElementsByTagName("Skill");


        NamedNodeMap girlNodeAttributes = doc.getElementsByTagName("Girl").item(0).getAttributes();

        for (int i =1 ;i <girlNodeAttributes.getLength();i++){
            String nodename = girlNodeAttributes.item(i).getNodeName();
            String nodeValue = girlNodeAttributes.getNamedItem(nodename).getNodeValue();
            if(nodename.equals("Desc"))
                girl.setDesc(nodeValue);
            if(nodename.equals("Name"))
                girl.setPath(nodeValue);
            if(nodename.equals("Human"))
                girl.setHuman(nodeValue);
            if(nodename.equals("Catacombs"))
                girl.setCatacomb(nodeValue);
        }
        for (int i =1 ;i <statList.getLength();i++){
            String nodename = statList.item(i).getAttributes().getNamedItem("Name").getNodeValue();
            String nodeValueMin = statList.item(i).getAttributes().getNamedItem("Min").getNodeValue();
            String nodeValueMax = statList.item(i).getAttributes().getNamedItem("Max").getNodeValue();
            if(nodeValueMin.equals("")){
                nodeValueMin = "0";
            }
            if(nodeValueMax.equals("")){
                nodeValueMax = "100";
            }
            girl.addStat(new Stat(nodename,Integer.parseInt(nodeValueMin),Integer.parseInt(nodeValueMax)));

        }

        for (int i =1 ;i <skillList.getLength();i++){
            String nodename = skillList.item(i).getAttributes().getNamedItem("Name").getNodeValue();
            String nodeValueMin = skillList.item(i).getAttributes().getNamedItem("Min").getNodeValue();
            String nodeValueMax = skillList.item(i).getAttributes().getNamedItem("Max").getNodeValue();
            if(nodeValueMin.equals("")){
                nodeValueMin = "0";
            }
            if(nodeValueMax.equals("")){
                nodeValueMax = "100";
            }
            girl.addSkill(new Skill(nodename,Integer.parseInt(nodeValueMin),Integer.parseInt(nodeValueMax)));

        }
        for (int i =1 ;i <traitList.getLength();i++){
            String nodename = traitList.item(i).getAttributes().getNamedItem("Name").getNodeValue();
            String percent = traitList.item(i).getAttributes().getNamedItem("Percent").getNodeValue();
            if(percent.equals("")){
                percent = "0";
            }

            girl.addTrait(new Trait(nodename),Integer.parseInt(percent));

        }
        return girl;
    }
    public Document openDocument(String path){
        try {
            File fXmlFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();
            return doc;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
