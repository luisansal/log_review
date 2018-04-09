package com.luisansal.belatrix;

/**
 * Created by Luis Sánchez
 */
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JobLogger {
    private static boolean logToFile;
    private static boolean logToConsole;
    private static boolean logMessage;
    private static boolean logWarning;
    private static boolean logError;
    private static boolean logToDatabase;
    private boolean initialized;
    private static Map dbParams;
    private static Logger logger;

    public JobLogger(boolean logToFileParam, boolean logToConsoleParam, boolean logToDatabaseParam,
                     boolean logMessageParam, boolean logWarningParam, boolean logErrorParam, Map dbParamsMap) {
        logger = Logger.getLogger("MyLog");
        logError = logErrorParam;
        logMessage = logMessageParam;
        logWarning = logWarningParam;
        logToDatabase = logToDatabaseParam;
        logToFile = logToFileParam;
        logToConsole = logToConsoleParam;
        dbParams = dbParamsMap;
    }

    public static void LogMessage(String messageText, boolean message, boolean warning, boolean error) throws Exception {
        if (messageText != null && messageText.length() == 0) {
            return;
        } else {
            if (messageText != null) {
                messageText.trim();
            }
        }

        if (!isValidConfiguration(logToConsole, logToFile, logToDatabase)) {
            throw new Exception("Invalid configuration");
        }
        if (!areCorrectParametersToContinueLog(logError, logMessage, logWarning, message, warning, error)) {
            throw new Exception("Error or Warning or Message must be specified");
        }

        Statement stmt = isDatabaseConnected(dbParams);
        if (stmt == null) {
            throw new SQLException("Database Connection Exception");
        }

        String l = null;
        int t = 0;

        File logFile = new File(dbParams.get("logFileFolder") + "/logFile.txt");
        if (!logFile.exists()) {
            logFile.createNewFile();
        }

        FileHandler fh = new FileHandler(dbParams.get("logFileFolder") + "/logFile.txt");
        ConsoleHandler ch = new ConsoleHandler();

        if (logToFile) {
            logger.addHandler(fh);
            if (error && logError) {
                t = 2;
                l = "error: " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + " " + messageText + "\n";
                logger.log(Level.INFO, l);
            }

            if (warning && logWarning) {
                t = 3;
                l = l + "warning: " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + " " + messageText;
                logger.log(Level.INFO, l);
            }
        }

        if (logToConsole) {
            //removed this log handler file, because this will be show only on console.
            logger.removeHandler(fh);
            logger.addHandler(ch);
            if (message && logMessage) {
                t = 1;
                l = "message: " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + " " + messageText;
            }

            logger.log(Level.INFO, l);
        }

        if (logToDatabase) {
            stmt.executeUpdate("insert into Log_Values VALUES ('" + message + "', " + String.valueOf(t) + ")");
        }
    }

    static boolean isValidConfiguration(boolean logToConsole, boolean logToFile, boolean logToDatabase) {
        if (!logToConsole && !logToFile && !logToDatabase) {
            return false;
        }
        return true;
    }

    static boolean areCorrectParametersToContinueLog(boolean logError, boolean logMessage, boolean logWarning, boolean message, boolean warning, boolean error) {
        if ((!logError && !logMessage && !logWarning) || (!message && !warning && !error)) {
            return false;
        }
        return true;
    }

    static Statement isDatabaseConnected(Map dbParams) {
        try {
            Connection connection = null;
            Properties connectionProps = new Properties();
            connectionProps.put("user", dbParams.get("userName"));
            connectionProps.put("password", dbParams.get("password"));

            connection = DriverManager.getConnection("jdbc:" + dbParams.get("dbms") + "://" + dbParams.get("serverName")
                    + ":" + dbParams.get("portNumber") + "/" + dbParams.get("dbName"), connectionProps);

            Statement stmt = connection.createStatement();
            return stmt;
        } catch (SQLException ex) {
            return null;
        }
    }
}
