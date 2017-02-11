package com.leofesk.quicktodomanager;

import com.leofesk.quicktodomanager.model.Options;
import com.leofesk.quicktodomanager.view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StartClass {
    public static void main(String[] args) throws InterruptedException {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("GTK+".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        EventQueue.invokeLater(() -> new MainFrame().setVisible(true));

        Thread.sleep(2000); // It is necessary to delay the loading of the configuration file.
        Options.initOptionsFile();
    }
}