package com.example.agentfilter.transformer;


import com.example.agentfilter.utils.StringUtils;
import lombok.extern.log4j.Log4j2;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yueqiuhong
 * @date 2022/5/22
 */
@Log4j2
public class TransformerManager {

    private Instrumentation inst;
    private List<BaseTransformer> transformers = new ArrayList<>();

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
        Set<String> classesSet = transformers.stream().flatMap(it -> it.getRetransformClasses().stream()).collect(Collectors.toSet());
        Arrays.stream(inst.getAllLoadedClasses()).filter(it -> classesSet.contains(it.getName())).forEach(it -> {
            try {
                inst.retransformClasses(it);
            } catch (UnmodifiableClassException e) {
                log.error(e);
                e.printStackTrace();
            }
        });
    }

}