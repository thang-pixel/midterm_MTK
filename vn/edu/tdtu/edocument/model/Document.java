package vn.edu.tdtu.edocument.model;

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
    }

    // Default constructor for backward compatibility if needed, but better use Builder
    public Document(String id, String applicantName, String applicantEmail, String applicantPhone,
                    String officerName, String officerEmail, String officerPhone,
                    String documentType, String filePath, String fileExtension, 
                    long fileSizeKB, String digitalSignature, String extractedContent, String status) {
        this.id = id;
        this.applicantName = applicantName;
        this.applicantEmail = applicantEmail;
        this.applicantPhone = applicantPhone;
        this.officerName = officerName;
        this.officerEmail = officerEmail;
        this.officerPhone = officerPhone;
        this.documentType = documentType;
        this.filePath = filePath;
        this.fileExtension = fileExtension;
        this.fileSizeKB = fileSizeKB;
        this.digitalSignature = digitalSignature;
        this.extractedContent = extractedContent;
        this.status = status;
    }

    public static class Builder {
        private String id;
        private String applicantName;
        private String applicantEmail;
        private String applicantPhone;
        private String officerName;
        private String officerEmail;
        private String officerPhone;
        private String documentType;
        private String filePath;
        private String fileExtension;
        private long fileSizeKB;
        private String digitalSignature;
        private String extractedContent;
        private String status = "MOI_TAO";

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

        public Builder documentDetails(String type, String signature) {
            this.documentType = type;
            this.digitalSignature = signature;
            return this;
        }

        public Builder fileInfo(String path, String extension, long sizeKB) {
            this.filePath = path;
            this.fileExtension = extension;
            this.fileSizeKB = sizeKB;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder extractedContent(String content) {
            this.extractedContent = content;
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