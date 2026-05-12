package vn.edu.tdtu.edocument.service.persistence;

import vn.edu.tdtu.edocument.model.Document;

public class CloudS3Repository implements DocumentRepository {
    @Override
    public void save(Document doc) {
        System.out.println("[STORAGE] Đang đẩy hồ sơ lên Amazon AWS S3 Bucket...");
        // Mô phỏng đẩy file lên Cloud
        System.out.println("  [S3] Uploading file: " + doc.id + "_" + doc.fileExtension + " to bucket 'edoc-storage-v2'");
        System.out.println("  [OK] Hồ sơ đã được lưu trữ an toàn trên Đám mây.");
    }
}
