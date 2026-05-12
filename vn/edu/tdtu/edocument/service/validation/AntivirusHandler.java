package vn.edu.tdtu.edocument.service.validation;

import vn.edu.tdtu.edocument.model.Document;

public class AntivirusHandler extends ValidationHandler {
    @Override
    public boolean validate(Document doc) {
        System.out.println("[VALIDATION] Trạm 2: Quét mã độc (Antivirus)...");
        
        // Mô phỏng quét virus
        if (doc.filePath.contains("virus")) {
            System.err.println("  [THẤT BẠI] Phát hiện mã độc trong tệp tin!");
            return false;
        }
        
        System.out.println("  [OK] Không phát hiện mã độc.");
        return checkNext(doc);
    }
}
