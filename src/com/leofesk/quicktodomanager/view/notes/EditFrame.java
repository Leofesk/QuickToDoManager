package com.leofesk.quicktodomanager.view.notes;

import com.leofesk.quicktodomanager.controller.DataBaseWorker;
import com.leofesk.quicktodomanager.controller.Message;
import com.leofesk.quicktodomanager.model.Options;
import com.leofesk.quicktodomanager.view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EditFrame extends JFrame {
    private JButton buttonEdit;
    private JLabel labelTaskName;
    private JLabel labelDeadline;
    private JLabel labelFormatDeadline;
    private JScrollPane scrollPaneForTextArea;
    private static JTextArea textArea;
    private static JTextField textFieldDeadlineDate;
    private static JTextField textFieldTaskName;
    private static ImageIcon imageAppIcon;

    public EditFrame() {
        initComponents();
    }

    private void initComponents() {
        scrollPaneForTextArea = new JScrollPane();
        textArea = new JTextArea();
        buttonEdit = new JButton();
        labelTaskName = new JLabel();
        textFieldDeadlineDate = new JTextField();
        labelDeadline = new JLabel();
        labelFormatDeadline = new JLabel();
        textFieldTaskName = new JTextField();
        imageAppIcon = new ImageIcon(EditFrame.class.getResource(Options.getOptionsValue("appLogo")));

        setIconImage(imageAppIcon.getImage());
        setTitle(Message.getText("frameEditTitle"));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        textArea.setColumns(20);
        textArea.setRows(5);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setMargin(new Insets(5, 5, 5, 5));
        scrollPaneForTextArea.setViewportView(textArea);

        buttonEdit.addActionListener(e -> actionButtonEdit());
        buttonEdit.setText(Message.getText("buttonEdit"));

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
                                                                .addComponent(textFieldDeadlineDate, GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(10, 10, 10)
                                                                .addComponent(labelFormatDeadline, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                                                                .addGap(10, 10, 10)
                                                                .addComponent(buttonEdit, GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))))
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
                                        .addComponent(buttonEdit)
                                        .addComponent(labelFormatDeadline, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10))
        );

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MainFrame.setEnabledWindowElement(true);
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    private void actionButtonEdit() {
        if (DataBaseWorker.isEdited()) {
            if (DataBaseWorker.isCorrectNote(textFieldTaskName.getText(), textArea.getText(), textFieldDeadlineDate.getText())) {
                editNote();
            } else {
                JOptionPane.showMessageDialog(null, Message.getText("errorButtonEditText"), Message.getText("errorButtonEditTitle"), JOptionPane.ERROR_MESSAGE);
            }
        }else {
            closeWindow();
        }
    }

    private void editNote() {
        if (DataBaseWorker.checkStatusForCurrentNote()) {
            DataBaseWorker.editNoteFromTable(textFieldTaskName.getText(), textArea.getText(), textFieldDeadlineDate.getText());
        } else {
            if (JOptionPane.showConfirmDialog(null,
                    Message.getText("editNoteConfirmText"),
                    Message.getText("editNoteConfirmTitle"),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                DataBaseWorker.editNoteFromTable(textFieldTaskName.getText(), textArea.getText(), textFieldDeadlineDate.getText());
            }
        }
        closeWindow();
    }

    private void closeWindow() {
        dispose();
        MainFrame.setEnabledWindowElement(true);
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

    public static JTextArea getTextArea() {
        return textArea;
    }

    public static JTextField getTextFieldDeadlineDate() {
        return textFieldDeadlineDate;
    }

    public static JTextField getTextFieldTaskName() {
        return textFieldTaskName;
    }
}