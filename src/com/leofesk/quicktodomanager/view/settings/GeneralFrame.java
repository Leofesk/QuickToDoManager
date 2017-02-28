package com.leofesk.quicktodomanager.view.settings;

import com.leofesk.quicktodomanager.controller.DataBaseWorker;
import com.leofesk.quicktodomanager.controller.Message;
import com.leofesk.quicktodomanager.model.Options;
import com.leofesk.quicktodomanager.view.MainFrame;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GeneralFrame extends JFrame {

    private JButton buttonSave;
    private JButton buttonCancel;
    private JComboBox<String> comboBoxListOfLang;
    private JLabel labelLanguage;
    private JPanel panel;
    private static ImageIcon imageAppIcon;
    private static String tempChoosingLang;

    public GeneralFrame() {
        initComponents();
    }

    private void initComponents() {
        tempChoosingLang = Message.getText("chooseLangEnglish");
        panel = new JPanel();
        labelLanguage = new JLabel();
        comboBoxListOfLang = new JComboBox<>();
        buttonSave = new JButton();
        buttonCancel = new JButton();
        imageAppIcon = new ImageIcon(GeneralFrame.class.getResource(Options.getOptionsValue("appLogo")));

        setIconImage(imageAppIcon.getImage());
        setTitle(Message.getText("frameGeneralTitle"));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        panel.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        labelLanguage.setText(Message.getText("labelChooseLang"));

        comboBoxListOfLang.setModel(new DefaultComboBoxModel<>(new String[]{
                Message.getText("chooseLangEnglish"),
                Message.getText("chooseLangRussian")}));
        comboBoxListOfLang.setSelectedItem(Options.getCurrentLanguageTitle());

        GroupLayout jPanel1Layout = new GroupLayout(panel);
        panel.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(labelLanguage)
                                .addGap(10, 10, 10)
                                .addComponent(comboBoxListOfLang, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10))
        );

        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(comboBoxListOfLang)
                                        .addComponent(labelLanguage, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(10, 10, 10))
        );

        buttonSave.setText(Message.getText("buttonSave"));

        buttonCancel.setText(Message.getText("buttonCancel"));

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addGap(10, 10, 10))
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(buttonCancel)
                                                .addGap(10, 10, 10)
                                                .addComponent(buttonSave)
                                                .addGap(10, 10, 10))))
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(buttonSave)
                                        .addComponent(buttonCancel))
                                .addGap(10, 10, 10))
        );

        buttonSave.addActionListener(e -> actionButtonSave());
        buttonCancel.addActionListener(e -> actionButtonCancel());
        comboBoxListOfLang.addActionListener(evt -> actionComboBoxListOfLang());

        pack();
        setLocationRelativeTo(null);
    }

    private void actionButtonCancel() {
        dispose();
    }

    private void actionButtonSave() {
        try {
            DataBaseWorker.changeLanguage(tempChoosingLang);
            JOptionPane.showMessageDialog(null,
                    Message.getText("settingsInfoMessageSuccess"),
                    Message.getText("settingsInfoTitleSuccess"),
                    JOptionPane.INFORMATION_MESSAGE);
            actionButtonCancel();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    Message.getText("settingsInfoMessageWarning"),
                    Message.getText("settingsInfoTitleWarning"),
                    JOptionPane.INFORMATION_MESSAGE);
            actionButtonCancel();
        }
    }

    private void actionComboBoxListOfLang() {
        tempChoosingLang = comboBoxListOfLang.getSelectedItem().toString();
    }
}
