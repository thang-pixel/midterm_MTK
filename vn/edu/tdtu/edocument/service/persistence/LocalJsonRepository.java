package vn.edu.tdtu.edocument.service.persistence;

import vn.edu.tdtu.edocument.model.Document;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class LocalJsonRepository implements DocumentRepository {
    private final String storageDirPath = "server_storage";

    @Override
    public void save(Document doc) {
        System.out.println("[STORAGE] Đang lưu trữ vào Local JSON...");
        File storageDir = new File(storageDirPath);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }

        try {
            if (doc.id == null) doc.id = java.util.UUID.randomUUID().toString().substring(0, 8);
            
            String finalTargetFilePath = "";
            
            // Chỉ thực hiện copy file nếu đường dẫn file không rỗng và file tồn tại
            if (doc.filePath != null && !doc.filePath.trim().isEmpty()) {
                java.io.File sourceFile = new java.io.File(doc.filePath);
                if (sourceFile.exists() && sourceFile.isFile()) {
                    Path sourcePath = Paths.get(doc.filePath);
                    Path targetPath = Paths.get(storageDirPath + File.separator + doc.id + "_" + sourcePath.getFileName().toString());
                    Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    finalTargetFilePath = targetPath.toString().replace("\\", "\\\\");
                }
            }

            // Đảm bảo notificationPreferences không null
            String prefsStr = "";
            if (doc.notificationPreferences != null) {
                prefsStr = String.join(",", doc.notificationPreferences);
            }

            String json = "{\n" +
                    "  \"id\": \"" + doc.id + "\",\n" +
                    "  \"applicantName\": \"" + doc.applicantName + "\",\n" +
                    "  \"applicantEmail\": \"" + doc.applicantEmail + "\",\n" +
                    "  \"applicantPhone\": \"" + doc.applicantPhone + "\",\n" +
                    "  \"officerName\": \"" + doc.officerName + "\",\n" +
                    "  \"officerEmail\": \"" + doc.officerEmail + "\",\n" +
                    "  \"officerPhone\": \"" + doc.officerPhone + "\",\n" +
                    "  \"documentType\": \"" + doc.documentType + "\",\n" +
                    "  \"filePath\": \"" + finalTargetFilePath + "\",\n" +
                    "  \"fileExtension\": \"" + doc.fileExtension + "\",\n" +
                    "  \"fileSizeKB\": " + doc.fileSizeKB + ",\n" +
                    "  \"digitalSignature\": \"" + doc.digitalSignature + "\",\n" +
                    "  \"priority\": " + doc.priority + ",\n" +
                    "  \"notificationPreferences\": \"" + prefsStr + "\",\n" +
                    "  \"isDraft\": " + doc.isDraft + ",\n" +
                    "  \"status\": \"" + doc.status + "\"\n" +
                    "}";

            String suffix = doc.isDraft ? "_draft.json" : "_data.json";
            File dataFile = new File(storageDirPath + File.separator + doc.id + suffix);
            try (FileWriter writer = new FileWriter(dataFile)) {
                writer.write(json);
            }
            System.out.println("  [OK] Đã lưu tệp JSON: " + dataFile.getName());

        } catch (Exception e) {
            System.err.println("  [LỖI HỆ THỐNG] Không thể lưu trữ: " + e.toString());
            e.printStackTrace();
        }
    }
}
