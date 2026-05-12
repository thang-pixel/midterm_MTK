package vn.edu.tdtu.edocument.service.strategy;

import java.nio.file.Files;
import java.nio.file.Paths;

public class TxtExtractorStrategy implements ContentExtractorStrategy {
    @Override
    public String extract(String filePath) {
        try {
            System.out.println("[STRATEGY] Đang dùng TxtExtractor...");
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (Exception e) {
            return "[LỖI ĐỌC TXT] " + e.getMessage();
        }
    }
}
