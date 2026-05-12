package vn.edu.tdtu.edocument.service.validation;

import vn.edu.tdtu.edocument.model.Document;

public class IntegrityHandler extends ValidationHandler {
    @Override
    public boolean validate(Document doc) {
        System.out.println("[VALIDATION] Trạm 3: Kiểm tra tính toàn vẹn (Chống trùng lặp)...");
        
        // Mô phỏng kiểm tra trùng lặp
        if (doc.id.startsWith("DUP")) {
            System.err.println("  [THẤT BẠI] Hồ sơ bị trùng lặp trong hệ thống.");
            return false;
        }
        
        System.out.println("  [OK] Tính toàn vẹn được đảm bảo.");
        return checkNext(doc);
    }
}
