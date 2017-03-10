package com.leofesk.quicktodomanager.view.notes;

import com.leofesk.quicktodomanager.controller.DataBaseWorker;
import com.leofesk.quicktodomanager.controller.Message;
import com.leofesk.quicktodomanager.model.Options;
import com.leofesk.quicktodomanager.view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
        imageAppIcon = new ImageIcon(AddFrame.class.getResource(Options.getOptionsValue("appLogo")));

        setIconImage(imageAppIcon.getImage());
        setTitle(Message.getText("frameAddTitle"));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        textArea.setColumns(20);
        textArea.setRows(5);
        textArea.setLineWrap(true);
        textArea.setMargin(new Insets(5, 5, 5, 5));
        scrollPaneForTextArea.setViewportView(textArea);

        buttonAdd.addActionListener(e -> actionButtonAdd());
        buttonAdd.setText(Message.getText("buttonAdd"));

        labelTaskName.setText(Message.getText("labelTaskTitle"));
        textFieldDeadlineDate.setText(DataBaseWorker.getNextDay());
        labelDeadline.setText(Message.getText("labelTaskDeadline"));
        labelFormatDeadline.setText(Message.getText("labelDeadlineFormat"));
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

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeFrame();
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    private void actionButtonAdd() {
        if (DataBaseWorker.isCorrectNote(textFieldTaskName.getText(), textArea.getText(), textFieldDeadlineDate.getText())) {
            if(!DataBaseWorker.isDuplicate(textFieldTaskName.getText())) {
                JOptionPane.showMessageDialog(null,Message.getText("errorDuplicateNoteText"), Message.getText("errorDuplicateNoteTitle"), JOptionPane.ERROR_MESSAGE);
            } else {
                DataBaseWorker.addNewNoteFromTable(textFieldTaskName.getText(), textArea.getText(), textFieldDeadlineDate.getText());
                closeFrame();
            }
        } else {
            JOptionPane.showMessageDialog(null, Message.getText("errorButtonAddText"), Message.getText("errorButtonAddTitle"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void closeFrame() {
        dispose();
        textFieldTaskName.setText("");
        textArea.setText("");
        textFieldDeadlineDate.setText(DataBaseWorker.getNextDay());
        MainFrame.setEnabledWindowElement(true);
    }
}
