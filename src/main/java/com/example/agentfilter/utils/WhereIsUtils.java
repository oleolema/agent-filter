package com.example.agentfilter.utils;

import com.example.agentfilter.AgentFilterApplication;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class WhereIsUtils {

    private static URI jarURI = null;
    private static final String JAVA_HOME = System.getProperty("java.home");

    public static File findJPS() {
        String[] paths = new String[]{"bin/jps", "bin/jps.exe", "../bin/jps", "../bin/jps.exe"};

        for (String path : paths) {
            File file = new File(JAVA_HOME, path);
            if (file.exists() && file.isFile() && file.canExecute()) {
                return getCanonicalFile(file);
            }
        }

        return null;
    }

    public static File findJava() {
        String[] paths = new String[]{"bin/java", "bin/java.exe", "../bin/java", "../bin/java.exe"};

        for (String path : paths) {
            File file = new File(JAVA_HOME, path);
            if (file.exists() && file.isFile() && file.canExecute()) {
                return getCanonicalFile(file);
            }
        }

        return null;
    }

    public static File findToolsJar() {
        String[] paths = new String[]{"lib/tools.jar", "../lib/tools.jar", "../../lib/tools.jar"};

        for (String path : paths) {
            File file = new File(JAVA_HOME, path);
            if (file.exists() && file.isFile()) {
                return getCanonicalFile(file);
            }
        }

        return null;
    }

    @SneakyThrows
    public static URI getJarURI() {
        if (jarURI != null) {
            return jarURI;
        }

        URL url = AgentFilterApplication.class.getProtectionDomain().getCodeSource().getLocation();
        if (null != url) {
            String path = url.getPath();
            if (path.endsWith("classes/")) {
                // 调试时使用
                path = path.substring(0, path.length() - 8) + "agent-filter-jar-with-dependencies.jar";
            }
            return URI.create(path);
        }

        String resourcePath = "/application.properties";
        url = AgentFilterApplication.class.getResource(resourcePath);
        if (null == url) {
            throw new Exception("Can not locate resource file.");
        }

        String path = url.getPath();
        if (!path.endsWith("!" + resourcePath)) {
            throw new Exception("Invalid resource path.");
        }

        path = path.substring(0, path.length() - resourcePath.length() - 1);

        jarURI = new URI(path);
        return jarURI;
    }

    private static File getCanonicalFile(File file) {
        try {
            return file.getCanonicalFile();
        } catch (IOException e) {
            return null;
        }
    }

    public static String getRelativePackagePath() {
        return AgentFilterApplication.class.getPackageName().replace(".", File.separator);
    }

    public static String getClassPath() {
        URL resource = Objects.requireNonNull(AgentFilterApplication.class.getResource("/application.properties"));
        return new File(resource.getFile()).getParentFile().getPath();
    }


    public static String getBootPath(String path) {
        URL resource = Objects.requireNonNull(AgentFilterApplication.class.getResource("/application.properties"));
        String transformerPath = new File(resource.getFile()).getParentFile().getPath() + File.separator + getRelativePackagePath() + File.separator + path;
        File file = new File(transformerPath);
        if (!file.exists()) {
            throw new IllegalArgumentException(String.format("File not found %s", transformerPath));
        }
        return file.getAbsolutePath();
    }

    private static List<File> listAllFiles(File file) {
        File[] listFiles = file.listFiles();
        if (listFiles == null) {
            return List.of();
        }
        return Arrays.stream(listFiles).flatMap(it -> it.isDirectory() ? listAllFiles(it).stream() : Stream.of(it)).collect(Collectors.toList());
    }

//    public static List<Class<?>> findAnnotation(String basePath, Class<? extends Annotation> annotation) {
//        File transformerFile = new File(WhereIsUtils.getBootPath(basePath));
//        String classPath = WhereIsUtils.getClassPath();
//        return listAllFiles(transformerFile).stream().map(it -> {
//            String path = it.getAbsolutePath();
//            String className = path.substring(classPath.length() + 1, path.length() - 6).replace(File.separator, ".");
//            try {
//                return Class.forName(className);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }).filter(it -> it.getAnnotation(annotation) != null).collect(Collectors.toList());
//    }

}
