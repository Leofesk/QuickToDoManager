package com.leofesk.quicktodomanager.model;

import com.leofesk.quicktodomanager.controller.DataBaseWorker;
import com.leofesk.quicktodomanager.controller.Message;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

public class Options {
    private static final String currentAppVersion = "1.9.13";
    private static String defaultOptionsFilePathWindows = "C:\\Users\\" + getUsername() + "\\AppData\\Roaming\\Leofesk.Ru\\QuickToDoManager\\options\\config.properties";
    private static String defaultDatabasePathWindows = "C:\\Users\\" + getUsername() + "\\Documents\\QuickToDoManager\\db\\";
    private static String defaultOptionsFilePathLinux = getUserCurrentDir() + "/QuickToDoManager/options/config.properties";
    private static String defaultDatabasePathLinux = getUserCurrentDir() + "/QuickToDoManager/db/";
    private static String currentOptionsFilePath = "";
    private static Properties options = new Properties();
    private static FileOutputStream output = null;
    private static Path path = null;
    private static String lang;
    private static String country;
    private static ResourceBundle langPack;

    public static void initOptionsFile() {
        if (identifyOS().equals("Windows")) {
            try {
                path = Paths.get("C:\\Users\\" + getUsername() + "\\AppData\\Roaming\\Leofesk.Ru\\QuickToDoManager\\options");
                Files.createDirectories(path);
                path = Paths.get("C:\\Users\\" + getUsername() + "\\Documents\\QuickToDoManager\\db");
                Files.createDirectories(path);
            } catch (IOException e) {
                DataBaseWorker.showMessage(Message.getText("optionsInitWinCatch"));
            }
            currentOptionsFilePath = defaultOptionsFilePathWindows;
        } else {
            try {
                path = Paths.get(getUserCurrentDir() + "/QuickToDoManager/options");
                Files.createDirectories(path);
                path = Paths.get(getUserCurrentDir() + "/QuickToDoManager/db");
                Files.createDirectories(path);
            } catch (IOException e) {
                DataBaseWorker.showMessage(Message.getText("optionsInitUnixCatch"));
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
            // Check for matching version of the application settings and settings for user;
            if (!getOptionsValue("version").equals(currentAppVersion)) {
                updateToNewVersion(options);
                options.load(new FileReader(currentOptionsFilePath));
            }
            initLanguageProperties();
        } catch (IOException e) {
            DataBaseWorker.showMessage(Message.getText("optionsLoadExistingFile"));
        } catch (NullPointerException npe) {
            createNewOptionsFile();
        }
    }

    public static String getCurrentLanguageTitle() {
        String tempTitle;
        switch (getOptionsValue("language")) {
            case "en":
                tempTitle = Message.getText("chooseLangEnglish");
                break;
            case "ru":
                tempTitle = Message.getText("chooseLangRussian");
                break;
            default:
                tempTitle = Message.getText("chooseLangEnglish");
        }
        return tempTitle;
    }

    private static Properties setupOptions(Properties properties) {
        properties.setProperty("currentOS", identifyOS());
        if (identifyOS().equals("Windows")) {
            properties.setProperty("defaultDatabaseFilePath", defaultDatabasePathWindows);
            properties.setProperty("customDatabasePath", defaultDatabasePathWindows);
        } else {
            properties.setProperty("defaultDatabaseFilePath", defaultDatabasePathLinux);
            properties.setProperty("customDatabasePath", defaultDatabasePathLinux);
        }
        properties.setProperty("databaseName", "notes");
        properties.setProperty("language", "en");
        properties.setProperty("country", "US");
        properties.setProperty("version", currentAppVersion);
        properties.setProperty("appLogo", "/img/AppLogo.png");
        return properties;
    }

    private static void createNewOptionsFile() {
        try {
            setupOptions(options);
            output = new FileOutputStream(currentOptionsFilePath);
            options.store(output, "QTDM - Default options file.");
            initLanguageProperties();
        } catch (IOException e) {
            DataBaseWorker.showMessage(Message.getText("optionsCreateNewFileCatch"));
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException ex) {
                    DataBaseWorker.showMessage(Message.getText("optionsCreateNewFileFinally"));
                }
            }
        }
    }

    private static void updateToNewVersion(Properties oldProperties) {
        try {
            Properties tempProp = new Properties();
            tempProp = setupOptions(tempProp);
            for (Map.Entry<Object, Object> oldOptions : oldProperties.entrySet()) {
                System.out.println("KEY: " + oldOptions.getKey() + " | VALUE: " + oldOptions.getValue());
                tempProp.setProperty((String) oldOptions.getKey(), (String) oldOptions.getValue());
            }
            tempProp.setProperty("version", currentAppVersion);
            output = new FileOutputStream(currentOptionsFilePath);
            tempProp.store(output, "QTDM - Default options file.");
        } catch (Exception e) {
            DataBaseWorker.showMessage(Message.getText("optionsCreateNewFileCatch"));
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException ex) {
                    DataBaseWorker.showMessage(Message.getText("optionsCreateNewFileFinally"));
                }
            }
        }
    }

    private static void initLanguageProperties() {
        langPack = ResourceBundle.getBundle("lang/lang", new UTF8Control());
    }

    public static void updateOptionsValue(String key, String value) {
        try {
            options.load(new FileReader(currentOptionsFilePath));
            options.setProperty(key, value);
            output = new FileOutputStream(currentOptionsFilePath);
            options.store(output, "QTDM - Updated options file.");
        } catch (IOException e) {
            DataBaseWorker.showMessage(Message.getText("optionsUpdateValueCatch"));
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException ex) {
                    DataBaseWorker.showMessage(Message.getText("optionsUpdateValueFinally"));
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

    public static void setLang(String lang) {
        updateOptionsValue("language", lang);
    }

    public static void setCountry(String country) {
        updateOptionsValue("country", country);
    }

    public static String getTextByLang(String key) {
        return langPack.getString(key);
    }

    public static String getLang() {
        return lang = options.getProperty("language");
    }

    public static String getCountry() {
        return country = options.getProperty("country");
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
