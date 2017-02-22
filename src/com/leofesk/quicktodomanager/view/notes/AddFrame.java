package com.leofesk.quicktodomanager.view.notes;

import com.leofesk.quicktodomanager.controller.DataBaseWorker;
import com.leofesk.quicktodomanager.view.MainFrame;

import javax.swing.*;
import java.awt.*;

public class AddFrame extends JFrame {
    private JButton buttonAdd;
    private JLabel labelTaskName;
    private JLabel labelDeadline;
    private JLabel labelFormatDeadline;
    private JScrollPane scrollPaneForTextArea;
    private JTextArea textArea;
    private JTextField textFieldDeadlineDate;
    private JTextField textFieldTaskName;
    private static ImageIcon imageAppIcon;
    private String pathToAppLogo = "/img/AppLogo.png";

    public AddFrame() {
        initComponents();
    }

    private void initComponents() {
        scrollPaneForTextArea = new JScrollPane();
        textArea = new JTextArea();
        buttonAdd = new JButton();
        labelTaskName = new JLabel();
        textFieldDeadlineDate = new JTextField();
        labelDeadline = new JLabel();
        labelFormatDeadline = new JLabel();
        textFieldTaskName = new JTextField();
        imageAppIcon = new ImageIcon(MainFrame.class.getResource(pathToAppLogo));
        setIconImage(imageAppIcon.getImage());

        setTitle(" Add new note");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        textArea.setColumns(20);
        textArea.setRows(5);
        textArea.setLineWrap(true);
        textArea.setMargin(new Insets(5, 5, 5, 5));
        scrollPaneForTextArea.setViewportView(textArea);

        buttonAdd.addActionListener(e -> actionButtonAdd());
        buttonAdd.setText("ОК");

        labelTaskName.setText("Title:");
        textFieldDeadlineDate.setText("11.08.1992");
        labelDeadline.setText("Deadline:");
        labelFormatDeadline.setText("Format: DD.MM.YYYY");
        textFieldTaskName.setText("");

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
                                                                .addComponent(textFieldDeadlineDate, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(10, 10, 10)
                                                                .addComponent(labelFormatDeadline, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                                                                .addGap(10, 10, 10)
                                                                .addComponent(buttonAdd, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)))))
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
                                        .addComponent(buttonAdd)
                                        .addComponent(labelFormatDeadline, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10))
        );

        pack();
        setLocationRelativeTo(null);
    }

    private void actionButtonAdd() {
        if (DataBaseWorker.isCorrectNote(textFieldTaskName.getText(), textArea.getText(), textFieldDeadlineDate.getText())) {
            DataBaseWorker.addNewNoteFromTable(textFieldTaskName.getText(), textArea.getText(), textFieldDeadlineDate.getText());
        } else {
            DataBaseWorker.showMessage("New task was not created. Title, text and deadline can't be empty.");
        }

        textFieldTaskName.setText("");
        textArea.setText("");
        textFieldDeadlineDate.setText("01.01.2000");

        dispose();
    }
}
