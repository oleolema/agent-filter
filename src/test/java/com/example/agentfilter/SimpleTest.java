package com.example.agentfilter;

import com.example.agentfilter.utils.WhereIsUtils;
import lombok.SneakyThrows;

/**
 * @author yueqiuhong
 * @date 2022/5/22
 */
public class SimpleTest {

    @SneakyThrows
    public static void main(String[] args) {
        System.out.println(AgentFilterApplication.class.getProtectionDomain().getCodeSource().getLocation());
        System.out.println(WhereIsUtils.getBootPath(""));
        System.out.println(AgentFilterApplication.class.getProtectionDomain().getClassLoader().getResource(""));
    }

}