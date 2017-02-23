package com.leofesk.quicktodomanager.view;

import com.leofesk.quicktodomanager.controller.DataBaseWorker;
import com.leofesk.quicktodomanager.controller.Message;
import com.leofesk.quicktodomanager.model.Options;
import com.leofesk.quicktodomanager.view.help.AboutFrame;
import com.leofesk.quicktodomanager.view.notes.AddFrame;
import com.leofesk.quicktodomanager.view.notes.EditFrame;
import com.leofesk.quicktodomanager.view.settings.GeneralFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {
    private static JLabel labelForInfoAndMessages;
    private static JLabel labelEndDate;
    private static JLabel labelNoteNameForViewCurrentNote;
    private static boolean isActive;
    private JLabel labelStatusForStats;
    private JLabel labelDeadlineForStats;
    private JLabel labelCreatedForStats;
    private JLabel labelEndForStats;
    private static JLabel labelDeadlineDate;
    private static JLabel labelCreatedDate;
    private static JMenu menuMain;
    private static JMenu menuSettings;
    private static JMenu menuHelp;
    private static JMenuBar menuBar;
    private JMenuItem menuHelpItemAbout;
    private JMenuItem menuSettingsItemGeneral;
    private JMenuItem menuMainItemCreateDB;
    private JMenuItem menuMainItemOpenDB;
    private JMenuItem menuMainItemExit;
    private JPanel panelForViewCurrentNote;
    private JPanel panelForStatsAboutNote;
    private JScrollPane scrollPaneForTableNotes;
    private JScrollPane scrollPaneForViewNote;
    public static JTable tableNotes;
    private static JTextArea textAreaForViewCurrentNote;
    private static JToolBar mainToolBar;
    private AddFrame addFrame;
    private EditFrame editFrame;
    private AboutFrame aboutFrame;
    private GeneralFrame generalFrame;
    private static ImageIcon imageAppIcon;
    private static JButton buttonAddForToolBar;
    private static JButton buttonEditForToolBar;
    private static JButton buttonDeleteForToolBar;
    private static JButton buttonChangeStatus;
    private static JComboBox<String> comboBoxSelectStatusForNote;
    private DefaultTableCellRenderer centerRenderer;

    public MainFrame() {
        initComponents();
    }

    private void initComponents() {
        addFrame = new AddFrame();
        editFrame = new EditFrame();
        aboutFrame = new AboutFrame();
        generalFrame = new GeneralFrame();
        scrollPaneForTableNotes = new JScrollPane();
        tableNotes = new JTable();
        mainToolBar = new JToolBar();
        buttonAddForToolBar = new JButton();
        buttonEditForToolBar = new JButton();
        buttonDeleteForToolBar = new JButton();
        labelForInfoAndMessages = new JLabel();
        panelForViewCurrentNote = new JPanel();
        labelNoteNameForViewCurrentNote = new JLabel();
        scrollPaneForViewNote = new JScrollPane();
        textAreaForViewCurrentNote = new JTextArea();
        panelForStatsAboutNote = new JPanel();
        labelStatusForStats = new JLabel();
        labelDeadlineForStats = new JLabel();
        labelCreatedForStats = new JLabel();
        labelEndForStats = new JLabel();
        labelDeadlineDate = new JLabel();
        labelCreatedDate = new JLabel();
        labelEndDate = new JLabel();
        buttonChangeStatus = new JButton();
        comboBoxSelectStatusForNote = new JComboBox<>();
        menuBar = new JMenuBar();
        menuMain = new JMenu();
        menuMainItemCreateDB = new JMenuItem();
        menuMainItemOpenDB = new JMenuItem();
        menuMainItemExit = new JMenuItem();
        menuSettings = new JMenu();
        menuSettingsItemGeneral = new JMenuItem();
        menuHelp = new JMenu();
        menuHelpItemAbout = new JMenuItem();
        centerRenderer = new DefaultTableCellRenderer();
        imageAppIcon = new ImageIcon(MainFrame.class.getResource(Options.getOptionsValue("appLogo")));
        setIconImage(imageAppIcon.getImage());
        isActive = true;

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(" Quick To Do Manager");
        setResizable(false);

        tableNotes.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                        Message.getText("tableID"),
                        Message.getText("tableTitle"),
                        Message.getText("tableDeadline"),
                        Message.getText("tableStatus"),
                        Message.getText("tableCreatedTime"),
                        Message.getText("tableEndTime")
                }
        ) {
            Class[] types = new Class[]{
                    java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                    false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        tableNotes.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableNotes.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tableNotes.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tableNotes.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tableNotes.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tableNotes.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);

        scrollPaneForTableNotes.setViewportView(tableNotes);
        if (tableNotes.getColumnModel().getColumnCount() > 0) {
            tableNotes.getColumnModel().getColumn(0).setMinWidth(35);
            tableNotes.getColumnModel().getColumn(0).setPreferredWidth(35);
            tableNotes.getColumnModel().getColumn(0).setMaxWidth(35);
            tableNotes.getColumnModel().getColumn(1).setMinWidth(400);
            tableNotes.getColumnModel().getColumn(1).setPreferredWidth(400);
            tableNotes.getColumnModel().getColumn(1).setMaxWidth(400);
        }

        tableNotes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (isActive) {
                    int row = tableNotes.rowAtPoint(evt.getPoint());
                    int col = tableNotes.columnAtPoint(evt.getPoint());
                    if (row >= 0 && col >= 0) {
                        DataBaseWorker.showSelectedNoteInfo((int) tableNotes.getValueAt(row, 0));
                        comboBoxSelectStatusForNote.setEnabled(true);
                        buttonEditForToolBar.setEnabled(true);
                        buttonDeleteForToolBar.setEnabled(true);
                        DataBaseWorker.showMessage(Message.getText("infoSelectedTask")+" [" + tableNotes.getValueAt(row, 0) + "]");
                    }
                }
            }
        });

        mainToolBar.setBorder(null);
        mainToolBar.setRollover(true);

        buttonAddForToolBar.setText(Message.getText("toolBarButtonAdd"));

        mainToolBar.add(buttonAddForToolBar);
        buttonAddForToolBar.setEnabled(false);

        buttonEditForToolBar.setText(Message.getText("toolBarButtonEdit"));

        mainToolBar.add(buttonEditForToolBar);
        buttonEditForToolBar.setEnabled(false);

        buttonDeleteForToolBar.setText(Message.getText("toolBarButtonDelete"));

        mainToolBar.add(buttonDeleteForToolBar);
        buttonDeleteForToolBar.setEnabled(false);

        textAreaForViewCurrentNote.setWrapStyleWord(true);
        textAreaForViewCurrentNote.setLineWrap(true);
        textAreaForViewCurrentNote.setMargin(new Insets(5, 5, 5, 5));

        labelForInfoAndMessages.setHorizontalAlignment(SwingConstants.CENTER);
        labelForInfoAndMessages.setText(Message.getText("infoTipAtStartup"));

        panelForViewCurrentNote.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        labelNoteNameForViewCurrentNote.setHorizontalAlignment(SwingConstants.CENTER);
        labelNoteNameForViewCurrentNote.setText(Message.getText("viewTitle"));

        textAreaForViewCurrentNote.setEditable(false);
        textAreaForViewCurrentNote.setColumns(20);
        textAreaForViewCurrentNote.setRows(5);
        textAreaForViewCurrentNote.setText(Message.getText("viewText"));
        textAreaForViewCurrentNote.setBorder(null);
        scrollPaneForViewNote.setViewportView(textAreaForViewCurrentNote);

        panelForStatsAboutNote.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        labelStatusForStats.setText(Message.getText("viewStatus"));
        labelDeadlineForStats.setText(Message.getText("viewDeadline"));
        labelCreatedForStats.setText(Message.getText("viewCreated"));
        labelEndForStats.setText(Message.getText("viewEnd"));
        labelDeadlineDate.setText("Select task");
        labelCreatedDate.setText("Select task");
        labelEndDate.setText("Select task");

        buttonChangeStatus.setText(Message.getText("viewButtonApplyStatus"));
        buttonChangeStatus.setEnabled(false);


        comboBoxSelectStatusForNote.setModel(new DefaultComboBoxModel<>(new String[]{
                Message.getText("viewComboBoxValueInWork"),
                Message.getText("viewComboBoxValueDone")}));
        comboBoxSelectStatusForNote.setToolTipText(Message.getText("viewComboBoxTip"));

        comboBoxSelectStatusForNote.setEnabled(false);

        GroupLayout jPanel2Layout = new GroupLayout(panelForStatsAboutNote);
        panelForStatsAboutNote.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(buttonChangeStatus, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(labelCreatedForStats, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(labelDeadlineForStats, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                                                        .addComponent(labelStatusForStats, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(labelEndForStats, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(10, 10, 10)
                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                .addComponent(labelEndDate, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(labelCreatedDate, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(labelDeadlineDate, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                        .addComponent(comboBoxSelectStatusForNote, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
                                .addGap(10, 10, 10))
        );

        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelStatusForStats)
                                        .addComponent(comboBoxSelectStatusForNote, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelDeadlineForStats)
                                        .addComponent(labelDeadlineDate))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelCreatedForStats)
                                        .addComponent(labelCreatedDate))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelEndForStats)
                                        .addComponent(labelEndDate))
                                .addGap(10, 10, 10)
                                .addComponent(buttonChangeStatus, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(10, 10, 10))
        );

        GroupLayout jPanel1Layout = new GroupLayout(panelForViewCurrentNote);
        panelForViewCurrentNote.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(labelNoteNameForViewCurrentNote, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(scrollPaneForViewNote)
                                                .addGap(10, 10, 10)
                                                .addComponent(panelForStatsAboutNote, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                .addGap(10, 10, 10))
        );

        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(labelNoteNameForViewCurrentNote, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(panelForStatsAboutNote, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(scrollPaneForViewNote))
                                .addGap(10, 10, 10))
        );

        menuMain.setText(Message.getText("mainTitle"));

        menuMainItemCreateDB.setText(Message.getText("mainItemCreateDB"));

        menuMain.add(menuMainItemCreateDB);

        menuMainItemOpenDB.setText(Message.getText("mainItemOpenDB"));

        menuMain.add(menuMainItemOpenDB);

        menuMainItemExit.setText(Message.getText("mainItemExit"));

        menuMain.add(menuMainItemExit);

        menuBar.add(menuMain);

        menuSettings.setText(Message.getText("settingsTitle"));
        menuSettingsItemGeneral.setText(Message.getText("settingsItemGeneral"));

        menuSettings.add(menuSettingsItemGeneral);

        menuBar.add(menuSettings);

        menuHelp.setText(Message.getText("helpTitle"));

        menuHelpItemAbout.setText(Message.getText("helpItemAbout"));

        menuHelp.add(menuHelpItemAbout);

        menuBar.add(menuHelp);

        setJMenuBar(menuBar);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(mainToolBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addGap(10, 10, 10)
                                                .addComponent(labelForInfoAndMessages, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(panelForViewCurrentNote, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(scrollPaneForTableNotes, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 827, Short.MAX_VALUE))
                                .addGap(5, 5, 5))
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(labelForInfoAndMessages, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(mainToolBar, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                                .addGap(5, 5, 5)
                                .addComponent(scrollPaneForTableNotes, GroupLayout.PREFERRED_SIZE, 288, GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(panelForViewCurrentNote, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5))
        );

        buttonAddForToolBar.addActionListener(evt -> actionButtonAddForToolBar());
        buttonEditForToolBar.addActionListener(evt -> actionButtonEditForToolBar());
        buttonDeleteForToolBar.addActionListener(evt -> actionButtonDeleteForToolBar());
        buttonChangeStatus.addActionListener(e -> actionButtonChangeStatus());
        comboBoxSelectStatusForNote.addActionListener(evt -> actionComboBoxSelectStatusForNote());
        menuMainItemCreateDB.addActionListener(evt -> actionMenuMainItemCreateDB());
        menuMainItemOpenDB.addActionListener(evt -> actionMenuMainItemOpenDB());
        menuMainItemExit.addActionListener(evt -> actionMenuMainItemExit());
        menuSettingsItemGeneral.addActionListener(evt -> actionMenuSettingsItemGeneral());
        menuHelpItemAbout.addActionListener(evt -> actionMenuHelpItemAbout());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                askAboutExit();
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    private void askAboutExit() {
        if (JOptionPane.showConfirmDialog(null,
                Message.getText("exitConfirmText"), Message.getText("exitConfirmTitle"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public static void setEnabledWindowElement(boolean statement) {
        tableNotes.setEnabled(statement);
        isActive = statement;
        buttonAddForToolBar.setEnabled(statement);
        buttonEditForToolBar.setEnabled(statement);
        buttonDeleteForToolBar.setEnabled(statement);
        menuMain.setEnabled(statement);
        menuSettings.setEnabled(statement);
        menuHelp.setEnabled(statement);
        comboBoxSelectStatusForNote.setEnabled(statement);
        buttonChangeStatus.setEnabled(statement);
    }

    private void actionMenuSettingsItemGeneral() {
        generalFrame.dispose();
        generalFrame.setVisible(true);
        setEnabledWindowElement(false);
    }

    private void actionButtonEditForToolBar() {
        editFrame.dispose();
        DataBaseWorker.addNoteToEditFrame();
        editFrame.setVisible(true);
        setEnabledWindowElement(false);
    }

    private void actionButtonDeleteForToolBar() {
        DataBaseWorker.deleteSelectedNote();
        buttonEditForToolBar.setEnabled(false);
        buttonDeleteForToolBar.setEnabled(false);
        DataBaseWorker.clearViewBlock();
        comboBoxSelectStatusForNote.setSelectedItem("In work");
        comboBoxSelectStatusForNote.setEnabled(false);
        buttonChangeStatus.setEnabled(false);
    }

    private void actionMenuMainItemCreateDB() {
        String databaseName = JOptionPane.showInputDialog(
                null,
                Message.getText("createDBText"),
                Message.getText("createDBTitle"),
                JOptionPane.INFORMATION_MESSAGE);
        if (databaseName != null && !databaseName.trim().isEmpty()) {
            DataBaseWorker.createDatabase(databaseName);
            buttonAddForToolBar.setEnabled(true);
        } else {
            DataBaseWorker.showMessage(Message.getText("errorCreateDB"));
        }
    }

    private void actionMenuMainItemOpenDB() {
        DataBaseWorker.chooseDatabase();
    }

    private void actionMenuMainItemExit() {
        askAboutExit();
    }

    private void actionComboBoxSelectStatusForNote() {
        buttonChangeStatus.setEnabled(true);
    }

    private void actionButtonAddForToolBar() {
        addFrame.dispose();
        addFrame.setVisible(true);
        setEnabledWindowElement(false);
    }

    private void actionButtonChangeStatus() {
        String value = comboBoxSelectStatusForNote.getSelectedItem().toString();
        DataBaseWorker.changeStatusToCurrentNote(value);
        DataBaseWorker.showSelectedNoteInfo(DataBaseWorker.getCurrentNoteID());
    }

    private void actionMenuHelpItemAbout() {
        aboutFrame.dispose();
        aboutFrame.setVisible(true);
        setEnabledWindowElement(false);

    }

    public static void changeEnabledForToolbarAddButton(boolean enabled) {
        buttonAddForToolBar.setEnabled(enabled);
    }

    public static void setLabelForInfoAndMessages(String message) {
        labelForInfoAndMessages.setText(message);
    }

    public static void setLabelEndDate(String endTime) {
        labelEndDate.setText(endTime);
    }

    public static void setLabelNoteNameForViewCurrentNote(String title) {
        labelNoteNameForViewCurrentNote.setText(title);
    }

    public static void setLabelDeadlineDate(String deadline) {
        labelDeadlineDate.setText(deadline);
    }

    public static void setLabelCreatedDate(String createdTime) {
        labelCreatedDate.setText(createdTime);
    }

    public static void setTextAreaForViewCurrentNote(String text) {
        textAreaForViewCurrentNote.setText(text);
        textAreaForViewCurrentNote.setCaretPosition(0);
    }
}
