package com.example.agentfiltertest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigInteger;
import java.util.Date;

@SpringBootApplication
public class AgentFilterTestApplication {

	public static void main(String[] args) {
//		SpringApplication.run(AgentFilterTestApplication.class, args);
		System.out.println("Hello world!");
		System.out.println(new BigInteger("0"));
		System.out.println(new Date());

	}

}
