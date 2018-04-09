package com.luisansal.belatrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;

@SpringBootApplication
public class BelatrixApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(BelatrixApplication.class, args);
		HashMap<String,String> dbParams = new HashMap<>();
		dbParams.put("userName","root");
		dbParams.put("password","admin");
		dbParams.put("dbms","mysql");
		dbParams.put("serverName","localhost");
		dbParams.put("dbName","bd_belatrix");
		dbParams.put("portNumber","3306");
		dbParams.put("logFileFolder","C:\\data");

		new JobLogger(true,true,true,true,true,true,dbParams);
		JobLogger.LogMessage("Custom Message Text",true,true,true);
	}
}

