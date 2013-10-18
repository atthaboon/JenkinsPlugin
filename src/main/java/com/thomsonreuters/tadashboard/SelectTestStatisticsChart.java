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
import hudson.plugins.view.dashboard.Messages;
import hudson.plugins.view.dashboard.test.TestResultSummary;
import hudson.plugins.view.dashboard.test.TestStatisticsChart;
import hudson.plugins.view.dashboard.test.TestUtil;
import hudson.util.ColorPalette;
import hudson.util.Graph;
import java.awt.Color;
import java.awt.Paint;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.kohsuke.stapler.DataBoundConstructor;
import sun.security.jca.GetInstance;

/**
 *
 * @author Atthaboon Sanurt
 */
//public class SystemTestPortlet extends DashboardPortlet {
public class SelectTestStatisticsChart extends TestStatisticsChart {
    public ArrayList<Checkbox> jobList;

    @DataBoundConstructor
    public SelectTestStatisticsChart(String name, ArrayList<Checkbox> jobList) {
        super(name);
        this.jobList = jobList;
    }

    // Override TestStatisticsChart
    @Override
    public Graph getSummaryGraph() {
        
        List<TopLevelItem> items = Utils.getSelectedItems(this.jobList);
        final TestResultSummary summary = TestUtil.getTestResultSummary(items);
        return new Graph(-1, 300, 220) {

         @Override
         protected JFreeChart createGraph() {
            DefaultPieDataset dataset = new DefaultPieDataset();
            dataset.setValue(Messages.Dashboard_Success(), summary.getSuccess());
            dataset.setValue(Messages.Dashboard_Skipped(), summary.getSkipped());
            dataset.setValue(Messages.Dashboard_Failed(), summary.getFailed());
            JFreeChart chart = ChartFactory.createPieChart(null, dataset, false, false, false);

            chart.setBackgroundPaint(Color.white);

            final PiePlot plot = (PiePlot) chart.getPlot();

            // plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
            plot.setBackgroundPaint(Color.WHITE);
            plot.setOutlinePaint(null);
            plot.setForegroundAlpha(0.8f);

            // create arraylist of paint color, adding only color if related stat is > 0
            List<Paint> paints = new ArrayList<Paint>();
            if (summary.getSuccess() > 0) {
               paints.add(ColorPalette.BLUE);
            }
            if (summary.getSkipped() > 0) {
               paints.add(ColorPalette.YELLOW);
            }
            if (summary.getFailed() > 0) {
               paints.add(ColorPalette.RED);
            }

            DefaultDrawingSupplier ds = new DefaultDrawingSupplier(
                    paints.toArray(new Paint[0]),
                    DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
                    DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
                    DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
                    DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE);
            plot.setDrawingSupplier(ds);

            plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
                    "{0} = {1} ({2})", NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance()));
            plot.setNoDataMessage(Messages.Dashboard_NoDataAvailable());

            return chart;
         }
      };
    }
    
    public ArrayList<Checkbox> getJobList() {
        this.jobList = Utils.updateJobList(jobList); // Force update job list first for support new job.
        return jobList;
    }

    public void setJobList(ArrayList<Checkbox> jobList) {
        this.jobList = jobList;
    }
    
    @Extension // This indicates to Jenkins that this is an implementation of an extension point.
    public static final class DescriptorImpl extends Descriptor<DashboardPortlet> {
        
        @Override
        public String getDisplayName() {
            return "Select Test Statistics Chart";
        }
        
        public ArrayList<Checkbox> getFillJobNameItems() {
            return Utils.getFillJobNameItems();
        }
    }
}
