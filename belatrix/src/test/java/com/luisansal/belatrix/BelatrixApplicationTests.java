package com.luisansal.belatrix;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BelatrixApplicationTests {

    @Test
    public void contextLoads() {
        boolean isValidConfiguration = isValidConfiguration(true, true, true);
        boolean areCorrectParameters = areCorrectParametersToContinueLog(true, true, true, true, true, true);
        System.out.println("IsVaidConfiguration: " + isValidConfiguration);
        System.out.println("AreCorrectParameters: " + areCorrectParameters);
    }

    public boolean isValidConfiguration(boolean logToConsole, boolean logToFile, boolean logToDatabase) {
        return JobLogger.isValidConfiguration(logToConsole, logToFile, logToDatabase);
    }

    public boolean areCorrectParametersToContinueLog(boolean logError, boolean logMessage, boolean logWarning, boolean message, boolean warning, boolean error) {
        return JobLogger.areCorrectParametersToContinueLog(logError, logMessage, logWarning, message, warning, error);
    }

    @Test
    public void testAreDatabaseConnected() {
        HashMap<String, String> dbParams = new HashMap<>();
        dbParams.put("userName", "root");
        dbParams.put("password", "admin");
        dbParams.put("dbms", "mysql");
        dbParams.put("serverName", "localhost");
        dbParams.put("dbName", "bd_belatrix");
        dbParams.put("portNumber", "3306");
        dbParams.put("logFileFolder", "C:\\data");
        JobLogger.isDatabaseConnected(dbParams);
    }
}
