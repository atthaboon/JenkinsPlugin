/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thomsonreuters.util;

import com.thomsonreuters.tamodel.Checkbox;
import hudson.model.Hudson;
import hudson.model.Job;
import hudson.model.TopLevelItem;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author u8018728
 */
public class Utils {
    public static ArrayList<Checkbox> updateJobList(List<Job> jobsL, ArrayList<Checkbox> jobList)
    {        
        ArrayList<Checkbox> newJobList = new ArrayList<Checkbox>();        
        for (Job job : jobsL) {
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
    
    public static ArrayList<Checkbox> getFillJobNameItems(List<Job> jobsL) {
        ArrayList<Checkbox> jobList = new ArrayList<Checkbox>();
        for (Job job : jobsL) {
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
    
    public static List<Job> getSelectedJobs(List<Job> jobsL ,ArrayList<Checkbox> jobList){
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
