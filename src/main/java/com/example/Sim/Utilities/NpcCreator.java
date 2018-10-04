package com.example.Sim.Utilities;

import com.example.Sim.Model.Npc;
import com.example.Sim.Model.Skill;
import com.example.Sim.Model.Stat;
import com.example.Sim.Model.Trait;
import com.example.Sim.Services.JobService;
import org.springframework.beans.factory.annotation.Value;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Arrays;
import java.util.List;


public class NpcCreator {

     @Value("${girls.directory:./New folder/}")
    private String directory;
    @Value("#{'${skills.all}'.split(',')}")
     private List<String> skillsList;
    @Value("#{'${stats.all}'.split(',')}")
    private List<String> statsList;
    @Resource
    private transient JobService jobService;

    private Npc createStatsAndSkills(Npc npc){
        skillsList.forEach(skillName -> npc.addSkill(new Skill(skillName, 0)));
        statsList.forEach(statName -> npc.addStat(new Stat(statName, 0)));
        return npc;
    }
    private Integer castValueToInt(String value){
        Integer result;
        try{
           result  = Integer.parseInt(value);
        }catch (NumberFormatException e){
            result = 0;
        }
        return result;
    }
    public Npc createNpc(String npcFile) {
        String path = directory + npcFile;
        Npc npc = new Npc(jobService);
        npc = createStatsAndSkills(npc);
        try {


            Document doc = openDocument(path);

            NamedNodeMap list = doc.getElementsByTagName("Girl").item(0).getAttributes();

            for (int i = 1; i < list.getLength(); i++) {
                String nodename = list.item(i).getNodeName();
                String nodeValue = list.getNamedItem(nodename).getNodeValue();
                Integer nodeValueInt = castValueToInt(nodeValue);
                if (statsList.contains(nodename))
                    npc.getStat(nodename).setValue(nodeValueInt);
                else if (skillsList.contains(nodename))
                    npc.getSkill(nodename).setValue(nodeValueInt);
                else if (nodename.equals("Gold"))
                    npc.setGold(Integer.parseInt(nodeValue));
                else if (nodename.equals("Desc"))
                    npc.setDesc(nodeValue);
                else if (nodename.equals("Name"))
                    npc.setPath(nodeValue);
                else if (nodename.equals("Human"))
                    npc.setHuman(nodeValue);
                else if (nodename.equals("Catacombs"))
                    npc.setCatacomb(nodeValue);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return npc;
    }

    public Npc createRandomNpc(String npcFile) {
        Npc npc = new Npc(jobService);
        String path = directory + npcFile;
        try {
            Document doc = openDocument(path);

            NodeList statList = doc.getElementsByTagName("Stat");
            NodeList traitList = doc.getElementsByTagName("Trait");
            NodeList skillList = doc.getElementsByTagName("Skill");


            NamedNodeMap npcNodeAttributes = doc.getElementsByTagName("Girl").item(0).getAttributes();

            for (int i = 1; i < npcNodeAttributes.getLength(); i++) {
                String nodename = npcNodeAttributes.item(i).getNodeName();
                String nodeValue = npcNodeAttributes.getNamedItem(nodename).getNodeValue();
                if (nodename.equals("Desc"))
                    npc.setDesc(nodeValue);
                if (nodename.equals("Name"))
                    npc.setPath(nodeValue);
                if (nodename.equals("Human"))
                    npc.setHuman(nodeValue);
                if (nodename.equals("Catacombs"))
                    npc.setCatacomb(nodeValue);
            }
            for (int i = 1; i < statList.getLength(); i++) {
                String nodename = statList.item(i).getAttributes().getNamedItem("Name").getNodeValue();
                String nodeValueMin = statList.item(i).getAttributes().getNamedItem("Min").getNodeValue();
                String nodeValueMax = statList.item(i).getAttributes().getNamedItem("Max").getNodeValue();
                if (nodeValueMin.equals("")) {
                    nodeValueMin = "0";
                }
                if (nodeValueMax.equals("")) {
                    nodeValueMax = "100";
                }
                npc.addStat(new Stat(nodename, Integer.parseInt(nodeValueMin), Integer.parseInt(nodeValueMax)));

            }

            for (int i = 1; i < skillList.getLength(); i++) {
                String nodename = skillList.item(i).getAttributes().getNamedItem("Name").getNodeValue();
                String nodeValueMin = skillList.item(i).getAttributes().getNamedItem("Min").getNodeValue();
                String nodeValueMax = skillList.item(i).getAttributes().getNamedItem("Max").getNodeValue();

                if (nodeValueMin.equals("")) {
                    nodeValueMin = "0";
                }
                if (nodeValueMax.equals("")) {
                    nodeValueMax = "100";
                }
                Integer nodeValueMaxInt = Integer.parseInt(nodeValueMax);
                Integer nodeValueMinInt = Integer.parseInt(nodeValueMin);

                npc.addSkill(new Skill(nodename, nodeValueMinInt, nodeValueMaxInt));

            }
            for (int i = 1; i < traitList.getLength(); i++) {
                String nodename = traitList.item(i).getAttributes().getNamedItem("Name").getNodeValue();
                String percent = traitList.item(i).getAttributes().getNamedItem("Percent").getNodeValue();
                if (percent.equals("")) {
                    percent = "0";
                }

                npc.addTrait(new Trait(nodename), Integer.parseInt(percent));

            }

        } catch (Exception e) {

        }
        return npc;
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
}
