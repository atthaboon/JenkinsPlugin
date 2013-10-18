/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thomsonreuters.tadashboard;

import com.thomsonreuters.tamodel.Checkbox;
import com.thomsonreuters.util.Utils;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.TopLevelItem;
import hudson.plugins.view.dashboard.DashboardPortlet;
import hudson.plugins.view.dashboard.test.TestResult;
import hudson.plugins.view.dashboard.test.TestResultSummary;
import hudson.plugins.view.dashboard.test.TestUtil;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 *
 * @author Atthaboon Sanurt
 */
public class SelectTestStatisticsPortlet extends DashboardPortlet {
    public ArrayList<Checkbox> jobList;
    
    private boolean useBackgroundColors;
    private String skippedColor;
    private String successColor;
    private String failureColor;
    
    @DataBoundConstructor
    public SelectTestStatisticsPortlet(String name, String successColor, String failureColor, String skippedColor, boolean useBackgroundColors, ArrayList<Checkbox> jobList) {
        super(name);
        this.successColor = successColor;
        this.failureColor = failureColor;
        this.skippedColor = skippedColor;
        this.useBackgroundColors = useBackgroundColors;
        this.jobList = jobList;
    }
    
    public TestResultSummary getTestResultSummary(Collection<TopLevelItem> jobs) {
        List<TopLevelItem> items = Utils.getSelectedItems(this.jobList);
        return TestUtil.getTestResultSummary(items);
    }

    public String format(DecimalFormat df, double val) {
        if (val < 1d && val > .99d) {
                return "<100%";
        }
        if (val > 0d && val < .01d) {
                return ">0%";
        }
        return df.format(val);
    }

    public boolean isUseBackgroundColors() {
        return useBackgroundColors;
    }

    public String getSuccessColor() {
        return successColor;
    }

    public String getFailureColor() {
        return failureColor;
    }

    public String getSkippedColor() {
        return skippedColor;
    }

    public String getRowColor(TestResult testResult) {
        return testResult.getSuccess() == testResult.getTests() ? successColor : failureColor;
    }

    public String getTotalRowColor(List<TestResult> testResults) {
        for(TestResult testResult : testResults) {
            if(testResult.getSuccess() != testResult.getTests()) {
                return failureColor;
            }
        }
        return successColor;
    }

    public void setUseBackgroundColors(boolean useBackgroundColors) {
        this.useBackgroundColors = useBackgroundColors;
    }

    public void setSkippedColor(String skippedColor) {
        this.skippedColor = skippedColor;
    }

    public void setSuccessColor(String successColor) {
        this.successColor = successColor;
}

    public void setFailureColor(String failureColor) {
        this.failureColor = failureColor;
    }
    
    public ArrayList<Checkbox> getJobList() {
        this.jobList = Utils.updateJobList(jobList); // Force update job list first for support new job.
        return jobList;
    }

    public void setJobList(ArrayList<Checkbox> jobList) {
        this.jobList = jobList;
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<DashboardPortlet> {
        @Override
        public String getDisplayName() {
            return "Select Test Statistics Grid";
        }
        
        public ArrayList<Checkbox> getFillJobNameItems() {
            return Utils.getFillJobNameItems();
        }
    }
}
