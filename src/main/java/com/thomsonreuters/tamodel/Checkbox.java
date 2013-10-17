/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thomsonreuters.tamodel;

import hudson.Extension;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 *
 * @author u8018728
 */
public class Checkbox implements Describable<Checkbox> {
    
    private String name;
    private boolean selected;
    
    @DataBoundConstructor
    public Checkbox(final String name, final boolean selected) 
    {
        this.name = name;
        this.selected = selected;
    }
    
    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return selected;
    }
    
    public void setSelect(boolean selected) {
        this.selected = selected;
    }

    public Descriptor<Checkbox> getDescriptor() 
    {
        return Hudson.getInstance().getDescriptor(getClass());
    }
    
    @Extension
    public static class DescriptorImpl extends Descriptor<Checkbox> 
    {
        @Override
        public String getDisplayName() 
        {
            return "Check Box";
        }
    }
}
