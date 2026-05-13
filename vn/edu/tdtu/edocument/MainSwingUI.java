package vn.edu.tdtu.edocument;

import vn.edu.tdtu.edocument.model.Document;
import vn.edu.tdtu.edocument.service.DocumentProcessor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class MainSwingUI extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextArea logArea;
    private final DocumentProcessor processor;
    private List<Document> documentList = new ArrayList<>();

    public MainSwingUI() {
        this.processor = new DocumentProcessor();
        setupUI();
        loadExistingDocuments();
        setVisible(true);
    }

    private void setupUI() {
        setTitle("HỆ THỐNG QUẢN LÝ HỒ SƠ ĐIỆN TỬ - v2.0 Professional");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIStyle.PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 80));
        
        JLabel titleLabel = new JLabel("E-DOCUMENT MANAGER");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 0));
        
        JLabel subTitleLabel = new JLabel("Version 2.0 | Advanced Management System (Wizard Mode)");
        subTitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        subTitleLabel.setForeground(new Color(200, 200, 200));
        subTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 0));
        
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subTitleLabel, BorderLayout.CENTER);

        // Action Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        JButton btnAdd = new JButton("Tiếp nhận hồ sơ");
        UIStyle.applyButtonStyle(btnAdd, UIStyle.SUCCESS_COLOR);
        btnAdd.addActionListener(e -> new AddDocumentDialog(this, processor).setVisible(true));

        JButton btnConfig = new JButton("Cấu hình hệ thống");
        UIStyle.applyButtonStyle(btnConfig, UIStyle.PRIMARY_COLOR);
        btnConfig.addActionListener(e -> showConfigDialog());

        JButton btnClearLog = new JButton("Xóa nhật ký");
        UIStyle.applyButtonStyle(btnClearLog, UIStyle.DANGER_COLOR);
        btnClearLog.addActionListener(e -> logArea.setText(""));

        actionPanel.add(btnAdd);
        actionPanel.add(btnConfig);
        actionPanel.add(btnClearLog);

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(headerPanel, BorderLayout.NORTH);
        topContainer.add(actionPanel, BorderLayout.CENTER);
        add(topContainer, BorderLayout.NORTH);

        // Table Panel
        String[] columns = {"Mã hồ sơ", "Người nộp", "Loại hồ sơ", "Mức ưu tiên", "Trạng thái", "Tập tin"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        UIStyle.applyTableStyle(table);
        
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách hồ sơ đang xử lý & bản nháp"));
        add(tableScrollPane, BorderLayout.CENTER);

        // Log Panel
        logArea = new JTextArea(10, 50);
        logArea.setEditable(false);
        logArea.setBackground(new Color(30, 30, 30));
        logArea.setForeground(Color.WHITE);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        
        PrintStream printStream = new PrintStream(new CustomOutputStream(logArea));
        System.setOut(printStream);
        System.setErr(printStream);

        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setBorder(BorderFactory.createTitledBorder("Nhật ký hệ thống (Real-time Logs)"));
        add(logScrollPane, BorderLayout.SOUTH);
    }

    private void showConfigDialog() {
        JDialog config = new JDialog(this, "Cấu hình Hệ thống (Runtime Patterns)", true);
        config.setSize(400, 300);
        config.setLayout(new GridBagLayout());
        config.setLocationRelativeTo(this);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; gbc.gridy = 0; config.add(new JLabel("Kho lưu trữ:"), gbc);
        JComboBox<String> cbRepo = new JComboBox<>(new String[]{"Local JSON", "SQL Database", "AWS S3 Cloud"});
        gbc.gridx = 1; config.add(cbRepo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; config.add(new JLabel("Chuỗi kiểm duyệt:"), gbc);
        JComboBox<String> cbChain = new JComboBox<>(new String[]{"Mặc định (Dung lượng->Virus->Trùng)", "Rút gọn (Dung lượng->Trùng)", "Đảo ngược (Trùng->Virus->Dung lượng)"});
        gbc.gridx = 1; config.add(cbChain, gbc);

        JButton btnSave = new JButton("Áp dụng cấu hình");
        UIStyle.applyButtonStyle(btnSave, UIStyle.PRIMARY_COLOR);
        btnSave.addActionListener(e -> {
            // Demo switching Repository
            int repoIdx = cbRepo.getSelectedIndex();
            if (repoIdx == 1) processor.setRepository(new vn.edu.tdtu.edocument.service.persistence.SqlDatabaseRepository());
            else if (repoIdx == 2) processor.setRepository(new vn.edu.tdtu.edocument.service.persistence.CloudS3Repository());
            else processor.setRepository(new vn.edu.tdtu.edocument.service.persistence.LocalJsonRepository());

            // Demo switching Chain
            int chainIdx = cbChain.getSelectedIndex();
            vn.edu.tdtu.edocument.service.validation.ValidationHandler basic = new vn.edu.tdtu.edocument.service.validation.BasicValidationHandler();
            vn.edu.tdtu.edocument.service.validation.ValidationHandler antivirus = new vn.edu.tdtu.edocument.service.validation.AntivirusHandler();
            vn.edu.tdtu.edocument.service.validation.ValidationHandler integrity = new vn.edu.tdtu.edocument.service.validation.IntegrityHandler();
            
            if (chainIdx == 1) { // Rút gọn
                basic.setNext(integrity);
                processor.setValidationChain(basic);
            } else if (chainIdx == 2) { // Đảo ngược
                integrity.setNext(antivirus);
                antivirus.setNext(basic);
                processor.setValidationChain(integrity);
            } else { // Mặc định
                basic.setNext(antivirus);
                antivirus.setNext(integrity);
                processor.setValidationChain(basic);
            }
            
            JOptionPane.showMessageDialog(config, "Đã cập nhật cấu hình hệ thống tại Runtime!");
            config.dispose();
        });
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        config.add(btnSave, gbc);
        config.setVisible(true);
    }

    public void addDocumentToList(Document doc) {
        documentList.add(doc);
        String priorityText = doc.priority == 2 ? "Thượng khẩn" : (doc.priority == 1 ? "Khẩn" : "Thường");
        tableModel.addRow(new Object[]{
            doc.id, doc.applicantName, doc.documentType, priorityText, doc.status, doc.fileExtension
        });
    }

    private void loadExistingDocuments() {
        File dir = new File("server_storage");
        if (!dir.exists()) return;
        
        File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));
        if (files == null) return;

        for (File f : files) {
            try {
                String content = new String(Files.readAllBytes(f.toPath()));
                // Sơ đồ nạp đơn giản (Mô phỏng nạp từ JSON)
                String id = extractValue(content, "id");
                String name = extractValue(content, "applicantName");
                String type = extractValue(content, "documentType");
                String status = extractValue(content, "status");
                String ext = extractValue(content, "fileExtension");
                String pStr = extractValue(content, "priority");
                int priority = Integer.parseInt(pStr.isEmpty() ? "0" : pStr);
                
                String priorityText = priority == 2 ? "Thượng khẩn" : (priority == 1 ? "Khẩn" : "Thường");
                tableModel.addRow(new Object[]{id, name, type, priorityText, status, ext});
            } catch (Exception e) {
                // Skip invalid files
            }
        }
    }

    private String extractValue(String json, String key) {
        String pattern = "\"" + key + "\": \"";
        int start = json.indexOf(pattern);
        if (start == -1) {
            // Try numeric
            pattern = "\"" + key + "\": ";
            start = json.indexOf(pattern);
            if (start == -1) return "";
            start += pattern.length();
            int end = json.indexOf(",", start);
            if (end == -1) end = json.indexOf("\n", start);
            if (end == -1) end = json.indexOf("}", start);
            return json.substring(start, end).trim().replace("\"", "");
        }
        start += pattern.length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainSwingUI::new);
    }

    static class CustomOutputStream extends java.io.OutputStream {
        private JTextArea textArea;
        public CustomOutputStream(JTextArea textArea) { this.textArea = textArea; }
        @Override
        public void write(int b) {
            textArea.append(String.valueOf((char) b));
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }
    }
}