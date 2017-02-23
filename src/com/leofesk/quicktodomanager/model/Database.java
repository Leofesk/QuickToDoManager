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
    private static PreparedStatement pstmt;
    private static ArrayList<Note> notes = new ArrayList<>();
    private static QueriesList queriesList = new QueriesList();

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
            statement.execute(queriesList.getCreateTable());

        } catch (SQLException e) {
            DataBaseWorker.showMessage("Can't create tables in database. [CODE:M_DB_003]");
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                DataBaseWorker.showMessage("Unexpected error: [CODE:M_DB_014]");
            }
        }
    }

    public static void addNoteToDatabase(String title, String text, String deadline) {
        try {
            if (connection.isClosed()) {
                connectToDatabase();
            }
            pstmt = connection.prepareStatement(queriesList.getAddNote());

            pstmt.setString(1, title);
            pstmt.setString(2, text);
            pstmt.setString(3, deadline);
            pstmt.setString(4, getCurrentDate());
            pstmt.setString(5, "Not done yet");
            pstmt.setInt(6, 0);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            DataBaseWorker.showMessage("Can't add task to database. [CODE:M_DB_004]");
        } finally {
            try {
                pstmt.close();
                connection.close();
            } catch (SQLException e) {
                DataBaseWorker.showMessage("Unexpected error: [CODE:M_DB_013]");
            }
        }
    }

    public static void updateCurrentNoteFromDatabase(int id, String title, String text, String deadline, String createdTime, String endTime, String status) {
        try {
            if (connection.isClosed()) {
                connectToDatabase();
            }
            pstmt = connection.prepareStatement(queriesList.getUpdateCurrentNote());

            pstmt.setString(1, title);
            pstmt.setString(2, text);
            pstmt.setString(3, deadline);
            pstmt.setString(4, createdTime);
            pstmt.setString(5, endTime);
            pstmt.setString(6, convertStatus(status));
            pstmt.setInt(7, id);

            pstmt.executeUpdate();


        } catch (SQLException e) {
            DataBaseWorker.showMessage("Can't update task. [CODE:M_DB_005]");
        } finally {
            try {
                pstmt.close();
                connection.close();
            } catch (SQLException e) {
                DataBaseWorker.showMessage("Unexpected error: [CODE:M_DB_012]");
            }
        }
    }

    private static String convertStatus(String status) {
        if (status.equals("Done")) {
            status = "1";
        }

        if (status.equals("In work")) {
            status = "0";
        }

        return status;
    }

    public static void getAllNotesFromDatabase() {
        try {
            connectToDatabase();
            resultSet = statement.executeQuery(queriesList.getAllNotes());
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
        } finally {
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                DataBaseWorker.showMessage("Unexpected error: [CODE:M_DB_011]");
            }
        }
    }

    public static Note getCurrentNoteFromDatabase(int id) {
        Note currentNote = null;
        try {
            if (connection.isClosed()) {
                connectToDatabase();
            }
            pstmt = connection.prepareStatement(queriesList.getCurrentNote());

            pstmt.setInt(1, id);

            resultSet = pstmt.getResultSet();

            resultSet = pstmt.executeQuery();
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
        } finally {
            try {
                resultSet.close();
                pstmt.close();
                connection.close();
            } catch (SQLException e) {
                DataBaseWorker.showMessage("Unexpected error: [CODE:M_DB_010]");
            }
        }
        return currentNote;
    }

    public static void deleteCurrentNoteFromDatabase(int id) {
        try {
            if (connection.isClosed()) {
                connectToDatabase();
            }
            pstmt = connection.prepareStatement(queriesList.getCurrentDeleteNote());

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            DataBaseWorker.showMessage("Task was deleted successfully.");
        } catch (SQLException e) {
            DataBaseWorker.showMessage("Can't delete task. [CODE:M_DB_008]");
        } finally {
            try {
                pstmt.close();
                connection.close();
            } catch (SQLException e) {
                DataBaseWorker.showMessage("Unexpected error: [CODE:M_DB_015]");
            }
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