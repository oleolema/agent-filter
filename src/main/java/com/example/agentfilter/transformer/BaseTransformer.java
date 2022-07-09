package com.example.agentfilter.transformer;

import javassist.ClassPool;
import lombok.extern.log4j.Log4j2;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.List;
import java.util.Set;

/**
 * @author yueqiuhong
 * @date 2022/5/22
 */
@Log4j2
abstract public class BaseTransformer implements ClassFileTransformer {

    protected final ClassPool classPool = ClassPool.getDefault();

    public Set<String> getRetransformClasses() {
        return Set.of();
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classFileBuffer) {
        return transform(classPool, loader, className, classBeingRedefined, protectionDomain, classFileBuffer);
    }

    public byte[] transform(ClassPool classPool, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classFileBuffer) {
        log.info("transform: {}", className);
        return classFileBuffer;
    }

}