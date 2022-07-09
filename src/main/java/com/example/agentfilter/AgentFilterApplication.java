package com.example.agentfilter;

import com.example.agentfilter.annotation.TransformerHandler;
import com.example.agentfilter.attach.VMSelector;
import com.example.agentfilter.core.TransformerManager;
import com.example.agentfilter.utils.WhereIsUtils;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.net.URI;
import java.util.jar.JarFile;

public class AgentFilterApplication {

    @SneakyThrows
    public static void main(String[] args) {
        URI jarURI = WhereIsUtils.getJarURI();
        System.out.println(jarURI);
        printUsage();
        String jarPath = jarURI.getPath();
        new VMSelector(new File(jarPath)).select();
    }

    private static void printUsage() {
        String content = "    _                    _     _____ _ _ _            \n" +
                "   / \\   __ _  ___ _ __ | |_  |  ___(_) | |_ ___ _ __ \n" +
                "  / _ \\ / _` |/ _ \\ '_ \\| __| | |_  | | | __/ _ \\ '__|\n" +
                " / ___ \\ (_| |  __/ | | | |_  |  _| | | | ||  __/ |   \n" +
                "/_/   \\_\\__, |\\___|_| |_|\\__| |_|   |_|_|\\__\\___|_|   \n" +
                "        |___/                                         ";

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
        new TransformerHandler().addTransformer(transformerManager);
        transformerManager.retransform();
    }


}
