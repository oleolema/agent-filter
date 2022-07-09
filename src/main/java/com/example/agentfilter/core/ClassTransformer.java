package com.example.agentfilter.core;

import javassist.ClassPool;
import javassist.CtClass;
import lombok.extern.slf4j.Slf4j;

import java.security.ProtectionDomain;

/**
 * @author yueqiuhong
 * @date 2022/5/22
 */
@Slf4j
abstract public class ClassTransformer extends BaseTransformer {

    abstract public boolean needTransform(String className);

    @Override
    public boolean isTransformClasses(Class<?> loadedClasses) {
        return needTransform(loadedClasses.getName());
    }

    @Override
    public byte[] transform(ClassPool classPool, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classFileBuffer) {
        log.info("transform: {}", className);
        // 操作Date类
        if (needTransform(className)) {
            log.info("begin transform: {}", className);
            try {
                // 从ClassPool获得CtClass对象
                final CtClass ctClass = classPool.get(className);
                transform(ctClass, classPool, loader, className, classBeingRedefined, protectionDomain, classFileBuffer);
                // 返回字节码，并且detachCtClass对象
                byte[] byteCode = ctClass.toBytecode();
                //detach的意思是将内存中曾经被javassist加载过的Date对象移除，如果下次有需要在内存中找不到会重新走javassist加载
                ctClass.detach();
                return byteCode;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        // 如果返回null则字节码不会被修改
        return null;
    }

    abstract public void transform(CtClass ctClass, ClassPool classPool, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classFileBuffer);

}