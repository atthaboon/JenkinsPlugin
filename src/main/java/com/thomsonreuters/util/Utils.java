/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thomsonreuters.util;

import com.thomsonreuters.tamodel.Checkbox;
import hudson.model.Hudson;
import hudson.model.Item;
import hudson.model.Job;
import hudson.model.TopLevelItem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author u8018728
 */
public class Utils {
    public static ArrayList<Checkbox> updateJobList(ArrayList<Checkbox> jobList)
    {        
        ArrayList<Checkbox> newJobList = new ArrayList<Checkbox>();        
        for (TopLevelItem job : Hudson.getInstance().getItems()) {
            Checkbox j_checkbox = new Checkbox(job.getName(), false);
            newJobList.add(j_checkbox);
            for (Checkbox jobSelect : jobList) {
                if(j_checkbox.getName().equals(jobSelect.getName()))
                {
                    j_checkbox.setSelect(jobSelect.isSelected());
                    break;
                }
            }
        }
        return newJobList;
    }
    
    public static ArrayList<Checkbox> getFillJobNameItems() {
        ArrayList<Checkbox> jobList = new ArrayList<Checkbox>();
        for (TopLevelItem job : Hudson.getInstance().getItems()) {
            Checkbox j_checkbox = new Checkbox(job.getName(), false);
            jobList.add(j_checkbox);
        }
        return jobList;
    }
    
    public static List<TopLevelItem> getSelectedItems(ArrayList<Checkbox> jobList){
        List<TopLevelItem> items = new LinkedList<TopLevelItem>();
        for (TopLevelItem item : Hudson.getInstance().getItems()) {
            for (Checkbox jobSelect : jobList) {
                if(item.getName().equals(jobSelect.getName()) && jobSelect.isSelected())
                {
                    items.add(item);
                    break;
                }
            }
        }
        return items;
    }
    
    public static List<Job> getSelectedJobs(ArrayList<Checkbox> jobList){
        List<Job> jobsL = new LinkedList<Job>();
        for (TopLevelItem item : Hudson.getInstance().getItems()) {
            jobsL.addAll(item.getAllJobs());
        }
        List<Job> jobs = new LinkedList<Job>();
        for (Job job : jobsL) {
            for (Checkbox jobSelect : jobList) {
                if(job.getName().equals(jobSelect.getName()) && jobSelect.isSelected())
                {
                    jobs.add(job);
                    break;
                }
            }
        }
        return jobs;
    }
}
