package vn.edu.tdtu.edocument.service.validation;

import vn.edu.tdtu.edocument.model.Document;
import vn.edu.tdtu.edocument.service.strategy.ExtractorFactory;

public class BasicValidationHandler extends ValidationHandler {
    @Override
    public boolean validate(Document doc) {
        System.out.println("[VALIDATION] Trạm 1: Kiểm tra tính hợp lệ cơ bản...");
        
        // Nghiệp vụ v1.0 2.2: Kiểm tra Email người nộp (chứa @)
        if (doc.applicantEmail == null || !doc.applicantEmail.contains("@")) {
            System.err.println("  [THẤT BẠI] Email người nộp không hợp lệ.");
            return false;
        }

        // Nghiệp vụ v1.0 2.2: Kiểm tra tệp đính kèm thực sự tồn tại
        java.io.File file = new java.io.File(doc.filePath);
        if (!file.exists()) {
            System.err.println("  [THẤT BẠI] Tệp tin đính kèm không tồn tại trên ổ cứng.");
            return false;
        }

        if (doc.fileSizeKB > 5120) {
            System.err.println("  [THẤT BẠI] Dung lượng file " + doc.fileSizeKB + "KB vượt quá 5MB.");
            return false;
        }
        
        if (ExtractorFactory.getExtractor(doc.fileExtension) == null) {
            String msg = (doc.fileExtension == null || doc.fileExtension.isEmpty()) 
                        ? "Hồ sơ không có định dạng tệp tin (Thiếu extension)."
                        : "Định dạng '" + doc.fileExtension + "' không được hỗ trợ.";
            System.err.println("  [THẤT BẠI] " + msg);
            return false;
        }
        
        System.out.println("  [OK] Hợp lệ cơ bản.");
        return checkNext(doc);
    }
}
