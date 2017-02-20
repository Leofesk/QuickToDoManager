package com.leofesk.quicktodomanager.view;

import com.leofesk.quicktodomanager.controller.DataBaseWorker;

import javax.swing.*;
import java.awt.*;

public class NoteEditFrame extends JFrame {
    private JButton buttonAddOrEdit;
    private JLabel labelTaskName;
    private JLabel labelDeadline;
    private JLabel labelFormatDeadline;
    private JScrollPane scrollPaneForTextArea;
    private static JTextArea textArea;
    private static JTextField textFieldDeadlineDate;
    private static JTextField textFieldTaskName;
    private static ImageIcon imageAppIcon;
    private String pathToAppLogo = "/img/AppLogo.png";

    public NoteEditFrame() {
        initComponents();
    }

    private void initComponents() {
        scrollPaneForTextArea = new JScrollPane();
        textArea = new JTextArea();
        buttonAddOrEdit = new JButton();
        labelTaskName = new JLabel();
        textFieldDeadlineDate = new JTextField();
        labelDeadline = new JLabel();
        labelFormatDeadline = new JLabel();
        textFieldTaskName = new JTextField();
        imageAppIcon = new ImageIcon(MainFrame.class.getResource(pathToAppLogo));
        setIconImage(imageAppIcon.getImage());

        setTitle(" Edit note");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        textArea.setColumns(20);
        textArea.setRows(5);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setMargin(new Insets(5, 5, 5, 5));
        scrollPaneForTextArea.setViewportView(textArea);

        buttonAddOrEdit.addActionListener(e -> actionButtonEdit());
        buttonAddOrEdit.setText("ОК");

        labelTaskName.setText("Title:");
        textFieldDeadlineDate.setText("11.08.1992");
        labelDeadline.setText("Deadline:");
        labelFormatDeadline.setText("Format: DD.MM.YYYY");
        textFieldTaskName.setText("Name Task");


        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(scrollPaneForTextArea))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(labelTaskName)
                                                                .addGap(10, 10, 10)
                                                                .addComponent(textFieldTaskName))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(labelDeadline)
                                                                .addGap(10, 10, 10)
                                                                .addComponent(textFieldDeadlineDate, GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(10, 10, 10)
                                                                .addComponent(labelFormatDeadline, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                                                                .addGap(10, 10, 10)
                                                                .addComponent(buttonAddOrEdit, GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGap(10, 10, 10))
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(labelTaskName, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textFieldTaskName, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addComponent(scrollPaneForTextArea, GroupLayout.PREFERRED_SIZE, 229, GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelDeadline, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textFieldDeadlineDate, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(buttonAddOrEdit)
                                        .addComponent(labelFormatDeadline, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10))
        );

        pack();
        setLocationRelativeTo(null);
    }

    private void actionButtonEdit() {
        if (DataBaseWorker.isCorrectNote(textFieldTaskName.getText(), textArea.getText(), textFieldDeadlineDate.getText())) {
            DataBaseWorker.editNoteFromTable(textFieldTaskName.getText(), textArea.getText(), textFieldDeadlineDate.getText());
        } else {
            DataBaseWorker.showMessage("Task was not updated. Title, text and deadline can't be empty. [CODE:V_NE_001]");
        }
        dispose();
    }

    public static void setTextArea(String text) {
        textArea.setText(text);
    }

    public static void setTextFieldDeadlineDate(String deadline) {
        textFieldDeadlineDate.setText(deadline);
    }

    public static void setTextFieldTaskName(String title) {
        textFieldTaskName.setText(title);
    }
}
