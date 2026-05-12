package vn.edu.tdtu.edocument.service.persistence;

import vn.edu.tdtu.edocument.model.Document;

public class SqlDatabaseRepository implements DocumentRepository {
    @Override
    public void save(Document doc) {
        System.out.println("[STORAGE] Đang lưu trữ vào SQL Database (MySQL/PostgreSQL)...");
        // Mô phỏng kết nối và lưu vào DB
        System.out.println("  [QUERY] INSERT INTO documents (id, applicant, status) VALUES ('" + doc.id + "', '" + doc.applicantName + "', '" + doc.status + "')");
        System.out.println("  [OK] Đã lưu dữ liệu vào bảng documents.");
    }
}
