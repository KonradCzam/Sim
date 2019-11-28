// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.Utilities;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Arrays;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import com.example.Sim.Model.Jobs.JobStat;
import com.example.Sim.Model.Jobs.Task;
import java.util.ArrayList;
import com.example.Sim.Model.Jobs.Job;
import java.util.List;

public class JobLoader
{
    String directory;
    
    public JobLoader() {
        this.directory = "Data/Tasks/Jobs.xml";
        this.loadJobs();
    }
    
    public List<Job> loadJobs() {
        final String path = this.directory;
        final List<Job> jobs = new ArrayList<Job>();
        try {
            final Document doc = this.openDocument(path);
            final NodeList jobsList = doc.getElementsByTagName("Job");
            for (int i = 0; i < jobsList.getLength(); ++i) {
                final String jobName = jobsList.item(i).getAttributes().item(0).getNodeValue();
                final Job job = new Job(jobName);
                job.setTasks(this.generateTasks(jobsList.item(i)));
                job.setJobStats(this.generateJobStats(jobsList.item(i)));
                jobs.add(job);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return jobs;
    }
    
    public List<Task> generateTasks(final Node job) {
        final NodeList tasks = job.getChildNodes();
        final List<Task> tasksList = new ArrayList<Task>();
        for (int i = 1; i < tasks.getLength(); ++i) {
            if (tasks.item(i).getNodeType() == 1 && tasks.item(i).getNodeName().equals("Task")) {
                final Task task = this.generateTask(tasks.item(i));
                tasksList.add(task);
            }
        }
        return tasksList;
    }
    
    public List<JobStat> generateJobStats(final Node job) {
        final NodeList concerns = job.getChildNodes();
        final List<JobStat> jobStatList = new ArrayList<JobStat>();
        for (int i = 1; i < concerns.getLength(); ++i) {
            if (concerns.item(i).getNodeType() == 1 && concerns.item(i).getNodeName().equals("Concern")) {
                final JobStat jobStat = this.generateJobStat(concerns.item(i));
                jobStatList.add(jobStat);
            }
        }
        return jobStatList;
    }
    
    public Task generateTask(final Node taskNode) {
        final String name = taskNode.getAttributes().getNamedItem("name").getNodeValue();
        final String description = taskNode.getAttributes().getNamedItem("description").getNodeValue();
        final List<String> relevantSkills = (List<String>)this.getRelevantSkills(taskNode);
        final List<String> relevantStats = (List<String>)this.getRelevantStats(taskNode);
        final String type = taskNode.getAttributes().getNamedItem("type").getNodeValue();
        final Integer tiring = Integer.parseInt(taskNode.getAttributes().getNamedItem("tiring").getNodeValue());
        final Double moneyCoefficient = Double.parseDouble(taskNode.getAttributes().getNamedItem("moneyCoefficient").getNodeValue());
        final Integer expGain = Integer.parseInt(taskNode.getAttributes().getNamedItem("expGain").getNodeValue());
        final String defaultCat = taskNode.getAttributes().getNamedItem("defaultCat").getNodeValue();
        final Integer threshold = Integer.parseInt(taskNode.getAttributes().getNamedItem("threshold").getNodeValue());
        Integer value = 0;
        if (taskNode.getAttributes().getNamedItem("value") != null) {
            value = Integer.parseInt(taskNode.getAttributes().getNamedItem("value").getNodeValue());
        }
        return new Task(name, description, (List)relevantSkills, (List)relevantStats, type, tiring, moneyCoefficient, expGain, defaultCat, threshold, value);
    }
    
    public JobStat generateJobStat(final Node taskNode) {
        final String name = taskNode.getAttributes().getNamedItem("name").getNodeValue();
        final String tasks = taskNode.getAttributes().getNamedItem("tasks").getNodeValue();
        final String weights = taskNode.getAttributes().getNamedItem("weights").getNodeValue();
        final List<String> tasksList = new LinkedList<String>(Arrays.asList(tasks.split(",")));
        final List<String> weightsList = new LinkedList<String>(Arrays.asList(weights.split(",")));
        final Map<String, Integer> concernsMap = new HashMap<String, Integer>();
        String temp = "";
        Integer temp2 = null;
        final Integer size = tasksList.size();
        for (int i = 0; i < size; ++i) {
            temp = tasksList.remove(0);
            temp2 = Integer.parseInt(weightsList.remove(0));
            concernsMap.put(temp, temp2);
        }
        return new JobStat(name, (Map)concernsMap, Double.valueOf(0.0), Double.valueOf(0.0));
    }
    
    private List<String> getRelevantStats(final Node taskNode) {
        final List<String> relevantStats = new ArrayList<String>();
        final NodeList statNodes = taskNode.getFirstChild().getNextSibling().getNextSibling().getNextSibling().getChildNodes();
        for (int i = 0; i < statNodes.getLength(); ++i) {
            if (statNodes.item(i).getNodeType() == 1 && statNodes.item(i).getAttributes() != null) {
                final String stat = statNodes.item(i).getAttributes().item(0).getNodeValue();
                relevantStats.add(stat);
            }
        }
        return relevantStats;
    }
    
    private List<String> getRelevantSkills(final Node taskNode) {
        final List<String> relevantSkills = new ArrayList<String>();
        final NodeList skillNodes = taskNode.getFirstChild().getNextSibling().getChildNodes();
        for (int i = 0; i < skillNodes.getLength(); ++i) {
            if (skillNodes.item(i).getAttributes() != null) {
                final String skill = skillNodes.item(i).getAttributes().item(0).getNodeValue();
                relevantSkills.add(skill);
            }
        }
        return relevantSkills;
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
