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
        // --- 1. THIẾT LẬP CHAIN OF RESPONSIBILITY (Yêu cầu 3) ---
        ValidationHandler basic = new BasicValidationHandler();
        ValidationHandler antivirus = new AntivirusHandler();
        ValidationHandler integrity = new IntegrityHandler();
        
        basic.setNext(antivirus);
        antivirus.setNext(integrity);
        this.validationChain = basic;

        // --- 2. THIẾT LẬP OBSERVER PATTERN (Yêu cầu 4) ---
        this.notificationService = new NotificationService();
        notificationService.subscribe(new EmailNotification());
        notificationService.subscribe(new SMSNotification());
        notificationService.subscribe(new AppPushNotification());

        // --- 3. THIẾT LẬP REPOSITORY/ADAPTER (Yêu cầu 5) ---
        // Có thể dễ dàng thay đổi giữa LocalJsonRepository, SqlDatabaseRepository, CloudS3Repository
        this.repository = new LocalJsonRepository();
    }

    public void process(Document doc) {
        System.out.println("\n=======================================================");
        System.out.println("BẮT ĐẦU XỬ LÝ HỒ SƠ ID: " + doc.id);

        if (!isInfoComplete(doc)) {
            System.err.println("[LỖI TIẾP NHẬN] Thiếu trường thông tin bắt buộc.");
            return;
        }

        doc.status = "DA_TIEP_NHAN";
        notificationService.notifyObservers(doc);

        // --- BƯỚC 1: KIỂM DUYỆT LINH HOẠT (Chain of Responsibility) ---
        if (!validationChain.validate(doc)) {
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