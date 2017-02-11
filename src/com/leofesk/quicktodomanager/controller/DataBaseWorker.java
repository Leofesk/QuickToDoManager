package com.leofesk.quicktodomanager.controller;

import com.leofesk.quicktodomanager.model.Database;
import com.leofesk.quicktodomanager.model.Note;
import com.leofesk.quicktodomanager.model.Options;
import com.leofesk.quicktodomanager.view.AboutFrame;
import com.leofesk.quicktodomanager.view.MainFrame;
import com.leofesk.quicktodomanager.view.NoteEditFrame;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.io.*;

import static com.leofesk.quicktodomanager.model.Database.checkStatus;

public class DataBaseWorker {
    private static int currentNoteID;
    private static Note note;

    private static void updateTableData() {
        Database.getAllNotesFromDatabase();
        addDataFromDatabaseToTable();
    }

    private static void loadDataFromDatabaseToTable(String databaseName) {
        try {
            Database.createTablesInDatabase();
            Database.getAllNotesFromDatabase();
            addDataFromDatabaseToTable();
            showMessage("Database [" + databaseName + "] opened successfully.");
        } catch (Exception e) {
            showMessage("Can't open selected database. [CODE:C_DBW_001]");
        }
    }

    public static void chooseDatabase() {
        JFileChooser chooseDB = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter("QTDM database files | .qtdm", "qtdm");
        chooseDB.setAcceptAllFileFilterUsed(false);
        chooseDB.setFileFilter(filter);

        int resultForFileChooser = chooseDB.showDialog(null, "Open database");
        if (resultForFileChooser == JFileChooser.APPROVE_OPTION) {
            String tempSelectedFileName = chooseDB.getSelectedFile().getName();
            tempSelectedFileName = tempSelectedFileName.replaceFirst("[.][^.]+$", "");
            String tempSelectedFilePath = chooseDB.getSelectedFile().getAbsolutePath();
            tempSelectedFilePath = tempSelectedFilePath.substring(0, tempSelectedFilePath.lastIndexOf(File.separator));

            if (Options.getOptionsValue("currentOS").equals("Windows")) {
                tempSelectedFilePath = tempSelectedFilePath + "\\";
            }

            if (Options.getOptionsValue("currentOS").equals("Linux")) {
                tempSelectedFilePath = tempSelectedFilePath + "/";
            }

            Options.updateOptionsValue("databaseName", tempSelectedFileName);
            Options.updateOptionsValue("customDatabasePath", tempSelectedFilePath);

            loadDataFromDatabaseToTable(tempSelectedFileName);
        }
    }

    public static void addDataFromDatabaseToTable() {
        DefaultTableModel model = (DefaultTableModel) MainFrame.tableNotes.getModel();
        model.setRowCount(0);
        for (Note note : Database.getNotes()) {
            note.setStatus(checkStatus(note.getStatus()));
            model.addRow(new Object[]{note.getId(), note.getTitle(), note.getDeadline(), note.getStatus(), note.getCreatedTime(), note.getEndTime()});
        }
    }

    public static void openAbout() {
        try {
            InputStream inputStream = DataBaseWorker.class.getResourceAsStream("/help/about.txt");
            InputStreamReader inputReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputReader);

            String temp;
            while ((temp = reader.readLine()) != null) {
                AboutFrame.setTextAreaForAboutText(temp);
            }
            reader.close();
        } catch (IOException ioe) {
            showMessage("Can't show about information. [CODE:C_DBW_002]");
        }
    }

    public static void createDatabase(String databaseName) {
        Database.createNewDatabase(databaseName);
        addNewNoteFromTable("Task sample.",
                "This is first task in your database. " +
                        "You can add, edit and delete task by clicking in table row. " +
                        "Change status In work/Done",
                Database.getCurrentDate());
        updateTableData();
        showMessage("New database [" + databaseName + "] created successfully.");
    }

    public static void showSelectedNoteInfo(int id) {
        currentNoteID = id;
        note = Database.getCurrentNoteFromDatabase(id);
        MainFrame.setLabelNoteNameForViewCurrentNote(note.getTitle());
        MainFrame.setLabelCreatedDate(note.getCreatedTime());
        MainFrame.setLabelDeadlineDate(note.getDeadline());
        MainFrame.setLabelEndDate(note.getEndTime());
        MainFrame.setTextAreaForViewCurrentNote(note.getText());
    }

    public static void changeStatusToCurrentNote(String value) {
        if (value.equals("Done")) {
            Database.updateCurrentNoteFromDatabase(currentNoteID, note.getTitle(),
                    note.getText(), note.getDeadline(),
                    note.getCreatedTime(), Database.getCurrentDate(), "1");
        } else {
            Database.updateCurrentNoteFromDatabase(currentNoteID, note.getTitle(),
                    note.getText(), note.getDeadline(),
                    note.getCreatedTime(), "Not done yet", "0");
        }
        updateTableData();
        showMessage("Status for current task was successfully updated.");
    }

    public static void showMessage(String message) {
        MainFrame.setLabelForInfoAndMessages("Info: " + message);
    }

    public static void addNewNoteFromTable(String title, String text, String deadline) {
        Database.addNoteToDatabase(title, text, deadline);
        updateTableData();
    }

    public static void editNoteFromTable(String title, String text, String deadline) {
        Database.updateCurrentNoteFromDatabase(currentNoteID, title, text,
                deadline, note.getCreatedTime(),
                note.getEndTime(), note.getStatus());
        updateTableData();
    }

    public static void addNoteToEditFrame() {
        try {
            NoteEditFrame.setTextFieldTaskName(note.getTitle());
            NoteEditFrame.setTextArea(note.getText());
            NoteEditFrame.setTextFieldDeadlineDate(note.getDeadline());
        } catch (Exception e) {
            showMessage("Can't add task to database. [CODE:C_DBW_003]");
        }
    }

    public static void deleteSelectedNote() {
        Database.deleteCurrentNoteFromDatabase(currentNoteID);
        updateTableData();
    }

    public static boolean isCorrectNote(String text, String textAreaText, String deadlineDateText) {
        if (text != null && !text.trim().isEmpty() &&
                textAreaText != null && !textAreaText.trim().isEmpty() &&
                deadlineDateText != null && !deadlineDateText.trim().isEmpty()) {
        } else {
            return false;
        }
        return true;
    }

    public static int getCurrentNoteID() {
        return currentNoteID;
    }

    public static void clearViewBlock() {
        MainFrame.setLabelNoteNameForViewCurrentNote("Choose task to view details.");
        MainFrame.setLabelCreatedDate("Select task");
        MainFrame.setLabelDeadlineDate("Select task");
        MainFrame.setLabelEndDate("Select task");
        MainFrame.setTextAreaForViewCurrentNote("Not chosen task to view.");
    }
}
