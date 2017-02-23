package com.leofesk.quicktodomanager.model;

import com.leofesk.quicktodomanager.controller.DataBaseWorker;
import com.leofesk.quicktodomanager.controller.Message;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class Options {
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
    private static Locale locale;
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
            initLanguageProperties();
        } catch (IOException e) {
            DataBaseWorker.showMessage(Message.getText("optionsLoadExistingFile"));
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
            options.setProperty("language", "en");
            options.setProperty("country", "US");
            options.setProperty("version", "1.1.6");
            options.setProperty("appLogo", "/img/AppLogo.png");
            output = new FileOutputStream(currentOptionsFilePath);
            options.store(output, Message.getText("optionsConfigStoreDefaultComment"));
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

    private static void initLanguageProperties() {
        lang = options.getProperty("language");
        country = options.getProperty("country");
        locale = new Locale(lang, country);
        langPack = ResourceBundle.getBundle("lang/lang", locale);
    }

    public static void updateOptionsValue(String key, String value) {
        try {
            options.load(new FileReader(currentOptionsFilePath));
            options.setProperty(key, value);
            output = new FileOutputStream(currentOptionsFilePath);
            options.store(output, Message.getText("optionsConfigStoreUpdatedComment"));
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
