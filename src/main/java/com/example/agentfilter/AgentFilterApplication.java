package com.example.agentfilter;

import com.example.agentfilter.attach.VMSelector;
import com.example.agentfilter.transformer.MyTransformer;
import com.example.agentfilter.transformer.TransformerManager;
import com.example.agentfilter.utils.WhereIsUtils;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.net.URI;
import java.util.jar.JarFile;

public class AgentFilterApplication {
    public static final String VERSION = "2022.2.0";


    public static void main(String[] args) {
        URI jarURI = WhereIsUtils.getJarURI();
        printUsage();
        String jarPath = jarURI.getPath();
        new VMSelector(new File(jarPath)).select();
    }

    private static void printUsage() {
        String content = "\n  ============================================================================  \n" +
                "\n" +
                "    Hello Agent Filter " + VERSION +
                "\n\n" +
                "    A javaagent framework :)\n" +
                "\n" +
                "  ============================================================================  \n\n";

        System.out.print(content);
    }

    public static void agentmain(String args, Instrumentation inst) {
        premain(args, inst, true);
    }

    public static void premain(String args, Instrumentation inst) {
        try {
            premain(args, inst, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    private static void premain(String args, @NotNull Instrumentation inst, boolean attachMode) {
        printUsage();
        URI jarURI = WhereIsUtils.getJarURI();
        File agentFile = new File(jarURI.getPath());
        inst.appendToBootstrapClassLoaderSearch(new JarFile(agentFile));
        TransformerManager transformerManager = new TransformerManager(inst);
        transformerManager.addTransform(new MyTransformer());
        transformerManager.retransform();
    }


}
