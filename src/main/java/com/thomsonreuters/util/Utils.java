/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thomsonreuters.util;

import com.thomsonreuters.tamodel.Checkbox;
import hudson.model.Hudson;
import hudson.model.TopLevelItem;
import java.util.ArrayList;
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
        for (TopLevelItem project : Hudson.getInstance().getItems()) {
            Checkbox job = new Checkbox(project.getName(), false);
            newJobList.add(job);
            for (Checkbox jobSelect : jobList) {
                if(job.getName().equals(jobSelect.getName()))
                {
                    job.setSelect(jobSelect.isSelected());
                    break;
                }
            }
        }
        return newJobList;
    }
    
    public static ArrayList<Checkbox> getFillJobNameItems() {
        ArrayList<Checkbox> jobList = new ArrayList<Checkbox>();
        for (TopLevelItem project : Hudson.getInstance().getItems()) {
            Checkbox job = new Checkbox(project.getName(), false);
            jobList.add(job);
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
    
    
}
