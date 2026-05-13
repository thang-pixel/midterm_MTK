package vn.edu.tdtu.edocument;

import vn.edu.tdtu.edocument.model.Document;
import vn.edu.tdtu.edocument.service.DocumentProcessor;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddDocumentDialog extends JDialog {
    private JTextField txtApplicantName, txtApplicantEmail, txtApplicantPhone;
    private JTextField txtOfficerName, txtOfficerEmail, txtOfficerPhone;
    private JComboBox<String> cbDocumentType, cbPriority;
    private JTextField txtDigitalSignature;
    private JLabel lblFileName;
    private JCheckBox chkEmail, chkSms, chkApp;
    
    private File selectedFile;
    private final DocumentProcessor processor;
    private final MainSwingUI parent;

    public AddDocumentDialog(MainSwingUI parent, DocumentProcessor processor) {
        super(parent, "TIẾP NHẬN HỒ SƠ - v2.0 Wizard", true);
        this.parent = parent;
        this.processor = processor;

        setSize(600, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Bước 1: Thông tin", createInfoPanel());
        tabbedPane.addTab("Bước 2: Hồ sơ & Thông báo", createDetailsPanel());
        add(tabbedPane, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);
        
        JButton btnDraft = new JButton("Lưu nháp");
        UIStyle.applyButtonStyle(btnDraft, UIStyle.SECONDARY_COLOR);
        btnDraft.addActionListener(e -> submitAction(true));

        JButton btnSubmit = new JButton("Nộp hồ sơ");
        UIStyle.applyButtonStyle(btnSubmit, UIStyle.PRIMARY_COLOR);
        btnSubmit.addActionListener(e -> submitAction(false));

        JButton btnCancel = new JButton("Hủy bỏ");
        UIStyle.applyButtonStyle(btnCancel, UIStyle.DANGER_COLOR);
        btnCancel.addActionListener(e -> dispose());

        btnPanel.add(btnDraft);
        btnPanel.add(btnSubmit);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private JPanel createInfoPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Applicant
        gbc.gridx = 0; gbc.gridy = 0; p.add(new JLabel("Tên người nộp:"), gbc);
        gbc.gridx = 1; txtApplicantName = new JTextField(20); p.add(txtApplicantName, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; p.add(new JLabel("Email người nộp:"), gbc);
        gbc.gridx = 1; txtApplicantEmail = new JTextField(20); p.add(txtApplicantEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 2; p.add(new JLabel("SĐT người nộp:"), gbc);
        gbc.gridx = 1; txtApplicantPhone = new JTextField(20); p.add(txtApplicantPhone, gbc);

        // Divider
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        p.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;

        // Officer
        gbc.gridx = 0; gbc.gridy = 4; p.add(new JLabel("Cán bộ tiếp nhận:"), gbc);
        gbc.gridx = 1; txtOfficerName = new JTextField("Cán bộ trực ban", 20); p.add(txtOfficerName, gbc);

        gbc.gridx = 0; gbc.gridy = 5; p.add(new JLabel("Email cán bộ:"), gbc);
        gbc.gridx = 1; txtOfficerEmail = new JTextField("officer@tdtu.edu.vn", 20); p.add(txtOfficerEmail, gbc);

        return p;
    }

    private JPanel createDetailsPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; p.add(new JLabel("Loại hồ sơ:"), gbc);
        cbDocumentType = new JComboBox<>(new String[]{"DON_XIN_PHEP", "BAO_CAO", "HOP_DONG", "KHAC"});
        gbc.gridx = 1; p.add(cbDocumentType, gbc);

        gbc.gridx = 0; gbc.gridy = 1; p.add(new JLabel("Mức ưu tiên:"), gbc);
        cbPriority = new JComboBox<>(new String[]{"0 - Thường", "1 - Khẩn", "2 - Thượng khẩn"});
        gbc.gridx = 1; p.add(cbPriority, gbc);

        gbc.gridx = 0; gbc.gridy = 2; p.add(new JLabel("Chữ ký số:"), gbc);
        txtDigitalSignature = new JTextField(20);
        gbc.gridx = 1; p.add(txtDigitalSignature, gbc);

        gbc.gridx = 0; gbc.gridy = 3; p.add(new JLabel("Tệp đính kèm:"), gbc);
        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        filePanel.setBackground(Color.WHITE);
        JButton btnBrowse = new JButton("Duyệt file...");
        lblFileName = new JLabel("Chưa chọn tệp");
        lblFileName.setForeground(UIStyle.PRIMARY_COLOR);
        btnBrowse.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();
                lblFileName.setText(selectedFile.getName());
            }
        });
        filePanel.add(btnBrowse);
        filePanel.add(Box.createHorizontalStrut(10));
        filePanel.add(lblFileName);
        gbc.gridx = 1; p.add(filePanel, gbc);

        // Notifications
        gbc.gridx = 0; gbc.gridy = 4; p.add(new JLabel("Nhận thông báo:"), gbc);
        JPanel chkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        chkPanel.setBackground(Color.WHITE);
        chkEmail = new JCheckBox("Email", true);
        chkSms = new JCheckBox("SMS");
        chkApp = new JCheckBox("App Push", true);
        chkPanel.add(chkEmail); chkPanel.add(chkSms); chkPanel.add(chkApp);
        gbc.gridx = 1; p.add(chkPanel, gbc);

        return p;
    }

    private void submitAction(boolean isDraft) {
        String filePath = (selectedFile != null) ? selectedFile.getAbsolutePath() : "";
        String ext = "";
        long size = 0;
        if (selectedFile != null) {
            size = selectedFile.length() / 1024;
            String name = selectedFile.getName();
            int lastDot = name.lastIndexOf('.');
            if (lastDot >= 0 && lastDot < name.length() - 1) {
                ext = name.substring(lastDot + 1).toLowerCase().trim();
            }
        }

        List<String> prefs = new ArrayList<>();
        if (chkEmail.isSelected()) prefs.add("EMAIL");
        if (chkSms.isSelected()) prefs.add("SMS");
        if (chkApp.isSelected()) prefs.add("APP");

        int priority = cbPriority.getSelectedIndex();

        Document doc = new Document.Builder(UUID.randomUUID().toString().substring(0, 8))
            .applicantInfo(txtApplicantName.getText().trim(), txtApplicantEmail.getText().trim(), txtApplicantPhone.getText().trim())
            .officerInfo(txtOfficerName.getText().trim(), txtOfficerEmail.getText().trim(), txtOfficerPhone.getText().trim())
            .documentDetails(cbDocumentType.getSelectedItem().toString(), txtDigitalSignature.getText().trim(), priority)
            .fileInfo(filePath, ext, size)
            .notifications(prefs)
            .isDraft(isDraft)
            .build();

        processor.process(doc);

        if (isDraft || "DANG_XET_DUYET".equals(doc.status)) {
            parent.addDocumentToList(doc);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Hồ sơ không hợp lệ. Vui lòng kiểm tra log hệ thống bên dưới.", "Lỗi Kiểm Duyệt", JOptionPane.ERROR_MESSAGE);
        }
    }
}