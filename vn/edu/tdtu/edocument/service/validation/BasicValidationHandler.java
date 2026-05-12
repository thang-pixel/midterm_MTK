package vn.edu.tdtu.edocument.service.validation;

import vn.edu.tdtu.edocument.model.Document;
import vn.edu.tdtu.edocument.service.strategy.ExtractorFactory;

public class BasicValidationHandler extends ValidationHandler {
    @Override
    public boolean validate(Document doc) {
        System.out.println("[VALIDATION] Trạm 1: Kiểm tra tính hợp lệ cơ bản...");
        
        if (doc.fileSizeKB > 5120) {
            System.err.println("  [THẤT BẠI] Dung lượng file " + doc.fileSizeKB + "KB vượt quá 5MB.");
            return false;
        }
        
        if (ExtractorFactory.getExtractor(doc.fileExtension) == null) {
            System.err.println("  [THẤT BẠI] Định dạng file " + doc.fileExtension + " không được hỗ trợ.");
            return false;
        }
        
        System.out.println("  [OK] Hợp lệ cơ bản.");
        return checkNext(doc);
    }
}
