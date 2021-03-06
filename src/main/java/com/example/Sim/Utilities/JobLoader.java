package com.example.Sim.Utilities;

import com.example.Sim.Model.Jobs.Job;
import com.example.Sim.Model.Jobs.JobStat;
import com.example.Sim.Model.Jobs.Task;
import com.example.Sim.Services.NpcService;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

public class JobLoader {
    String directory = "Data/Tasks/Jobs.xml";

    public JobLoader( ){
        loadJobs();
    }
    public List<Job> loadJobs() {
        String path = directory;
        List<Job> jobs = new ArrayList<>();
        try {
            Document doc = openDocument(path);
            NodeList jobsList = doc.getElementsByTagName("Job");
            for (int i = 0; i < jobsList.getLength(); i++) {
                String jobName = jobsList.item(i).getAttributes().item(0).getNodeValue();

                Job job = new Job(jobName);
                job.setTasks(generateTasks(jobsList.item(i)));
                job.setJobStats(generateJobStats(jobsList.item(i)));
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
            if (tasks.item(i).getNodeType() == Node.ELEMENT_NODE && tasks.item(i).getNodeName().equals("Task") ) {
                Task task = generateTask(tasks.item(i));
                tasksList.add(task);
            }
        }
        return tasksList;

    }
    public List<JobStat> generateJobStats(Node job) {
        NodeList concerns = job.getChildNodes();
        List<JobStat> jobStatList = new ArrayList<>();
        for (int i = 1; i < concerns.getLength(); i++) {
            if (concerns.item(i).getNodeType() == Node.ELEMENT_NODE && concerns.item(i).getNodeName().equals("Concern") ) {
                JobStat jobStat = generateJobStat(concerns.item(i));
                jobStatList.add(jobStat);
            }
        }
        return jobStatList;

    }
    public Task generateTask(Node taskNode) {
        String name = taskNode.getAttributes().getNamedItem("name").getNodeValue();
        String description = taskNode.getAttributes().getNamedItem("description").getNodeValue();
        List<String> relevantSkills = getRelevantSkills(taskNode);
        List<String> relevantStats = getRelevantStats(taskNode);
        String type = taskNode.getAttributes().getNamedItem("type").getNodeValue();
        Integer tiring = Integer.parseInt(taskNode.getAttributes().getNamedItem("tiring").getNodeValue());
        Double moneyCoefficient = Double.parseDouble(taskNode.getAttributes().getNamedItem("moneyCoefficient").getNodeValue());
        Integer expGain = Integer.parseInt(taskNode.getAttributes().getNamedItem("expGain").getNodeValue());
        String defaultCat = taskNode.getAttributes().getNamedItem("defaultCat").getNodeValue();
        Integer threshold = Integer.parseInt(taskNode.getAttributes().getNamedItem("threshold").getNodeValue());
        Integer value = 0;
        if (taskNode.getAttributes().getNamedItem("value") != null) {
            value = Integer.parseInt(taskNode.getAttributes().getNamedItem("value").getNodeValue());
        }
        return new Task(name, description, relevantSkills, relevantStats, type, tiring, moneyCoefficient, expGain, defaultCat, threshold, value);
    }
    public JobStat generateJobStat(Node taskNode) {
        String name = taskNode.getAttributes().getNamedItem("name").getNodeValue();
        String tasks = taskNode.getAttributes().getNamedItem("tasks").getNodeValue();
        String weights = taskNode.getAttributes().getNamedItem("weights").getNodeValue();
        List<String> tasksList = new LinkedList<String>(Arrays.asList(tasks.split(",")));
        List<String> weightsList = new LinkedList<String>(Arrays.asList(weights.split(",")));
        Map<String,Integer> concernsMap = new HashMap<>();
        String temp ="";
        Integer temp2 = null;
        Integer size = tasksList.size();
        for (int i =0; i< size; i++){
            temp = tasksList.remove(0);
            temp2 = Integer.parseInt(weightsList.remove(0));
            concernsMap.put(temp,temp2);
        }
        return new JobStat(name,concernsMap,0.0,0.0);
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
            if (skillNodes.item(i).getAttributes() != null) {
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
