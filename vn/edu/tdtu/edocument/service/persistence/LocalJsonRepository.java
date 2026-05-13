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
            Path sourcePath = Paths.get(doc.filePath);
            Path targetPath = Paths.get(storageDirPath + File.separator + doc.id + "_" + sourcePath.getFileName().toString());
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

            String json = "{\n" +
                    "  \"id\": \"" + doc.id + "\",\n" +
                    "  \"applicantName\": \"" + doc.applicantName + "\",\n" +
                    "  \"applicantEmail\": \"" + doc.applicantEmail + "\",\n" +
                    "  \"applicantPhone\": \"" + doc.applicantPhone + "\",\n" +
                    "  \"officerName\": \"" + doc.officerName + "\",\n" +
                    "  \"officerEmail\": \"" + doc.officerEmail + "\",\n" +
                    "  \"officerPhone\": \"" + doc.officerPhone + "\",\n" +
                    "  \"documentType\": \"" + doc.documentType + "\",\n" +
                    "  \"filePath\": \"" + targetPath.toString().replace("\\", "\\\\") + "\",\n" +
                    "  \"fileExtension\": \"" + doc.fileExtension + "\",\n" +
                    "  \"fileSizeKB\": " + doc.fileSizeKB + ",\n" +
                    "  \"digitalSignature\": \"" + doc.digitalSignature + "\",\n" +
                    "  \"priority\": " + doc.priority + ",\n" +
                    "  \"notificationPreferences\": \"" + String.join(",", doc.notificationPreferences) + "\",\n" +
                    "  \"isDraft\": " + doc.isDraft + ",\n" +
                    "  \"status\": \"" + doc.status + "\"\n" +
                    "}";

            String suffix = doc.isDraft ? "_draft.json" : "_data.json";
            File dataFile = new File(storageDirPath + File.separator + doc.id + suffix);
            try (FileWriter writer = new FileWriter(dataFile)) {
                writer.write(json);
            }
            System.out.println("  [OK] Đã lưu tệp JSON: " + dataFile.getName());

        } catch (IOException e) {
            System.err.println("  [LỖI] Lỗi khi lưu trữ Local JSON: " + e.getMessage());
        }
    }
}
