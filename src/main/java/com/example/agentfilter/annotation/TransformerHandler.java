package com.example.agentfilter.annotation;

import com.example.agentfilter.core.BaseTransformer;
import com.example.agentfilter.core.TransformerManager;
import com.example.agentfilter.utils.ReflectUtils;

/**
 * @author yueqiuhong
 * @date 2022/7/9
 */
public class TransformerHandler {

    public void addTransformer(TransformerManager transformerManager) {
        ReflectUtils.getClassesByAnnotation("com.example.agentfilter.transformer", Transformer.class)
                .forEach(it -> {
                    try {
                        BaseTransformer baseTransformer = (BaseTransformer) it.getDeclaredConstructor().newInstance();
                        transformerManager.addTransform(baseTransformer);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

    }

}