package com.leofesk.quicktodomanager.model;

import com.leofesk.quicktodomanager.controller.DataBaseWorker;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Options {
    private static String defaultOptionsFilePathWindows = "C:\\Users\\" + getUsername() + "\\AppData\\Roaming\\Leofesk.Ru\\QuickToDoManager\\options\\config.properties";
    private static String defaultDatabasePathWindows = "C:\\Users\\" + getUsername() + "\\Documents\\QuickToDoManager\\db\\";
    private static String defaultOptionsFilePathLinux = getUserCurrentDir() + "/QuickToDoManager/options/config.properties";
    private static String defaultDatabasePathLinux = getUserCurrentDir() + "/QuickToDoManager/db/";
    private static String currentOptionsFilePath = "";
    private static Properties options = new Properties();
    private static FileOutputStream output = null;
    private static Path path = null;

    public static void initOptionsFile() {
        if (identifyOS().equals("Windows")) {
            try {
                path = Paths.get("C:\\Users\\" + getUsername() + "\\AppData\\Roaming\\Leofesk.Ru\\QuickToDoManager\\options");
                Files.createDirectories(path);
                path = Paths.get("C:\\Users\\" + getUsername() + "\\Documents\\QuickToDoManager\\db");
                Files.createDirectories(path);
            } catch (IOException e) {
                DataBaseWorker.showMessage("Can't create folder. Check your rights. [CODE:M_OP_001]");
            }
            currentOptionsFilePath = defaultOptionsFilePathWindows;
        } else {
            try {
                path = Paths.get(getUserCurrentDir() + "/QuickToDoManager/options");
                Files.createDirectories(path);
                path = Paths.get(getUserCurrentDir() + "/QuickToDoManager/db");
                Files.createDirectories(path);
            } catch (IOException e) {
                DataBaseWorker.showMessage("Can't create folder. Check your rights. [CODE:M_OP_002]");
            }
            currentOptionsFilePath = defaultOptionsFilePathLinux;
        }

        File tempFile = new File(currentOptionsFilePath);
        if (tempFile.exists() && !tempFile.isDirectory()) {
            loadExistingOptionsFile();
        } else {
            createNewOptionsFile();
        }
    }

    private static void loadExistingOptionsFile() {
        try {
            options.load(new FileReader(currentOptionsFilePath));
        } catch (IOException e) {
            DataBaseWorker.showMessage("Can't load config file. [CODE:M_OP_003]");
        }
    }

    private static void createNewOptionsFile() {
        try {
            options.setProperty("currentOS", identifyOS());
            if (identifyOS().equals("Windows")) {
                options.setProperty("defaultDatabaseFilePath", defaultDatabasePathWindows);
                options.setProperty("customDatabasePath", defaultDatabasePathWindows);
            } else {
                options.setProperty("defaultDatabaseFilePath", defaultDatabasePathLinux);
                options.setProperty("customDatabasePath", defaultDatabasePathLinux);
            }
            options.setProperty("databaseName", "notes");
            options.setProperty("language", "eng");
            options.setProperty("version", "1.1.6");
            output = new FileOutputStream(currentOptionsFilePath);
            options.store(output, "QTDM - Default options file.");
        } catch (IOException e) {
            DataBaseWorker.showMessage("Can't create new config file. Check your rights. [CODE:M_OP_004]");
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException ex) {
                    DataBaseWorker.showMessage("Can't create new config file. Check your rights. [CODE:M_OP_005]");
                }
            }
        }
    }

    public static void updateOptionsValue(String key, String value) {
        try {
            options.load(new FileReader(currentOptionsFilePath));
            options.setProperty(key, value);
            output = new FileOutputStream(currentOptionsFilePath);
            options.store(output, "QTDM - Updated options file.");
        } catch (IOException e) {
            DataBaseWorker.showMessage("Can't update config file. [CODE:M_OP_006]");
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException ex) {
                    DataBaseWorker.showMessage("Can't update config file. [CODE:M_OP_007]");
                }
            }
        }
    }

    private static String identifyOS() {
        if (isWindows()) {
            return "Windows";
        } else if (isUnix()) {
            return "Linux";
        }
        return "Not identified OS";
    }

    private static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("win") >= 0);
    }

    private static boolean isUnix() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
    }

    public static String getOptionsValue(String key) {
        return options.getProperty(key);
    }

    private static String getUsername() {
        return System.getProperty("user.name");
    }

    private static String getUserCurrentDir() {
        return System.getProperty("user.dir");
    }
}
