package com.leofesk.quicktodomanager.view;

import com.leofesk.quicktodomanager.controller.DataBaseWorker;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class MainFrame extends JFrame {
    private static JLabel labelForInfoAndMessages;
    private static JLabel labelEndDate;
    private static JLabel labelNoteNameForViewCurrentNote;
    private JLabel labelStatusForStats;
    private JLabel labelDeadlineForStats;
    private JLabel labelCreatedForStats;
    private JLabel labelEndForStats;
    private static JLabel labelDeadlineDate;
    private static JLabel labelCreatedDate;
    private JMenu menuMain;
    private JMenu menuHelp;
    private JMenuBar menuBar;
    private JMenuItem menuHelpItemAbout;
    private JMenuItem menuMainItemCreateDB;
    private JMenuItem menuMainItemOpenDB;
    private JMenuItem menuMainItemExit;
    private JPanel panelForViewCurrentNote;
    private JPanel panelForStatsAboutNote;
    private JScrollPane scrollPaneForTableNotes;
    private JScrollPane scrollPaneForViewNote;
    public static JTable tableNotes;
    private static JTextArea textAreaForViewCurrentNote;
    private JToolBar mainToolBar;
    private NoteAddFrame noteAddFrame;
    private NoteEditFrame noteEditFrame;
    private AboutFrame aboutFrame;
    private static ImageIcon imageAppIcon;
    private static JButton buttonAddForToolBar;
    private JButton buttonEditForToolBar;
    private JButton buttonDeleteForToolBar;
    private JButton buttonChangeStatus;
    private JComboBox<String> comboBoxSelectStatusForNote;
    private String pathToAppLogo = "/img/AppLogo.png";
    private DefaultTableCellRenderer centerRenderer;

    public MainFrame() {
        initComponents();
    }

    private void initComponents() {
        noteAddFrame = new NoteAddFrame();
        noteEditFrame = new NoteEditFrame();
        aboutFrame = new AboutFrame();
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
        menuHelp = new JMenu();
        menuHelpItemAbout = new JMenuItem();
        centerRenderer = new DefaultTableCellRenderer();
        imageAppIcon = new ImageIcon(MainFrame.class.getResource(pathToAppLogo));
        setIconImage(imageAppIcon.getImage());

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(" Quick To Do Manager");
        setResizable(false);

        tableNotes.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                        "ID", "Title", "Deadline", "Status", "Created", "End"
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
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tableNotes.rowAtPoint(evt.getPoint());
                int col = tableNotes.columnAtPoint(evt.getPoint());
                if (row >= 0 && col >= 0) {
                    DataBaseWorker.showSelectedNoteInfo((int) tableNotes.getValueAt(row, 0));
                    comboBoxSelectStatusForNote.setEnabled(true);
                    buttonEditForToolBar.setEnabled(true);
                    buttonDeleteForToolBar.setEnabled(true);
                    DataBaseWorker.showMessage("Selected task with ID [" + tableNotes.getValueAt(row, 0) + "]");
                }
            }
        });

        mainToolBar.setBorder(null);
        mainToolBar.setRollover(true);

        buttonAddForToolBar.setText("Add");
        buttonAddForToolBar.addActionListener(evt -> actionButtonAddForToolBar());
        mainToolBar.add(buttonAddForToolBar);
        buttonAddForToolBar.setEnabled(false);

        buttonEditForToolBar.setText("Edit");
        buttonEditForToolBar.addActionListener(evt -> actionButtonEditForToolBar());
        mainToolBar.add(buttonEditForToolBar);
        buttonEditForToolBar.setEnabled(false);

        buttonDeleteForToolBar.setText("Delete");
        buttonDeleteForToolBar.addActionListener(evt -> actionButtonDeleteForToolBar());
        mainToolBar.add(buttonDeleteForToolBar);
        buttonDeleteForToolBar.setEnabled(false);

        textAreaForViewCurrentNote.setWrapStyleWord(true);
        textAreaForViewCurrentNote.setLineWrap(true);
        textAreaForViewCurrentNote.setMargin(new Insets(5, 5, 5, 5));

        labelForInfoAndMessages.setHorizontalAlignment(SwingConstants.CENTER);
        labelForInfoAndMessages.setText("You need create or open existing database. [Menu > Create/Open database]");

        panelForViewCurrentNote.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        labelNoteNameForViewCurrentNote.setHorizontalAlignment(SwingConstants.CENTER);
        labelNoteNameForViewCurrentNote.setText("Choose task to view details.");

        textAreaForViewCurrentNote.setEditable(false);
        textAreaForViewCurrentNote.setColumns(20);
        textAreaForViewCurrentNote.setRows(5);
        textAreaForViewCurrentNote.setText("Not chosen task to view.");
        textAreaForViewCurrentNote.setBorder(null);
        scrollPaneForViewNote.setViewportView(textAreaForViewCurrentNote);

        panelForStatsAboutNote.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        labelStatusForStats.setText("Status:");
        labelDeadlineForStats.setText("Deadline:");
        labelCreatedForStats.setText("Created:");
        labelEndForStats.setText("End:");
        labelDeadlineDate.setText("Select task");
        labelCreatedDate.setText("Select task");
        labelEndDate.setText("Select task");

        buttonChangeStatus.setText("Apply status");
        buttonChangeStatus.setEnabled(false);
        buttonChangeStatus.addActionListener(e -> actionButtonChangeStatus());

        comboBoxSelectStatusForNote.setModel(new DefaultComboBoxModel<>(new String[]{"In work", "Done"}));
        comboBoxSelectStatusForNote.setToolTipText("Select status for this task.");
        comboBoxSelectStatusForNote.addActionListener(evt -> actionComboBoxSelectStatusForNote());
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

        menuMain.setText("Menu");

        menuMainItemCreateDB.setText("Create database");
        menuMainItemCreateDB.addActionListener(evt -> actionMenuMainItemCreateDB());
        menuMain.add(menuMainItemCreateDB);

        menuMainItemOpenDB.setText("Open database");
        menuMainItemOpenDB.addActionListener(evt -> actionMenuMainItemOpenDB());
        menuMain.add(menuMainItemOpenDB);

        menuMainItemExit.setText("Exit");
        menuMainItemExit.addActionListener(evt -> actionMenuMainItemExit());
        menuMain.add(menuMainItemExit);

        menuBar.add(menuMain);

        menuHelp.setText("Help");

        menuHelpItemAbout.setText("About");
        menuHelpItemAbout.addActionListener(evt -> actionMenuHelpItemAbout());
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

        pack();
        setLocationRelativeTo(null);
    }

    private void actionButtonEditForToolBar() {
        noteEditFrame.dispose();
        DataBaseWorker.addNoteToEditFrame();
        noteEditFrame.setVisible(true);
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
                "Choose name for new database:",
                "Create database",
                JOptionPane.INFORMATION_MESSAGE);
        if (databaseName != null && !databaseName.trim().isEmpty()) {
            DataBaseWorker.createDatabase(databaseName);
            buttonAddForToolBar.setEnabled(true);
        } else {
            DataBaseWorker.showMessage("New database was not created. [CODE:V_MN_001]");
        }
    }

    private void actionMenuMainItemOpenDB() {
        DataBaseWorker.chooseDatabase();
    }

    private void actionMenuMainItemExit() {
        System.exit(0);
    }

    private void actionComboBoxSelectStatusForNote() {
        buttonChangeStatus.setEnabled(true);
    }

    private void actionButtonAddForToolBar() {
        noteAddFrame.dispose();
        noteAddFrame.setVisible(true);
    }

    private void actionButtonChangeStatus() {
        String value = comboBoxSelectStatusForNote.getSelectedItem().toString();
        DataBaseWorker.changeStatusToCurrentNote(value);
        DataBaseWorker.showSelectedNoteInfo(DataBaseWorker.getCurrentNoteID());
    }

    private void actionMenuHelpItemAbout() {
        aboutFrame.dispose();
        aboutFrame.setVisible(true);
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
