package com.example.agentfilter.transformer;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author yueqiuhong
 * @date 2022/5/22
 */
@Log4j2
public class MyTransformer extends BaseTransformer {

    protected Set<String> retransformClasses = Set.of("java.util.Date", "java.math.BigInteger");

    @Override
    public Set<String> getRetransformClasses() {
        return retransformClasses;
    }

    @Override
    public byte[] transform(final ClassPool classPool, final ClassLoader loader, String className, final Class<?> classBeingRedefined, final ProtectionDomain protectionDomain, final byte[] classfileBuffer) {
        className = className.replace("/", ".");
        log.info("transform: {}", className);

        // 操作Date类
        if (retransformClasses.contains(className)) {
            log.info("begin transform: {}", className);
            try {
                // 从ClassPool获得CtClass对象
                final CtClass clazz = classPool.get(className);
                CtMethod convertToAbbr = clazz.getDeclaredMethod("toString");
                String methodBody = "{return \"hello\";}";
                convertToAbbr.setBody(methodBody);
                // 返回字节码，并且detachCtClass对象
                byte[] byteCode = clazz.toBytecode();
                //detach的意思是将内存中曾经被javassist加载过的Date对象移除，如果下次有需要在内存中找不到会重新走javassist加载
                clazz.detach();
                return byteCode;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        // 如果返回null则字节码不会被修改
        return null;
    }

}