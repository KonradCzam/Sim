package com.example.Sim.Utilities;

import com.example.Sim.Model.Jobs.Job;
import com.example.Sim.Model.Jobs.Task;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JobLoader {
    String directory = "Tasks/Jobs.xml";

    public List<Job> generateJobs() {
        String path = directory;
        List<Job> jobs = new ArrayList<>();
        try {
            Document doc = openDocument(path);
            NodeList jobsList = doc.getElementsByTagName("Job");
            for (int i = 0; i < jobsList.getLength(); i++) {
                String jobName = jobsList.item(i).getAttributes().item(0).getNodeValue();
                Job job = new Job(jobName);
                job.setTasks(generateTasks(jobsList.item(i)));
                jobs.add(job);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobs;

    }

    public List<Task> generateTasks(Node job) {
        NodeList tasks = job.getChildNodes();
        List<Task> tasksList = new ArrayList<>();
        for (int i = 1; i < tasks.getLength(); i++) {
            if (tasks.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Task task = generateTask(tasks.item(i));
                tasksList.add(task);
            }
        }
        return tasksList;

    }

    public Task generateTask(Node taskNode) {
        String name = taskNode.getAttributes().getNamedItem("name").getNodeValue();
        String description = taskNode.getAttributes().getNamedItem("description").getNodeValue();
        List<String> relevantSkills = getRelevantSkills(taskNode);
        List<String> relevantStats= getRelevantStats(taskNode);
        String type = taskNode.getAttributes().getNamedItem("type").getNodeValue();
        Integer tiring = Integer.parseInt(taskNode.getAttributes().getNamedItem("tiring").getNodeValue());
        Double moneyCoefficient = Double.parseDouble(taskNode.getAttributes().getNamedItem("moneyCoefficient").getNodeValue());
        Integer expGain = Integer.parseInt(taskNode.getAttributes().getNamedItem("expGain").getNodeValue());
        String defaultCat = taskNode.getAttributes().getNamedItem("defaultCat").getNodeValue();
        String thresholdType =taskNode.getAttributes().getNamedItem("thresholdType").getNodeValue();
        Integer threshold = Integer.parseInt(taskNode.getAttributes().getNamedItem("threshold").getNodeValue());
        return new Task(name,description,relevantSkills,relevantStats,type,tiring,moneyCoefficient,expGain,defaultCat,thresholdType,threshold);
    }

    private List<String> getRelevantStats(Node taskNode) {
        List<String> relevantStats = new ArrayList<>();
        NodeList statNodes = taskNode.getFirstChild().getNextSibling().getNextSibling().getNextSibling().getChildNodes();
        for (int i = 0; i < statNodes.getLength(); i++) {
            if (statNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                if (statNodes.item(i).getAttributes() != null) {
                    String stat = statNodes.item(i).getAttributes().item(0).getNodeValue();
                    relevantStats.add(stat);
                }
            }
        }
        return relevantStats;
    }


    private List<String> getRelevantSkills(Node taskNode) {
        List<String> relevantSkills = new ArrayList<>();
        NodeList skillNodes = taskNode.getFirstChild().getNextSibling().getChildNodes();
        for (int i = 0; i < skillNodes.getLength(); i++) {
            if(skillNodes.item(i).getAttributes() != null) {
                String skill = skillNodes.item(i).getAttributes().item(0).getNodeValue();
                relevantSkills.add(skill);
            }
        }
        return relevantSkills;
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
