package com.thomsonreuters.taklocwork;
import hudson.Launcher;
import hudson.Extension;
import hudson.FilePath;
import hudson.Proc;
import hudson.util.FormValidation;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.AbstractProject;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import java.io.BufferedReader;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Sample {@link Builder}.
 *
 * <p>
 * When the user configures the project and enables this builder,
 * {@link DescriptorImpl#newInstance(StaplerRequest)} is invoked
 * and a new {@link HelloWorldBuilder} is created. The created
 * instance is persisted to the project configuration XML by using
 * XStream, so this allows you to use instance fields (like {@link #name})
 * to remember the configuration.
 *
 * <p>
 * When a build is performed, the {@link #perform(AbstractBuild, Launcher, BuildListener)}
 * method will be invoked. 
 *
 * @author Kohsuke Kawaguchi
 */
public class KlocworkBuilder extends Builder {

    private final String command;

    // Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
    @DataBoundConstructor
    public KlocworkBuilder(String command) {
        this.command = command;
    }

    /**
     * We'll use this from the <tt>config.jelly</tt>.
     */
    public String getCommand() {
        return command;
    }

    // This is where you 'build' the project.
    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) throws IOException, InterruptedException {
        
        // Print debug method
        listener.getLogger().println("Build source code with command = "+command+"!");
        FilePath workspace = build.getProject().getWorkspace();
        // 1. Run Klocwork Shell and build. Example "kwshell make"
        Proc proc = launcher.launch("kwshell "+command, new String[0], listener.getLogger(), build.getProject().getWorkspace());
        int exitCode = proc.join();
        
        // 2. Run Klocwork Analyze and generate xml file
        launcher.launch("kwcheck run -F xml > klocwork.xml", new String[0], listener.getLogger(), build.getProject().getWorkspace());
        
        
        /*
         * Obsolete
        // 4. Parsing report file
        // Example - 1 (Local) /home12/atthaboon/klocwork/ATS143_BL0203/pdm/dts_file/DTSReject.cpp:102 NPD.FUNC.MUST (1:Critical) Analyze
        FilePath reportFile = new FilePath(workspace, "kwcheck_issues.log");
        InputStream is = reportFile.read();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer buf = new StringBuffer();      
        String str="";
        
        
        Pattern issue_pattern = Pattern.compile("[0-9]+ \\(Local\\) (.*?):([0-9]+) ([A-Za-z.]+) \\(([0-9]):([A-Za-z]+)\\)");
        if (is!=null) {                         
            while ((str = reader.readLine()) != null) { 
                // Parsing report
                Matcher m = issue_pattern.matcher(str);
                if( m.find())
                {
                    System.out.println("Capture Line = "+m.group());
                    System.out.println("File = "+m.group(1));
                    System.out.println("Line = "+m.group(2));
                    System.out.println("Rule = "+m.group(3));
                    System.out.println("Severity Number = "+m.group(4));
                    System.out.println("Severity Name   = "+m.group(5));
                }
            }
        }
        */
        
        // 5. Generate new report file
        
        
        return true;
    }

    // Overridden for better type safety.
    // If your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }

    /**
     * Descriptor for {@link HelloWorldBuilder}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     *
     * <p>
     * See <tt>src/main/resources/hudson/plugins/hello_world/HelloWorldBuilder/*.jelly</tt>
     * for the actual HTML fragment for the configuration screen.
     */
    @Extension // This indicates to Jenkins that this is an implementation of an extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
        /**
         * Performs on-the-fly validation of the form field 'name'.
         *
         * @param value
         *      This parameter receives the value that the user has typed.
         * @return
         *      Indicates the outcome of the validation. This is sent to the browser.
         */
        public FormValidation doCheckCommand(@QueryParameter String value)
                throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error("Please set a build command!");
            return FormValidation.ok();
        }

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project types 
            return true;
        }

        /**
         * This human readable name is used in the configuration screen.
         */
        public String getDisplayName() {
            return "KlocworkBuilder";
        }

    }
}

