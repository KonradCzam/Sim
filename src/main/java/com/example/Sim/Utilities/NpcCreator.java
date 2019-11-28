// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.Utilities;

import com.example.Sim.Model.NPC.Skill;
import com.example.Sim.Model.NPC.Stat;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Document;
import com.example.Sim.Exceptions.NpcCreationException;
import java.util.function.Consumer;
import java.util.Collections;
import com.example.Sim.Model.NPC.Npc;
import com.example.Sim.Model.NPC.Trait;
import com.example.Sim.Services.FactorService;
import javax.annotation.Resource;
import com.example.Sim.Services.JobService;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;

public class NpcCreator
{
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
    
    public NpcCreator(final TraitLoader traitLoader) {
        this.allTraits = traitLoader.readTraits();
    }
    
    private Npc createStatsAndSkills(final Npc npc) {
        Collections.sort((List<Comparable>)this.skillsList);
        Collections.sort((List<Comparable>)this.lightStatsList);
        Collections.sort((List<Comparable>)this.heavyStats);
        this.skillsList.forEach(NpcCreator::lambda$createStatsAndSkills$0);
        this.heavyStats.forEach(NpcCreator::lambda$createStatsAndSkills$1);
        this.lightStatsList.forEach(NpcCreator::lambda$createStatsAndSkills$2);
        return npc;
    }
    
    public Npc createNpc(final String npcFile) throws NpcCreationException {
        try {
            final String path = this.directory + npcFile;
            Npc npc = new Npc(this.jobService, this.factorService);
            npc = this.createStatsAndSkills(npc);
            try {
                final Document doc = this.openDocument(path);
                final NamedNodeMap list = doc.getElementsByTagName("Girl").item(0).getAttributes();
                final NodeList traitList = doc.getElementsByTagName("Trait");
                for (int i = 1; i < list.getLength(); ++i) {
                    final String nodename = list.item(i).getNodeName();
                    final String nodeValue = list.getNamedItem(nodename).getNodeValue();
                    final Integer nodeValueInt = this.castValueToInt(nodeValue);
                    if (this.lightStatsList.contains(nodename) || this.heavyStats.contains(nodename)) {
                        npc.getStat(nodename).setValue(nodeValueInt);
                    }
                    else if (this.skillsList.contains(nodename)) {
                        npc.getSkill(nodename).setValue(nodeValueInt);
                    }
                    else if (nodename.equals("Gold")) {
                        npc.setGold(Integer.parseInt(nodeValue));
                    }
                    else if (nodename.equals("Desc")) {
                        npc.setDesc(nodeValue);
                    }
                    else if (nodename.equals("Name")) {
                        npc.setPath(nodeValue);
                    }
                    else if (nodename.equals("Human")) {
                        npc.setHuman(nodeValue);
                    }
                    else if (nodename.equals("Catacombs")) {
                        npc.setCatacomb(nodeValue);
                    }
                }
                for (int i = 0; i < traitList.getLength(); ++i) {
                    final String nodename = traitList.item(i).getAttributes().getNamedItem("Name").getNodeValue();
                    final List<Trait> npcTraits = (List<Trait>)this.getTrait(nodename);
                    npc.addTrait(npcTraits.get(0));
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            if (npc.getStat("Age").getValue() < 18) {
                npc.getStat("Age").setValue(Integer.valueOf(18));
            }
            npc.getStat("Level").setValue(npc.calculateLevel());
            return npc;
        }
        catch (Exception e2) {
            throw new NpcCreationException();
        }
    }
    
    public Npc createRandomNpc(final String npcFile) throws NpcCreationException {
        try {
            Npc npc = new Npc(this.jobService, this.factorService);
            final String path = this.directory + npcFile;
            npc = this.createStatsAndSkills(npc);
            try {
                final Document doc = this.openDocument(path);
                final NodeList statList = doc.getElementsByTagName("Stat");
                final NodeList traitList = doc.getElementsByTagName("Trait");
                final NodeList skillList = doc.getElementsByTagName("Skill");
                final NamedNodeMap npcNodeAttributes = doc.getElementsByTagName("Girl").item(0).getAttributes();
                for (int i = 1; i < npcNodeAttributes.getLength(); ++i) {
                    final String nodename = npcNodeAttributes.item(i).getNodeName();
                    final String nodeValue = npcNodeAttributes.getNamedItem(nodename).getNodeValue();
                    if (nodename.equals("Desc")) {
                        npc.setDesc(nodeValue);
                    }
                    if (nodename.equals("Name")) {
                        npc.setPath(nodeValue);
                    }
                    if (nodename.equals("Human")) {
                        npc.setHuman(nodeValue);
                    }
                    if (nodename.equals("Catacombs")) {
                        npc.setCatacomb(nodeValue);
                    }
                }
                for (int i = 1; i < statList.getLength(); ++i) {
                    final String nodename = statList.item(i).getAttributes().getNamedItem("Name").getNodeValue();
                    String nodeValueMin = statList.item(i).getAttributes().getNamedItem("Min").getNodeValue();
                    String nodeValueMax = statList.item(i).getAttributes().getNamedItem("Max").getNodeValue();
                    if (nodeValueMin.equals("")) {
                        nodeValueMin = "0";
                    }
                    if (nodeValueMax.equals("")) {
                        nodeValueMax = "100";
                    }
                    final Integer nodeValueMinInt = this.castValueToInt(nodeValueMin);
                    final Integer nodeValueMaxInt = this.castValueToInt(nodeValueMax);
                    if (this.lightStatsList.contains(nodename) || this.heavyStats.contains(nodename)) {
                        npc.getStat(nodename).setValue(nodeValueMinInt, nodeValueMaxInt);
                    }
                }
                for (int i = 1; i < skillList.getLength(); ++i) {
                    final String nodename = statList.item(i).getAttributes().getNamedItem("Name").getNodeValue();
                    String nodeValueMin = statList.item(i).getAttributes().getNamedItem("Min").getNodeValue();
                    String nodeValueMax = statList.item(i).getAttributes().getNamedItem("Max").getNodeValue();
                    if (nodeValueMin.equals("")) {
                        nodeValueMin = "0";
                    }
                    if (nodeValueMax.equals("")) {
                        nodeValueMax = "100";
                    }
                    final Integer nodeValueMinInt = this.castValueToInt(nodeValueMin);
                    final Integer nodeValueMaxInt = this.castValueToInt(nodeValueMax);
                    if (this.skillsList.contains(nodename)) {
                        npc.getSkill(nodename).setValue(nodeValueMinInt, nodeValueMaxInt);
                    }
                }
                for (int i = 0; i < traitList.getLength(); ++i) {
                    final String nodename = traitList.item(i).getAttributes().getNamedItem("Name").getNodeValue();
                    String percent = traitList.item(i).getAttributes().getNamedItem("Percent").getNodeValue();
                    if (percent.equals("")) {
                        percent = "0";
                    }
                    final List<Trait> npcTraits = (List<Trait>)this.getTrait(nodename);
                    npc.addTrait(npcTraits.get(0), Integer.parseInt(percent));
                }
            }
            catch (Exception ex) {}
            if (npc.getStat("Age").getValue() < 18) {
                npc.getStat("Age").setValue(Integer.valueOf(18));
            }
            npc.getStat("Level").setValue(npc.calculateLevel());
            return npc;
        }
        catch (Exception e) {
            throw new NpcCreationException();
        }
    }
    
    public List<Trait> getTrait(final String nodename) {
        final List<Trait> npcTraits = new ArrayList<Trait>();
        this.allTraits.forEach(NpcCreator::lambda$getTrait$3);
        return npcTraits;
    }
    
    private Integer castValueToInt(final String value) {
        Integer result;
        try {
            result = Integer.parseInt(value);
        }
        catch (NumberFormatException e) {
            result = 0;
        }
        return result;
    }
    
    public Document openDocument(final String path) {
        try {
            final File fXmlFile = new File(path);
            final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            final Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            return doc;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
