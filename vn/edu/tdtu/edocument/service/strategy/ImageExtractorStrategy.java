package vn.edu.tdtu.edocument.service.strategy;

public class ImageExtractorStrategy implements ContentExtractorStrategy {
    @Override
    public String extract(String filePath) {
        System.out.println("[STRATEGY] Đang dùng ImageExtractor (Mô phỏng AI OCR cho ảnh)...");
        // Mô phỏng gọi dịch vụ AI OCR
        return "[NỘI DUNG ẢNH ĐÃ TRÍCH XUẤT OCR TỪ: " + filePath + "]";
    }
}
