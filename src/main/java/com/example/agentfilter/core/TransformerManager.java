package com.example.agentfilter.core;


import com.example.agentfilter.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author yueqiuhong
 * @date 2022/5/22
 */
@Slf4j
public class TransformerManager {

    private final Instrumentation inst;
    private final List<BaseTransformer> transformers = new ArrayList<>();

    private static final String NATIVE_PREFIX = StringUtils.randomMethodName(15) + "_";


    public TransformerManager(Instrumentation inst) {
        this.inst = inst;
    }

    public void addTransform(BaseTransformer transformer) {
        inst.addTransformer(transformer, true);
        inst.setNativeMethodPrefix(transformer, NATIVE_PREFIX);
        transformers.add(transformer);
    }

    public void retransform() {
        Arrays.stream(inst.getAllLoadedClasses()).filter(it -> transformers.stream().anyMatch(tran -> tran.isTransformClasses(it))).forEach(it -> {
            try {
                inst.retransformClasses(it);
            } catch (UnmodifiableClassException e) {
                log.error("error ", e);
                e.printStackTrace();
            }
        });
    }

}