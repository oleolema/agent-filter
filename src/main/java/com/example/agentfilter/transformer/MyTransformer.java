package com.example.agentfilter.transformer;

import com.example.agentfilter.annotation.Transformer;
import com.example.agentfilter.core.ClassTransformer;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.security.ProtectionDomain;

/**
 * @author yueqiuhong
 * @date 2022/5/22
 */
@Slf4j
@Transformer
public class MyTransformer extends ClassTransformer {

    @Override
    public boolean needTransform(String className) {
        return "java.util.Date".equals(className);
    }

    @SneakyThrows
    @Override
    public void transform(CtClass ctClass, ClassPool classPool, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classFileBuffer) {
        CtMethod convertToAbbr = ctClass.getDeclaredMethod("toString");
        String methodBody = "{" +
                "System.out.println(com.example.agentfilter.transformer.MyTransformer.class);" +
                "" +
                "return  \"hello world!!!\";}";
        convertToAbbr.setBody(methodBody);
    }

}