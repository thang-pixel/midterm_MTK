package vn.edu.tdtu.edocument.service.strategy;

public class PdfExtractorStrategy implements ContentExtractorStrategy {
    @Override
    public String extract(String filePath) {
        System.out.println("[STRATEGY] Đang dùng PdfExtractor (Mô phỏng trích xuất văn bản từ PDF)...");
        // Mô phỏng gọi dịch vụ bên thứ ba
        return "[NỘI DUNG PDF ĐÃ TRÍCH XUẤT TỪ: " + filePath + "]";
    }
}
