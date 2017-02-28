package com.leofesk.quicktodomanager.view.help;

import com.leofesk.quicktodomanager.controller.DataBaseWorker;
import com.leofesk.quicktodomanager.controller.Message;
import com.leofesk.quicktodomanager.model.Options;
import com.leofesk.quicktodomanager.view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AboutFrame extends JFrame {
    private JLabel labelForAboutVersion;
    private JScrollPane jScrollPane1;
    private static JTextArea textAreaForAboutText;
    private static ImageIcon imageAppIcon;

    public AboutFrame() {
        initComponents();
    }

    private void initComponents() {
        labelForAboutVersion = new JLabel();
        jScrollPane1 = new JScrollPane();
        textAreaForAboutText = new JTextArea();
        imageAppIcon = new ImageIcon(AboutFrame.class.getResource(Options.getOptionsValue("appLogo")));
        setIconImage(imageAppIcon.getImage());

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(Message.getText("frameAboutTitle"));
        setResizable(false);

        labelForAboutVersion.setHorizontalAlignment(SwingConstants.CENTER);
        labelForAboutVersion.setText(getTitleForLabel());

        textAreaForAboutText.setEditable(false);
        textAreaForAboutText.setColumns(20);
        textAreaForAboutText.setRows(5);
        textAreaForAboutText.setMargin(new Insets(5, 5, 5, 5));
        textAreaForAboutText.setWrapStyleWord(true);
        textAreaForAboutText.setLineWrap(true);
        jScrollPane1.setViewportView(textAreaForAboutText);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
                                        .addComponent(labelForAboutVersion, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(4, 4, 4))
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(labelForAboutVersion, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
                                .addGap(10, 10, 10))
        );

        pack();
        setLocationRelativeTo(null);

        loadAboutText();
    }

    private String getTitleForLabel() {
        return Message.getText("aboutBlockTitle")+" " + Options.getOptionsValue("version");
    }

    private void loadAboutText() {
        DataBaseWorker.openAbout();
        textAreaForAboutText.setCaretPosition(0);
    }

    public static void setTextAreaForAboutText(String text) {
        textAreaForAboutText.append(text + "\n");
    }
}
