package com.leofesk.quicktodomanager.model;

import com.leofesk.quicktodomanager.controller.DataBaseWorker;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class Database {
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;
    private static ArrayList<Note> notes = new ArrayList<>();

    private static void connectToDatabase() {
        connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + Options.getOptionsValue("customDatabasePath")
                    + Options.getOptionsValue("databaseName") + ".qtdm");
            statement = connection.createStatement();
        } catch (ClassNotFoundException e) {
            DataBaseWorker.showMessage("Can't find database. [CODE:M_DB_001]");
        } catch (SQLException e) {
            DataBaseWorker.showMessage("Can't connect to database. [CODE:M_DB_002]");
        }
    }

    public static void createTablesInDatabase() {
        try {
            connectToDatabase();
            statement.execute("CREATE TABLE if NOT EXISTS `qtdm_notes` " +
                    "(`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`title` VARCHAR(255) NOT NULL , " +
                    "`text` TEXT CHARACTER NOT NULL , " +
                    "`deadline` DATE NOT NULL , " +
                    "`created` DATE NOT NULL , " +
                    "`end` DATE NOT NULL , " +
                    "`status` INT UNSIGNED NOT NULL)");
        } catch (SQLException e) {
            DataBaseWorker.showMessage("Can't create tables in database. [CODE:M_DB_003]");
        }
    }

    public static void addNoteToDatabase(String title, String text, String deadline) {
        try {
            if (connection.isClosed()) {
                connectToDatabase();
            }
            statement.execute("INSERT INTO `qtdm_notes`(`title`, `text`, `deadline`, `created`, `end`, `status`) " +
                    "VALUES ('" + title + "','" + text + "','" + deadline + "','" + getCurrentDate() + "','Not done yet',0)");
        } catch (SQLException e) {
            DataBaseWorker.showMessage("Can't add task to database. [CODE:M_DB_004]");
        }
    }

    public static void updateCurrentNoteFromDatabase(int id, String title, String text, String deadline, String createdTime, String endTime, String status) {
        try {
            if (connection.isClosed()) {
                connectToDatabase();
            }
            statement.execute("UPDATE `qtdm_notes` SET `title`='" + title + "'," +
                    "`text`='" + text + "'," +
                    "`deadline`='" + deadline + "'," +
                    "`created`='" + createdTime + "'," +
                    "`end`='" + endTime + "'," +
                    "`status`='" + status + "'" +
                    "WHERE `id` = '" + id + "'");
            connection.close();
        } catch (SQLException e) {
            DataBaseWorker.showMessage("Can't update task. [CODE:M_DB_005]");
        }
    }

    public static void getAllNotesFromDatabase() {
        try {
            connectToDatabase();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `qtdm_notes` ORDER BY `status`");
            notes.clear(); // Reset list before adding data from another query.
            while (resultSet.next()) {
                notes.add(new Note(resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("text"),
                        resultSet.getString("deadline"),
                        resultSet.getString("created"),
                        resultSet.getString("end"),
                        resultSet.getString("status")));
            }
        } catch (SQLException e) {
            DataBaseWorker.showMessage("Can't get data about all tasks from database. [CODE:M_DB_006]");
        }
    }

    public static Note getCurrentNoteFromDatabase(int id) {
        Note currentNote = null;
        try {
            if (connection.isClosed()) {
                connectToDatabase();
            }
            resultSet = statement.executeQuery("SELECT * FROM `qtdm_notes` WHERE `id` = '" + id + "'");
            currentNote = new Note(resultSet.getInt("id"),
                    resultSet.getString("title"),
                    resultSet.getString("text"),
                    resultSet.getString("deadline"),
                    resultSet.getString("created"),
                    resultSet.getString("end"),
                    resultSet.getString("status"));
            currentNote.setStatus(checkStatus(currentNote.getStatus()));
        } catch (SQLException e) {
            DataBaseWorker.showMessage("Can't get data about task from database. [CODE:M_DB_007]");
        }
        return currentNote;
    }

    public static void deleteCurrentNoteFromDatabase(int id) {
        try {
            if (connection.isClosed()) {
                connectToDatabase();
            }
            statement.execute("DELETE from `qtdm_notes` where `id` = '" + id + "'");
            DataBaseWorker.showMessage("Task was deleted successfully.");
        } catch (SQLException e) {
            DataBaseWorker.showMessage("Can't delete task. [CODE:M_DB_008]");
        }
    }

    public static String checkStatus(String status) {
        if (status.equals("0")) {
            status = "In work";
        } else {
            status = "Done";
        }
        return status;
    }

    public static void createNewDatabase(String databaseName) {
        if (!databaseName.equals("") || databaseName != null) {
            Options.updateOptionsValue("customDatabasePath", Options.getOptionsValue("defaultDatabaseFilePath"));
            Options.updateOptionsValue("databaseName", databaseName);
            createTablesInDatabase();
        } else {
            DataBaseWorker.showMessage("Can't create database. [CODE:M_DB_009]");
        }
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("dd.MM.yyyy").format(new Date());
    }

    public static ArrayList<Note> getNotes() {
        return notes;
    }
}
