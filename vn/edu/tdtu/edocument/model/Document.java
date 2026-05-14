package vn.edu.tdtu.edocument.model;

import java.util.ArrayList;
import java.util.List;

public class Document {
    public String id;
    public String applicantName;
    public String applicantEmail;
    public String applicantPhone;
    public String officerName;
    public String officerEmail;
    public String officerPhone;
    public String documentType;
    public String filePath;
    public String fileExtension;
    public long fileSizeKB;
    public String digitalSignature;
    public String extractedContent;
    public String status;
    
    // Yêu cầu v2.0 & v1.0 bổ sung
    public int priority; // 0: Thường, 1: Khẩn, 2: Thượng khẩn
    public List<String> applicantPrefs; // "EMAIL", "SMS", "APP"
    public List<String> officerPrefs;   // "EMAIL", "SMS", "APP"
    public boolean isDraft;

    private Document(Builder builder) {
        this.id = builder.id;
        this.applicantName = builder.applicantName;
        this.applicantEmail = builder.applicantEmail;
        this.applicantPhone = builder.applicantPhone;
        this.officerName = builder.officerName;
        this.officerEmail = builder.officerEmail;
        this.officerPhone = builder.officerPhone;
        this.documentType = builder.documentType;
        this.filePath = builder.filePath;
        this.fileExtension = builder.fileExtension;
        this.fileSizeKB = builder.fileSizeKB;
        this.digitalSignature = builder.digitalSignature;
        this.extractedContent = builder.extractedContent;
        this.status = builder.status;
        this.priority = builder.priority;
        this.applicantPrefs = builder.applicantPrefs;
        this.officerPrefs = builder.officerPrefs;
        this.isDraft = builder.isDraft;
    }

    public static class Builder {
        private String id;
        private String applicantName = "";
        private String applicantEmail = "";
        private String applicantPhone = "";
        private String officerName = "Cán bộ trực ban";
        private String officerEmail = "officer@tdtu.edu.vn";
        private String officerPhone = "0123456789";
        private String documentType = "";
        private String filePath = "";
        private String fileExtension = "";
        private long fileSizeKB = 0;
        private String digitalSignature = "";
        private String extractedContent = "";
        private String status = "MOI_TAO";
        private int priority = 0;
        private List<String> applicantPrefs = new ArrayList<>();
        private List<String> officerPrefs = new ArrayList<>();
        private boolean isDraft = false;

        public Builder(String id) {
            this.id = id;
        }

        public Builder applicantInfo(String name, String email, String phone) {
            this.applicantName = name;
            this.applicantEmail = email;
            this.applicantPhone = phone;
            return this;
        }

        public Builder officerInfo(String name, String email, String phone) {
            this.officerName = name;
            this.officerEmail = email;
            this.officerPhone = phone;
            return this;
        }

        public Builder documentDetails(String type, String signature, int priority) {
            this.documentType = type;
            this.digitalSignature = signature;
            this.priority = priority;
            return this;
        }

        public Builder fileInfo(String path, String extension, long sizeKB) {
            this.filePath = path;
            this.fileExtension = extension;
            this.fileSizeKB = sizeKB;
            return this;
        }

        public Builder applicantPrefs(List<String> prefs) {
            this.applicantPrefs = prefs;
            return this;
        }

        public Builder officerPrefs(List<String> prefs) {
            this.officerPrefs = prefs;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder isDraft(boolean draft) {
            this.isDraft = draft;
            return this;
        }

        public Document build() {
            return new Document(this);
        }
    }

    @Override
    public String toString() {
        return String.format("Hồ sơ [%s] - Nộp bởi: %s - Trạng thái: %s", id, applicantName, status);
    }
}