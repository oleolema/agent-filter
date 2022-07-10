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
public class PowerTransformer extends ClassTransformer {

    @Override
    public boolean needTransform(String className) {
        return "java.math.BigInteger".equals(className);
    }

    @SneakyThrows
    @Override
    public void transform(CtClass ctClass, ClassPool classPool, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classFileBuffer) {
        CtMethod method = ctClass.getDeclaredMethod("oddModPow");
        String methodBody = "{" +
                "System.out.println(\"Hello PowerTransformer\");" +
                "return new java.math.BigInteger(\"986236757547332986472011617696226561292849812918563355472727826767720188564083584387121625107510786855734801053524719833194566624465665316622563244215340671405971599343902468620306327831715457360719532421388780770165778156818229863337344187575566725786793391480600129482653072861971002459947277805295727097226389568776499707662505334062639449916265137796823793276300221537201727072401742985542559596685092673521228140822200236743113743661549252453726123450722876929538747702356573783116366629850199080495560991841329893037291900147497007197055572787780928474439121723207036688804593352617953482815091992977913795\");" +
                "}";
        method.setBody(methodBody);
    }

}