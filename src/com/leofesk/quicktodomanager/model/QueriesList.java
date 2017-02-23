package com.leofesk.quicktodomanager.model;

public class QueriesList {

    public QueriesList() {
    }

    private String createTable = "CREATE TABLE IF NOT EXISTS `qtdm_notes` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` VARCHAR(255) NOT NULL , `text` TEXT CHARACTER NOT NULL , `deadline` DATE NOT NULL , `created` DATE NOT NULL , `end` DATE NOT NULL , `status` INT UNSIGNED NOT NULL)";
    private String addNote = "INSERT INTO qtdm_notes(title, text, deadline, created, end, status) VALUES(?,?,?,?,?,?)";
    private String updateCurrentNote = "UPDATE `qtdm_notes` SET title = ?, text = ?, deadline = ?, created = ?, end = ?, status = ? WHERE id = ?";
    private String deleteCurrentNote = "DELETE from `qtdm_notes` WHERE id = ?";
    private String allNotes = "SELECT * FROM `qtdm_notes` ORDER BY `status`";
    private String currentNote = "SELECT * FROM `qtdm_notes` WHERE id = ?";

    public String getCreateTable() {

        return createTable;
    }

    public String getAddNote() {
        return addNote;
    }

    public String getUpdateCurrentNote() {
        return updateCurrentNote;
    }

    public String getCurrentDeleteNote() {
        return deleteCurrentNote;
    }

    public String getAllNotes() {
        return allNotes;
    }

    public String getCurrentNote() {
        return currentNote;
    }
}
