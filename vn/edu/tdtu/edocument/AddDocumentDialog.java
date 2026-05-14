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
    private JCheckBox chkAppEmail, chkAppSms, chkAppPush;
    private JCheckBox chkOffEmail, chkOffSms, chkOffPush;
    
    private File selectedFile;
    private final DocumentProcessor processor;
    private final MainSwingUI parent;

    public AddDocumentDialog(MainSwingUI parent, DocumentProcessor processor) {
        this(parent, processor, null);
    }

    public AddDocumentDialog(MainSwingUI parent, DocumentProcessor processor, Document existingDoc) {
        super(parent, existingDoc == null ? "TIẾP NHẬN HỒ SƠ - v2.0 Wizard" : "TIẾP TỤC HOÀN THIỆN HỒ SƠ", true);
        this.parent = parent;
        this.processor = processor;

        setSize(600, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Bước 1: Thông tin", createInfoPanel());
        tabbedPane.addTab("Bước 2: Hồ sơ & Thông báo", createDetailsPanel());
        add(tabbedPane, BorderLayout.CENTER);

        if (existingDoc != null) {
            prefillFields(existingDoc);
        }

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);
        
        JButton btnDraft = new JButton("Lưu nháp");
        UIStyle.applyButtonStyle(btnDraft, UIStyle.SECONDARY_COLOR);
        btnDraft.addActionListener(e -> submitAction(true, existingDoc != null ? existingDoc.id : null));

        JButton btnSubmit = new JButton("Nộp hồ sơ");
        UIStyle.applyButtonStyle(btnSubmit, UIStyle.PRIMARY_COLOR);
        btnSubmit.addActionListener(e -> submitAction(false, existingDoc != null ? existingDoc.id : null));

        JButton btnCancel = new JButton("Hủy bỏ");
        UIStyle.applyButtonStyle(btnCancel, UIStyle.DANGER_COLOR);
        btnCancel.addActionListener(e -> dispose());

        btnPanel.add(btnDraft);
        btnPanel.add(btnSubmit);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void prefillFields(Document doc) {
        txtApplicantName.setText(doc.applicantName);
        txtApplicantEmail.setText(doc.applicantEmail);
        txtApplicantPhone.setText(doc.applicantPhone);
        txtOfficerName.setText(doc.officerName);
        txtOfficerEmail.setText(doc.officerEmail);
        txtOfficerPhone.setText(doc.officerPhone);
        cbDocumentType.setSelectedItem(doc.documentType);
        cbPriority.setSelectedIndex(doc.priority);
        txtDigitalSignature.setText(doc.digitalSignature);
        if (doc.filePath != null && !doc.filePath.isEmpty()) {
            selectedFile = new File(doc.filePath);
            lblFileName.setText(selectedFile.getName());
        }
        if (doc.applicantPrefs != null) {
            chkAppEmail.setSelected(doc.applicantPrefs.contains("EMAIL"));
            chkAppSms.setSelected(doc.applicantPrefs.contains("SMS"));
            chkAppPush.setSelected(doc.applicantPrefs.contains("APP"));
        }
        if (doc.officerPrefs != null) {
            chkOffEmail.setSelected(doc.officerPrefs.contains("EMAIL"));
            chkOffSms.setSelected(doc.officerPrefs.contains("SMS"));
            chkOffPush.setSelected(doc.officerPrefs.contains("APP"));
        }
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

        gbc.gridx = 0; gbc.gridy = 6; p.add(new JLabel("SĐT cán bộ:"), gbc);
        gbc.gridx = 1; txtOfficerPhone = new JTextField("0123456789", 20); p.add(txtOfficerPhone, gbc);

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

        // Notifications - YÊU CẦU 4 NÂNG CẤP
        gbc.gridx = 0; gbc.gridy = 4; p.add(new JLabel("Thông báo Người nộp:"), gbc);
        JPanel appChkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        appChkPanel.setBackground(Color.WHITE);
        chkAppEmail = new JCheckBox("Email", true);
        chkAppSms = new JCheckBox("SMS");
        chkAppPush = new JCheckBox("App Push", true);
        appChkPanel.add(chkAppEmail); appChkPanel.add(chkAppSms); appChkPanel.add(chkAppPush);
        gbc.gridx = 1; p.add(appChkPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 5; p.add(new JLabel("Thông báo Cán bộ:"), gbc);
        JPanel offChkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        offChkPanel.setBackground(Color.WHITE);
        chkOffEmail = new JCheckBox("Email", true);
        chkOffSms = new JCheckBox("SMS", true);
        chkOffPush = new JCheckBox("App Push");
        offChkPanel.add(chkOffEmail); offChkPanel.add(chkOffSms); offChkPanel.add(chkOffPush);
        gbc.gridx = 1; p.add(offChkPanel, gbc);

        return p;
    }

    private void submitAction(boolean isDraft, String existingId) {
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

        List<String> appPrefs = new ArrayList<>();
        if (chkAppEmail.isSelected()) appPrefs.add("EMAIL");
        if (chkAppSms.isSelected()) appPrefs.add("SMS");
        if (chkAppPush.isSelected()) appPrefs.add("APP");

        List<String> offPrefs = new ArrayList<>();
        if (chkOffEmail.isSelected()) offPrefs.add("EMAIL");
        if (chkOffSms.isSelected()) offPrefs.add("SMS");
        if (chkOffPush.isSelected()) offPrefs.add("APP");

        int priority = cbPriority.getSelectedIndex();
        String id = (existingId != null) ? existingId : UUID.randomUUID().toString().substring(0, 8);

        Document doc = new Document.Builder(id)
            .applicantInfo(txtApplicantName.getText().trim(), txtApplicantEmail.getText().trim(), txtApplicantPhone.getText().trim())
            .officerInfo(txtOfficerName.getText().trim(), txtOfficerEmail.getText().trim(), txtOfficerPhone.getText().trim())
            .documentDetails(cbDocumentType.getSelectedItem().toString(), txtDigitalSignature.getText().trim(), priority)
            .fileInfo(filePath, ext, size)
            .applicantPrefs(appPrefs)
            .officerPrefs(offPrefs)
            .isDraft(isDraft)
            .build();

        processor.process(doc);

        if (isDraft || "DANG_XET_DUYET".equals(doc.status)) {
            if (existingId == null) {
                parent.addDocumentToList(doc);
            } else {
                parent.refreshTable();
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Hồ sơ không hợp lệ. Vui lòng kiểm tra log hệ thống bên dưới.", "Lỗi Kiểm Duyệt", JOptionPane.ERROR_MESSAGE);
        }
    }
}