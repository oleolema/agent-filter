package com.example.agentfilter.core;

import javassist.ClassPool;
import lombok.extern.slf4j.Slf4j;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

/**
 * @author yueqiuhong
 * @date 2022/5/22
 */
@Slf4j
abstract public class BaseTransformer implements ClassFileTransformer {

    protected final ClassPool classPool = ClassPool.getDefault();

    public boolean isTransformClasses(Class<?> loadedClasses) {
        return false;
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classFileBuffer) {
        className = className.replace("/", ".");
        return transform(classPool, loader, className, classBeingRedefined, protectionDomain, classFileBuffer);
    }

    public byte[] transform(ClassPool classPool, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classFileBuffer) {
        log.info("transform: {}", className);
        return classFileBuffer;
    }

}