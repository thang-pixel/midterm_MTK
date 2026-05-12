package vn.edu.tdtu.edocument.service.strategy;

public class ExtractorFactory {
    public static ContentExtractorStrategy getExtractor(String extension) {
        if (extension == null) return null;
        
        switch (extension.toLowerCase()) {
            case "txt":
                return new TxtExtractorStrategy();
            case "pdf":
                return new PdfExtractorStrategy();
            case "jpg":
            case "jpeg":
            case "png":
                return new ImageExtractorStrategy();
            default:
                return null;
        }
    }
}
