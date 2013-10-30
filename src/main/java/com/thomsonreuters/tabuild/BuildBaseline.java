package com.thomsonreuters.tabuild;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import java.io.IOException;
import org.kohsuke.stapler.DataBoundConstructor;


/**
 * Copy all artifact into NAS baseline folder.
 * 
 * @author Atthaboon Sanurt
 */
public class BuildBaseline extends Notifier {
    public boolean is_baseline;
 
    @DataBoundConstructor
    public BuildBaseline(boolean is_baseline)
    {
        this.is_baseline = is_baseline;
    }
    
    public boolean isIs_baseline() {
        return is_baseline;
    }

    public void setIs_baseline(boolean is_baseline) {
        this.is_baseline = is_baseline;
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }
    
    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        // Enable if set baseline is true
        if(this.is_baseline)
        {
            // Auto set baseline flag to false
            this.is_baseline = false;
            
        }
        return true;
    }
    
    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Build Baseline";
        }
    }
}
