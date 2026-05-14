package vn.edu.tdtu.edocument.service;

import vn.edu.tdtu.edocument.model.Document;
import vn.edu.tdtu.edocument.service.strategy.*;
import vn.edu.tdtu.edocument.service.validation.*;
import vn.edu.tdtu.edocument.service.notification.*;
import vn.edu.tdtu.edocument.service.persistence.*;

import java.io.File;

public class DocumentProcessor {
    private ValidationHandler validationChain;
    private NotificationService notificationService;
    private DocumentRepository repository;

    public DocumentProcessor() {
        this.notificationService = new NotificationService();
        notificationService.subscribe(new EmailNotification());
        notificationService.subscribe(new SMSNotification());
        notificationService.subscribe(new AppPushNotification());

        // Default configuration
        setupDefaultChain();
        this.repository = new LocalJsonRepository();
    }

    private void setupDefaultChain() {
        ValidationHandler basic = new BasicValidationHandler();
        ValidationHandler antivirus = new AntivirusHandler();
        ValidationHandler integrity = new IntegrityHandler();
        basic.setNext(antivirus);
        antivirus.setNext(integrity);
        this.validationChain = basic;
    }

    // Yêu cầu 3 & 5: Cấu hình linh hoạt tại Runtime
    public void setValidationChain(ValidationHandler chain) { this.validationChain = chain; }
    
    /**
     * NÂNG CẤP YÊU CẦU 3: Cơ chế cấu hình mở hoàn toàn cho quy trình kiểm duyệt.
     * Cho phép lắp ghép bất kỳ thứ tự nào dựa trên danh sách tên Handler (có thể đọc từ file config).
     */
    public void configureChain(java.util.List<String> handlerNames) {
        if (handlerNames == null || handlerNames.isEmpty()) return;
        
        System.out.println("[CONFIG] Thiết lập chuỗi kiểm duyệt động: " + String.join(" -> ", handlerNames));
        
        ValidationHandler first = null;
        ValidationHandler current = null;

        for (String name : handlerNames) {
            ValidationHandler handler = createHandlerByName(name);
            if (handler != null) {
                if (first == null) {
                    first = handler;
                    current = first;
                } else {
                    current.setNext(handler);
                    current = handler;
                }
            }
        }
        this.validationChain = first;
    }

    private ValidationHandler createHandlerByName(String name) {
        switch (name.toUpperCase().trim()) {
            case "BASIC": return new BasicValidationHandler();
            case "ANTIVIRUS": return new AntivirusHandler();
            case "INTEGRITY": return new IntegrityHandler();
            default: return null;
        }
    }

    public void setRepository(DocumentRepository repo) { this.repository = repo; }
    public DocumentRepository getRepository() { return this.repository; }

    public void process(Document doc) {
        System.out.println("\n=======================================================");
        if (doc.isDraft) {
            System.out.println("LƯU NHÁP HỒ SƠ ID: " + doc.id);
            doc.status = "NHAP";
            repository.save(doc);
            System.out.println("[OK] Đã lưu nháp.");
            return;
        }

        System.out.println("BẮT ĐẦU XỬ LÝ HỒ SƠ CHÍNH THỨC ID: " + doc.id);

        if (!isInfoComplete(doc)) {
            System.err.println("[LỖI TIẾP NHẬN] Thiếu trường thông tin bắt buộc.");
            return;
        }

        doc.status = "DA_TIEP_NHAN";
        notificationService.notifyObservers(doc);

        // --- BƯỚC 1: KIỂM DUYỆT LINH HOẠT (Chain of Responsibility) ---
        if (validationChain != null && !validationChain.validate(doc)) {
            doc.status = "TU_CHOI";
            System.err.println("[KẾT QUẢ] Hồ sơ bị từ chối ở bước kiểm duyệt.");
            notificationService.notifyObservers(doc);
            return;
        }

        // --- BƯỚC 2: TRÍCH XUẤT NỘI DUNG (Strategy Pattern) ---
        ContentExtractorStrategy extractor = ExtractorFactory.getExtractor(doc.fileExtension);
        if (extractor != null) {
            System.out.println("[TRÍCH XUẤT] Đang xử lý bằng " + extractor.getClass().getSimpleName());
            doc.extractedContent = extractor.extract(doc.filePath);
        }

        // --- BƯỚC 3: LƯU TRỮ ĐA NỀN TẢNG (Repository/Adapter Pattern) ---
        doc.status = "DA_XU_LY";
        repository.save(doc);

        System.out.println("[HOÀN TẤT] Quy trình xử lý hồ sơ kết thúc thành công.");
        doc.status = "DANG_XET_DUYET";
        notificationService.notifyObservers(doc);
    }

    private boolean isInfoComplete(Document doc) {
        return doc.id != null && !doc.id.isEmpty() &&
               doc.applicantName != null && !doc.applicantName.isEmpty() &&
               doc.applicantEmail != null && !doc.applicantEmail.isEmpty() &&
               doc.filePath != null && !doc.filePath.isEmpty();
    }
}