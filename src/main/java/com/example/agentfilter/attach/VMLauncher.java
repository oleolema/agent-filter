package com.example.agentfilter.attach;


import com.example.agentfilter.utils.ProcessUtils;
import com.example.agentfilter.utils.WhereIsUtils;
import com.sun.tools.attach.VirtualMachine;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;

@Log4j2
public class VMLauncher {
    public static void attachVM(String agentFile, String pid, String args) {
        log.info(String.format("agentFile: %s, pid: %s, args: %s", agentFile, pid, args));
        try {
            VirtualMachine vm = VirtualMachine.attach(pid);
            vm.loadAgent(agentFile, args);
            vm.detach();
        } catch (IOException e) {
            if (e.getMessage().startsWith("Non-numeric value found")) {
                log.info("WARN: The jdk used by `ja-netfilter` does not match the attached jdk version");
            }
        } catch (Throwable e) {
            log.error("Attach failed: " + pid);
            throw new RuntimeException(e);
        }

        log.info("ATTACHED SUCCESSFULLY: " + pid);
    }

    public static void launch(File thisJar, VMDescriptor descriptor, String args) throws Exception {
        File javaCommand = WhereIsUtils.findJava();
        if (null == javaCommand) {
            throw new Exception("Can not locate java command, unable to start attach mode.");
        }

        ProcessBuilder pb;
        double version = Double.parseDouble(System.getProperty("java.specification.version"));
        if (version > 1.8D) {
            pb = buildProcess(javaCommand, thisJar, descriptor.getId(), args);
        } else {
            File toolsJar = WhereIsUtils.findToolsJar();
            if (null == toolsJar) {
                throw new Exception("Can not locate tools.jar file, unable to start attach mode.");
            }

            pb = buildProcess(javaCommand, thisJar, descriptor.getId(), args, toolsJar);
        }
        log.info(pb.command().toString());
        int exitValue = ProcessUtils.start(pb);
        if (0 != exitValue) {
            throw new Exception("Attach mode failed: " + exitValue);
        }
    }

    private static ProcessBuilder buildProcess(File java, File thisJar, String id, String args) {
        String[] cmdArray = new String[]{
                java.getAbsolutePath(),
                "-Djanf.debug=" + System.getProperty("janf.debug", "0"),
                "-jar",
                thisJar.getAbsolutePath(),
                "--attach",
                id, args
        };

        return new ProcessBuilder(cmdArray);
    }

    private static ProcessBuilder buildProcess(File java, File thisJar, String id, String args, File toolsJar) {
        String[] cmdArray = new String[]{
                java.getAbsolutePath(),
                "-Djanf.debug=" + System.getProperty("janf.debug", "0"),
                "-Xbootclasspath/a:" + toolsJar.getAbsolutePath(),
                "-jar",
                thisJar.getAbsolutePath(),
                "--attach",
                id, args
        };

        return new ProcessBuilder(cmdArray);
    }
}
