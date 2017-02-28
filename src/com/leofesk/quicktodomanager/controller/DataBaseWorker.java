package com.leofesk.quicktodomanager.controller;

import com.leofesk.quicktodomanager.model.Database;
import com.leofesk.quicktodomanager.model.Note;
import com.leofesk.quicktodomanager.model.Options;
import com.leofesk.quicktodomanager.view.help.AboutFrame;
import com.leofesk.quicktodomanager.view.MainFrame;
import com.leofesk.quicktodomanager.view.notes.EditFrame;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.xml.crypto.Data;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.leofesk.quicktodomanager.model.Database.checkStatus;

/**
 * Basic controller class, all function blocks program was here.
 * In future this class be splitted.
 */

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
            MainFrame.changeEnabledForToolbarAddButton(true);
            showMessage(Message.getText("database") + " [" + databaseName + "] " + Message.getText("dbwLoadDataFromDBSuccess"));
        } catch (Exception e) {
            MainFrame.changeEnabledForToolbarAddButton(false);
            showMessage(Message.getText("errorDBWLoadDataFromDB"));
        }
    }

    public static void chooseDatabase() {
        JFileChooser chooseDB = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter(Message.getText("dbwChooseDBDesc") + " | .qtdm", "qtdm");
        chooseDB.setAcceptAllFileFilterUsed(false);
        chooseDB.setFileFilter(filter);

        int resultForFileChooser = chooseDB.showDialog(null, Message.getText("dbwButtonOpenDB"));
        if (resultForFileChooser == JFileChooser.APPROVE_OPTION) {
            String tempSelectedFileName = chooseDB.getSelectedFile().getName();
            tempSelectedFileName = tempSelectedFileName.replaceFirst("[.][^.]+$", "");
            String tempSelectedFilePath = chooseDB.getSelectedFile().getAbsolutePath();
            tempSelectedFilePath = tempSelectedFilePath.substring(0, tempSelectedFilePath.lastIndexOf(File.separator));

            // Checking which OS used and add correct for current OS path.

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
            if (note.getEndTime().equals("0")) {
                note.setEndTime(Message.getText("databaseEndField"));
            }
            model.addRow(new Object[]{note.getId(), note.getTitle(), note.getDeadline(), note.getStatus(), note.getCreatedTime(), note.getEndTime()});
        }
    }

    public static void openAbout() {
        try {
            String tempPath = "about_en_US"; // This is default "About";
            if (Options.getOptionsValue("language").equals("ru")) {
                tempPath = "about_ru_RU";
            }
            InputStream inputStream = DataBaseWorker.class.getResourceAsStream("/help/about/" + tempPath + ".txt");
            InputStreamReader inputReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader reader = new BufferedReader(inputReader);

            String temp;
            while ((temp = reader.readLine()) != null) {
                AboutFrame.setTextAreaForAboutText(temp);
            }
            reader.close();
        } catch (IOException ioe) {
            showMessage(Message.getText("errorDBWOpenAboutCatch"));
        }
    }

    public static void createDatabase(String databaseName) {
        if (isAlreadyExistDB(databaseName)) {
            if (JOptionPane.showConfirmDialog(null,
                    Message.getText("database") + " [" + databaseName + "] " +
                            Message.getText("dbwCreateDBMessage") + "\n" +
                            Message.getText("dbwCreateDBMessageAdditional"),
                    Message.getText("dbwCreateDBTitle"),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                Options.updateOptionsValue("databaseName", databaseName);
                loadDataFromDatabaseToTable(databaseName);
            }
        } else {
            Database.createNewDatabase(databaseName);
            addNewNoteFromTable(Message.getText("dbwNewNoteTitle"),
                    Message.getText("dbwNewNoteText"),
                    getNextDay());
            updateTableData();
            showMessage(Message.getText("database") + " [" + databaseName + "] " + Message.getText("dbwCreateSuccess"));
        }
    }

    private static boolean isAlreadyExistDB(String databaseName) {
        String fullPath = Options.getOptionsValue("customDatabasePath") + databaseName + ".qtdm";
        File file = new File(fullPath);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public static void showSelectedNoteInfo(int id) {
        currentNoteID = id;
        note = Database.getCurrentNoteFromDatabase(id);
        MainFrame.setLabelNoteNameForViewCurrentNote(note.getTitle());
        MainFrame.setLabelCreatedDate(note.getCreatedTime());
        MainFrame.setLabelDeadlineDate(note.getDeadline());
        if (note.getEndTime().equals("0")) {
            note.setEndTime(Message.getText("databaseEndField"));
        }
        MainFrame.setLabelEndDate(note.getEndTime());
        MainFrame.setTextAreaForViewCurrentNote(note.getText());
        MainFrame.setComboBoxSelectedItem();
    }

    public static void changeStatusToCurrentNote(String value) {
        if (value.equals(Message.getText("statusDone"))) {
            Database.updateCurrentNoteFromDatabase(currentNoteID, note.getTitle(),
                    note.getText(), note.getDeadline(),
                    note.getCreatedTime(), Database.getCurrentDate(), "1");
        } else {
            Database.updateCurrentNoteFromDatabase(currentNoteID, note.getTitle(),
                    note.getText(), note.getDeadline(),
                    note.getCreatedTime(), "0", "0");
        }
        updateTableData();
        showMessage(Message.getText("dbwChangeStatusNoteSuccess"));
    }

    public static void showMessage(String message) {
        MainFrame.setLabelForInfoAndMessages(Message.getText("labelInfo") + " " + message);
    }

    public static void addNewNoteFromTable(String title, String text, String deadline) {
        if (isDate(deadline)) {
            Database.addNoteToDatabase(title, text, deadline);
            updateTableData();
            DataBaseWorker.showMessage(Message.getText("task") + " [" + title + "] " + Message.getText("dbwCreateSuccess"));
        } else {
            MainFrame.setLabelForInfoAndMessages(Message.getText("dbwNotCorrectFormat"));
        }
    }

    public static void editNoteFromTable(String title, String text, String deadline) {
        if (isDate(deadline)) {
            Database.updateCurrentNoteFromDatabase(currentNoteID, title, text,
                    deadline, note.getCreatedTime(),
                    "0", "0");
            updateTableData();
            showSelectedNoteInfo(currentNoteID);
            DataBaseWorker.showMessage(Message.getText("task") + " [" + title + "] " + Message.getText("dbwUpdatedSuccess"));
        } else {
            MainFrame.setLabelForInfoAndMessages(Message.getText("dbwNotCorrectFormat"));
        }
    }

    // Checking correct date input.
    private static boolean isDate(String deadline) {
        String s[];
        int day;
        int month;
        int year;

        try {
            s = deadline.split("\\.");
            day = Integer.parseInt(s[0]);
            month = Integer.parseInt(s[1]);
            year = Integer.parseInt(s[2]);
        } catch (NumberFormatException nfe) {
            return false;
        }

        if (!(day <= 31 && day > 0) || !(month <= 12 && month >= 1) || !(year <= 3000 && year >= 2000)) {
            return false;
        }
        return true;
    }

    public static void addNoteToEditFrame() {
        try {
            EditFrame.setTextFieldTaskName(note.getTitle());
            EditFrame.setTextArea(note.getText());
            EditFrame.setTextFieldDeadlineDate(note.getDeadline());
        } catch (Exception e) {
            showMessage(Message.getText("errorDBWAddNoteToFrame"));
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

    public static boolean isDuplicate(String text) {
        Database.getAllNotesFromDatabase();
        for (Note note : Database.getNotes()) {
            if (note.getTitle().equals(text)) {
                return false;
            }
        }
        return true;
    }

    public static int getCurrentNoteID() {
        return currentNoteID;
    }

    public static String getNextDay() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        date = c.getTime();
        DateFormat dataFormat = new SimpleDateFormat("dd.MM.yyyy");
        String nextDay = dataFormat.format(date);
        return nextDay;
    }

    // Restore default value to view block, if not active task.
    public static void clearViewBlock() {
        MainFrame.setLabelNoteNameForViewCurrentNote(Message.getText("clearViewNoteTitle"));
        MainFrame.setLabelCreatedDate(Message.getText("clearViewNoteCreated"));
        MainFrame.setLabelDeadlineDate(Message.getText("clearViewNoteDeadline"));
        MainFrame.setLabelEndDate(Message.getText("clearViewNoteEnd"));
        MainFrame.setTextAreaForViewCurrentNote(Message.getText("clearViewNoteText"));
    }

    public static void changeLanguage(String tempChoosingLang) {
        if (tempChoosingLang.equals(Message.getText("chooseLangEnglish"))) {
            Options.setLang("en");
            Options.setCountry("US");
        }
        if (tempChoosingLang.equals(Message.getText("chooseLangRussian"))) {
            Options.setLang("ru");
            Options.setCountry("RU");
        }
    }

    public static String getNoteStatus() {
        return note.getStatus();
    }

    public static boolean checkStatusForCurrentNote() {
        System.out.println(note.getEndTime());
        if (!isDate(note.getEndTime())) {
            return true;
        } else {
            return false;
        }
    }
}
