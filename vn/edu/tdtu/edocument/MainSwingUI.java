package vn.edu.tdtu.edocument;

import vn.edu.tdtu.edocument.model.Document;
import vn.edu.tdtu.edocument.service.DocumentProcessor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class MainSwingUI extends JFrame {
    private JTextArea consoleArea;
    private JTable documentTable;
    private DefaultTableModel tableModel;
    private DocumentProcessor processor;
    private List<Document> documentList;

    public MainSwingUI() {
        UIStyle.applyGlobalStyle();
        processor = new DocumentProcessor();
        documentList = new ArrayList<>();

        setTitle("Electronic Document Management System - v2.0 Professional");
        setSize(1100, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIStyle.BACKGROUND_COLOR);

        // --- HEADER PANEL ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIStyle.PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel titleLabel = new JLabel("E-DOCUMENT MANAGER");
        titleLabel.setFont(UIStyle.TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JLabel subTitleLabel = new JLabel("Version 2.0 | Advanced Management System");
        subTitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        subTitleLabel.setForeground(new Color(200, 230, 255));
        headerPanel.add(subTitleLabel, BorderLayout.SOUTH);
        
        add(headerPanel, BorderLayout.NORTH);

        // --- CENTER CONTENT ---
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Toolbar
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolBar.setOpaque(false);
        JButton btnAdd = new JButton(" Tiếp nhận hồ sơ");
        JButton btnClear = new JButton("Xóa nhật ký");
        
        UIStyle.styleButton(btnAdd, UIStyle.SUCCESS_COLOR, Color.WHITE);
        UIStyle.styleButton(btnClear, UIStyle.DANGER_COLOR, Color.WHITE);
        
        toolBar.add(btnAdd);
        toolBar.add(btnClear);
        contentPanel.add(toolBar, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"Mã hồ sơ", "Người nộp", "Loại hồ sơ", "Trạng thái", "Tập tin"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        documentTable = new JTable(tableModel);
        UIStyle.styleTable(documentTable);
        
        JScrollPane tableScrollPane = new JScrollPane(documentTable);
        tableScrollPane.setBorder(UIStyle.createSectionBorder("Danh sách hồ sơ đang xử lý"));
        tableScrollPane.getViewport().setBackground(Color.WHITE);

        // Console
        consoleArea = new JTextArea();
        consoleArea.setEditable(false);
        consoleArea.setBackground(UIStyle.CONSOLE_BG);
        consoleArea.setForeground(new Color(171, 178, 191)); // One Dark theme text color
        consoleArea.setFont(UIStyle.CONSOLE_FONT);
        consoleArea.setMargin(new Insets(10, 10, 10, 10));
        
        JScrollPane logScrollPane = new JScrollPane(consoleArea);
        logScrollPane.setBorder(UIStyle.createSectionBorder("Nhật ký hệ thống (Real-time Logs)"));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScrollPane, logScrollPane);
        splitPane.setDividerLocation(350);
        splitPane.setBorder(null);
        splitPane.setOpaque(false);
        
        contentPanel.add(splitPane, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);

        redirectSystemStreams();
        loadExistingDocuments();
        refreshTable();

        btnAdd.addActionListener(e -> {
            AddDocumentDialog dialog = new AddDocumentDialog(this, processor);
            dialog.setVisible(true);
            refreshTable();
        });

        btnClear.addActionListener(e -> consoleArea.setText(""));

        documentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && documentTable.getSelectedRow() != -1) {
                int selectedRow = documentTable.getSelectedRow();
                String docId = tableModel.getValueAt(selectedRow, 0).toString();
                
                for (Document doc : documentList) {
                    if (doc.id.equals(docId)) {
                        System.out.println("\n\u001B[34m[THÔNG TIN CHI TIẾT] ID: " + doc.id + "\u001B[0m");
                        System.out.println("  > Người nộp: " + doc.applicantName + " (" + doc.applicantEmail + ")");
                        System.out.println("  > Cán bộ:    " + doc.officerName);
                        System.out.println("  > Loại:      " + doc.documentType);
                        System.out.println("  > Tệp tin:   " + doc.filePath + " (" + doc.fileSizeKB + " KB)");
                        System.out.println("  > Trạng thái: " + doc.status);
                        System.out.println("----------------------------------------\n");
                        break;
                    }
                }
            }
        });
    }

    private void loadExistingDocuments() {
        File storageDir = new File("server_storage");
        if (storageDir.exists() && storageDir.isDirectory()) {
            File[] files = storageDir.listFiles((dir, name) -> name.endsWith("_data.json"));
            if (files != null) {
                for (File file : files) {
                    try {
                        String content = new String(Files.readAllBytes(file.toPath()));
                        Document doc = parseJsonToDocument(content);
                        if (doc != null) {
                            documentList.add(doc);
                        }
                    } catch (Exception e) {
                        System.err.println("[LỖI] Không thể nạp hồ sơ: " + file.getName());
                    }
                }
            }
        }
    }

    private Document parseJsonToDocument(String json) {
        try {
            String id = extractValue(json, "id");
            String applicantName = extractValue(json, "applicantName");
            String applicantEmail = extractValue(json, "applicantEmail");
            String applicantPhone = extractValue(json, "applicantPhone");
            String officerName = extractValue(json, "officerName");
            String officerEmail = extractValue(json, "officerEmail");
            String officerPhone = extractValue(json, "officerPhone");
            String documentType = extractValue(json, "documentType");
            String filePath = extractValue(json, "filePath");
            String fileExtension = extractValue(json, "fileExtension");
            String fileSizeStr = extractValue(json, "fileSizeKB");
            long fileSizeKB = (fileSizeStr != null && !fileSizeStr.isEmpty()) ? Long.parseLong(fileSizeStr) : 0;
            String digitalSignature = extractValue(json, "digitalSignature");
            String status = extractValue(json, "status");

            return new Document(id, applicantName, applicantEmail, applicantPhone,
                    officerName, officerEmail, officerPhone, documentType,
                    filePath, fileExtension, fileSizeKB, digitalSignature, null, status);
        } catch (Exception e) {
            return null;
        }
    }

    private String extractValue(String json, String key) {
        try {
            String pattern = "\"" + key + "\": ";
            int start = json.indexOf(pattern);
            if (start == -1) return "";
            start += pattern.length();
            if (json.charAt(start) == '\"') {
                start++;
                int end = json.indexOf("\"", start);
                return json.substring(start, end);
            } else {
                int end = json.indexOf(",", start);
                if (end == -1) end = json.indexOf("\n", start);
                if (end == -1) end = json.indexOf("}", start);
                return json.substring(start, end).trim();
            }
        } catch (Exception e) {
            return "";
        }
    }

    public void addDocumentToList(Document doc) {
        if (!documentList.contains(doc)) {
            documentList.add(doc);
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Document doc : documentList) {
            tableModel.addRow(new Object[]{
                doc.id, doc.applicantName, doc.documentType, doc.status, doc.fileExtension
            });
        }
    }

    private void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
            @Override public void write(int b) { updateTextArea(String.valueOf((char) b)); }
            @Override public void write(byte[] b, int off, int len) { updateTextArea(new String(b, off, len)); }
        };
        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }

    private void updateTextArea(final String text) {
        SwingUtilities.invokeLater(() -> {
            consoleArea.append(text);
            consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainSwingUI frame = new MainSwingUI();
            frame.setVisible(true);
        });
    }

}