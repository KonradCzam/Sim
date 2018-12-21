package com.example.Sim.Utilities;

import com.example.Sim.Exceptions.NpcCreationException;
import com.example.Sim.Model.NPC.Npc;
import com.example.Sim.Model.NPC.Skill;
import com.example.Sim.Model.NPC.Stat;
import com.example.Sim.Model.NPC.Trait;
import com.example.Sim.Services.FactorService;
import com.example.Sim.Services.JobService;
import org.springframework.beans.factory.annotation.Value;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class NpcCreator {

    @Value("${girls.directory:./New folder/}")
    private String directory;
    @Value("#{'${skills.all}'.split(',')}")
    private List<String> skillsList;
    @Value("#{'${stats.status}'.split(',')}")
    private List<String> lightStatsList;
    @Value("#{'${stats.important}'.split(',')}")
    private List<String> heavyStats;
    @Resource
    private transient JobService jobService;
    @Resource
    private transient FactorService factorService;
    List<Trait> allTraits;

    public NpcCreator(TraitLoader traitLoader) {
        this.allTraits = traitLoader.readTraits();
    }

    private Npc createStatsAndSkills(Npc npc) {
        Collections.sort(skillsList);
        Collections.sort(lightStatsList);
        Collections.sort(heavyStats);
        skillsList.forEach(skillName -> npc.addSkill(new Skill(skillName, 0)));
        heavyStats.forEach(statName -> npc.addHeavyStat(new Stat(statName, 0)));
        lightStatsList.forEach(statName -> npc.addLightStat(new Stat(statName, 0)));
        return npc;
    }


    public Npc createNpc(String npcFile) throws NpcCreationException {
        try {
            String path = directory + npcFile;
            Npc npc = new Npc(jobService,factorService);
            npc = createStatsAndSkills(npc);
            try {
                Document doc = openDocument(path);

                NamedNodeMap list = doc.getElementsByTagName("Girl").item(0).getAttributes();
                NodeList traitList = doc.getElementsByTagName("Trait");
                for (int i = 1; i < list.getLength(); i++) {
                    String nodename = list.item(i).getNodeName();
                    String nodeValue = list.getNamedItem(nodename).getNodeValue();
                    Integer nodeValueInt = castValueToInt(nodeValue);
                    if (lightStatsList.contains(nodename) || heavyStats.contains(nodename))
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
                for (int i = 0; i < traitList.getLength(); i++) {
                    String nodename = traitList.item(i).getAttributes().getNamedItem("Name").getNodeValue();
                    List<Trait> npcTraits = getTrait(nodename);
                    npc.addTrait(npcTraits.get(0));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (npc.getStat("Age").getValue() < 18) {
                npc.getStat("Age").setValue(18);
            }
            npc.getStat("Level").setValue(npc.calculateLevel());
            return npc;
        } catch (Exception e) {
            throw new NpcCreationException();
        }
    }

    public Npc createRandomNpc(String npcFile) throws NpcCreationException {
        try {
            Npc npc = new Npc(jobService,factorService);
            String path = directory + npcFile;
            npc = createStatsAndSkills(npc);
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

                    Integer nodeValueMinInt = castValueToInt(nodeValueMin);
                    Integer nodeValueMaxInt = castValueToInt(nodeValueMax);
                    if (lightStatsList.contains(nodename) || heavyStats.contains(nodename))
                        npc.getStat(nodename).setValue(nodeValueMinInt, nodeValueMaxInt);
                }

                for (int i = 1; i < skillList.getLength(); i++) {
                    String nodename = statList.item(i).getAttributes().getNamedItem("Name").getNodeValue();
                    String nodeValueMin = statList.item(i).getAttributes().getNamedItem("Min").getNodeValue();
                    String nodeValueMax = statList.item(i).getAttributes().getNamedItem("Max").getNodeValue();
                    if (nodeValueMin.equals("")) {
                        nodeValueMin = "0";
                    }
                    if (nodeValueMax.equals("")) {
                        nodeValueMax = "100";
                    }

                    Integer nodeValueMinInt = castValueToInt(nodeValueMin);
                    Integer nodeValueMaxInt = castValueToInt(nodeValueMax);
                    if (skillsList.contains(nodename))
                        npc.getSkill(nodename).setValue(nodeValueMinInt, nodeValueMaxInt);


                }
                for (int i = 0; i < traitList.getLength(); i++) {
                    String nodename = traitList.item(i).getAttributes().getNamedItem("Name").getNodeValue();
                    String percent = traitList.item(i).getAttributes().getNamedItem("Percent").getNodeValue();
                    if (percent.equals("")) {
                        percent = "0";
                    }
                    List<Trait> npcTraits = getTrait(nodename);
                    npc.addTrait(npcTraits.get(0), Integer.parseInt(percent));

                }

            } catch (Exception e) {

            }
            if (npc.getStat("Age").getValue() < 18) {
                npc.getStat("Age").setValue(18);
            }
            npc.getStat("Level").setValue(npc.calculateLevel());

            return npc;
        } catch (Exception e) {
            throw new NpcCreationException();
        }
    }

    public List<Trait> getTrait(String nodename) {
        List<Trait> npcTraits = new ArrayList<>();
        allTraits.forEach(alltrait -> {
            if (alltrait.getName().equals(nodename)) {
                npcTraits.add(alltrait);
            }
        });
        return npcTraits;
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
