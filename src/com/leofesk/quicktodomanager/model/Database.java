package com.leofesk.quicktodomanager.model;

import com.leofesk.quicktodomanager.controller.DataBaseWorker;
import com.leofesk.quicktodomanager.controller.Message;

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
            DataBaseWorker.showMessage(Message.getText("databaseErrorConnectNotFound"));
        } catch (SQLException e) {
            DataBaseWorker.showMessage(Message.getText("databaseErrorConnectSQL"));
        }
    }

    public static void createTablesInDatabase() {
        try {
            connectToDatabase();
            statement.execute(queriesList.getCreateTable());

        } catch (SQLException e) {
            DataBaseWorker.showMessage(Message.getText("databaseErrorCreateTableCatch"));
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                DataBaseWorker.showMessage(Message.getText("databaseErrorCreateTableFinally"));
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
            pstmt.setString(5, "0");
            pstmt.setInt(6, 0);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            DataBaseWorker.showMessage(Message.getText("databaseErrorAddNoteCatch"));
        } finally {
            try {
                pstmt.close();
                connection.close();
            } catch (SQLException e) {
                DataBaseWorker.showMessage(Message.getText("databaseErrorAddNoteFinally"));
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
            DataBaseWorker.showMessage(Message.getText("databaseErrorUpdateCurrentCatch"));
        } finally {
            try {
                pstmt.close();
                connection.close();
            } catch (SQLException e) {
                DataBaseWorker.showMessage(Message.getText("databaseErrorUpdateCurrentFinally"));
            }
        }
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
            DataBaseWorker.showMessage(Message.getText("databaseErrorGetAllNotesCatch"));
        } finally {
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                DataBaseWorker.showMessage(Message.getText("databaseErrorGetAllNotesFinally"));
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
        } catch (SQLException e) {
            DataBaseWorker.showMessage(Message.getText("databaseErrorGetCurrentNoteCatch"));
        } finally {
            try {
                resultSet.close();
                pstmt.close();
                connection.close();
            } catch (SQLException e) {
                DataBaseWorker.showMessage(Message.getText("databaseErrorGetCurrentNoteFinally"));
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

            DataBaseWorker.showMessage(Message.getText("databaseDeleteCurrentNoteInfo"));
        } catch (SQLException e) {
            DataBaseWorker.showMessage(Message.getText("databaseErrorDeleteCurrentNoteCatch"));
        } finally {
            try {
                pstmt.close();
                connection.close();
            } catch (SQLException e) {
                DataBaseWorker.showMessage(Message.getText("databaseErrorDeleteCurrentNoteFinally"));
            }
        }
    }

    public static String checkStatus(String status) {
        if (status.equals("0")) {
            status = Message.getText("statusInWork");
        }
        if (status.equals("1")) {
            status = Message.getText("statusDone");
        }
        return status;
    }

    private static String convertStatus(String status) {
        if (status.equals(Message.getText("statusDone"))) {
            status = "1";
        }
        if (status.equals(Message.getText("statusInWork"))) {
            status = "0";
        }
        return status;
    }

    public static void createNewDatabase(String databaseName) {
        if (!databaseName.equals("") || databaseName != null) {
            Options.updateOptionsValue("customDatabasePath", Options.getOptionsValue("defaultDatabaseFilePath"));
            Options.updateOptionsValue("databaseName", databaseName);
            createTablesInDatabase();
        } else {
            DataBaseWorker.showMessage(Message.getText("databaseErrorCreateNewDB"));
        }
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("dd.MM.yyyy").format(new Date());
    }

    public static ArrayList<Note> getNotes() {
        return notes;
    }
}