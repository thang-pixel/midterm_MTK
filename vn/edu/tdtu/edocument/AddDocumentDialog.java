package vn.edu.tdtu.edocument;

import vn.edu.tdtu.edocument.model.Document;
import vn.edu.tdtu.edocument.service.DocumentProcessor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.UUID;

public class AddDocumentDialog extends JDialog {
    private JTextField txtApplicantName, txtApplicantEmail, txtApplicantPhone;
    private JTextField txtOfficerName, txtOfficerEmail, txtOfficerPhone;
    private JComboBox<String> cbDocumentType;
    private JTextField txtDigitalSignature;
    private JLabel lblFileName;
    private File selectedFile;
    
    private DocumentProcessor processor;
    private MainSwingUI parent;

    public AddDocumentDialog(MainSwingUI parent, DocumentProcessor processor) {
        super(parent, "Tiếp nhận hồ sơ mới", true);
        this.parent = parent;
        this.processor = processor;
        
        setSize(550, 650);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UIStyle.BACKGROUND_COLOR);

        // --- HEADER ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(UIStyle.PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        JLabel headerLabel = new JLabel("NHẬP THÔNG TIN HỒ SƠ");
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(UIStyle.BOLD_FONT);
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // --- FORM PANEL ---
        JPanel formContainer = new JPanel(new GridBagLayout());
        formContainer.setOpaque(false);
        formContainer.setBorder(new EmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;

        int row = 0;

        // Section: Personal Information
        addSeparator(formContainer, "Thông tin người nộp", gbc, row++);
        
        addField(formContainer, "Tên người nộp:", txtApplicantName = new JTextField(), gbc, row++);
        addField(formContainer, "Email người nộp:", txtApplicantEmail = new JTextField(), gbc, row++);
        addField(formContainer, "SĐT người nộp:", txtApplicantPhone = new JTextField(), gbc, row++);

        // Section: Officer Information
        addSeparator(formContainer, "Thông tin cán bộ tiếp nhận", gbc, row++);
        
        addField(formContainer, "Tên cán bộ:", txtOfficerName = new JTextField("Cán bộ trực ban"), gbc, row++);
        addField(formContainer, "Email cán bộ:", txtOfficerEmail = new JTextField("officer@tdtu.edu.vn"), gbc, row++);
        addField(formContainer, "SĐT cán bộ:", txtOfficerPhone = new JTextField("0123456789"), gbc, row++);

        // Section: Document Details
        addSeparator(formContainer, "Chi tiết hồ sơ", gbc, row++);
        
        cbDocumentType = new JComboBox<>(new String[]{"DON_XIN_PHEP", "BAO_CAO", "HO_SO_THUE"});
        addField(formContainer, "Loại hồ sơ:", cbDocumentType, gbc, row++);
        
        addField(formContainer, "Chữ ký số:", txtDigitalSignature = new JTextField(), gbc, row++);

        // File Selection
        JLabel lblFile = new JLabel("Tập tin đính kèm:");
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formContainer.add(lblFile, gbc);

        JPanel pFile = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pFile.setOpaque(false);
        JButton btnFile = new JButton("Duyệt file...");
        UIStyle.styleButton(btnFile, UIStyle.ACCENT_COLOR, Color.WHITE);
        lblFileName = new JLabel("Chưa chọn tệp");
        lblFileName.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblFileName.setForeground(UIStyle.TEXT_LIGHT);
        pFile.add(btnFile); pFile.add(lblFileName);
        
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 1.0;
        formContainer.add(pFile, gbc);

        JScrollPane scrollPane = new JScrollPane(formContainer);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);

        // --- BUTTONS ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        btnPanel.setOpaque(false);
        JButton btnSend = new JButton("Gửi hồ sơ");
        JButton btnCancel = new JButton("Hủy bỏ");
        
        UIStyle.styleButton(btnSend, UIStyle.PRIMARY_COLOR, Color.WHITE);
        UIStyle.styleButton(btnCancel, Color.GRAY, Color.WHITE);
        
        btnPanel.add(btnCancel);
        btnPanel.add(btnSend);
        add(btnPanel, BorderLayout.SOUTH);

        // --- ACTIONS ---
        btnFile.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedFile = fc.getSelectedFile();
                lblFileName.setText(selectedFile.getName());
                lblFileName.setForeground(UIStyle.SUCCESS_COLOR);
                lblFileName.setFont(UIStyle.BOLD_FONT);
            }
        });

        btnCancel.addActionListener(e -> dispose());

        btnSend.addActionListener(e -> {
            submitAction();
        });
    }

    private void addField(JPanel panel, String label, JComponent field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);
        
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 1.0;
        panel.add(field, gbc);
        
        if (field instanceof JTextField) {
            ((JTextField) field).setMargin(new Insets(5, 10, 5, 10));
        }
    }

    private void addSeparator(JPanel panel, String text, GridBagConstraints gbc, int row) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 10, 0);
        JLabel label = new JLabel(text);
        label.setFont(UIStyle.BOLD_FONT);
        label.setForeground(UIStyle.PRIMARY_COLOR);
        panel.add(label, gbc);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
    }

    private void submitAction() {
        String filePath = (selectedFile != null) ? selectedFile.getAbsolutePath() : "";
        String ext = "";
        long size = 0;
        if (selectedFile != null) {
            size = selectedFile.length() / 1024;
            String name = selectedFile.getName();
            int lastDot = name.lastIndexOf('.');
            if (lastDot > 0) {
                ext = name.substring(lastDot + 1);
            }
        }

        Document doc = new Document.Builder(UUID.randomUUID().toString().substring(0, 8))
            .applicantInfo(
                txtApplicantName.getText().trim(),
                txtApplicantEmail.getText().trim(),
                txtApplicantPhone.getText().trim()
            )
            .officerInfo(
                txtOfficerName.getText().trim(),
                txtOfficerEmail.getText().trim(),
                txtOfficerPhone.getText().trim()
            )
            .documentDetails(
                cbDocumentType.getSelectedItem().toString(),
                txtDigitalSignature.getText().trim()
            )
            .fileInfo(filePath, ext, size)
            .status("MOI_TAO")
            .build();

        processor.process(doc);

        if ("DANG_XET_DUYET".equals(doc.status)) {
            parent.addDocumentToList(doc);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Hồ sơ không hợp lệ. Vui lòng kiểm tra lại log.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

}