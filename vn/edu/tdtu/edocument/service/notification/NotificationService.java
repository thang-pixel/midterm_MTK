package vn.edu.tdtu.edocument.service.notification;

import vn.edu.tdtu.edocument.model.Document;
import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    private List<NotificationObserver> observers = new ArrayList<>();

    public void subscribe(NotificationObserver observer) {
        observers.add(observer);
    }

    public void unsubscribe(NotificationObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Document doc) {
        // Yêu cầu 4: Đăng ký nhận thông báo theo nhu cầu riêng biệt
        // Người nộp và Cán bộ có thể chọn kênh khác nhau
        for (NotificationObserver observer : observers) {
            String type = observer.getClass().getSimpleName().toUpperCase();
            
            boolean notifyApplicant = shouldNotify(doc.applicantPrefs, type);
            boolean notifyOfficer = shouldNotify(doc.officerPrefs, type);

            if (notifyApplicant || notifyOfficer) {
                // Ta có thể in log chi tiết hơn để thấy sự khác biệt
                if (notifyApplicant) System.out.println("  [NOTIFY] Gửi " + type + " cho Người nộp: " + doc.applicantName);
                if (notifyOfficer) System.out.println("  [NOTIFY] Gửi " + type + " cho Cán bộ: " + doc.officerName);
                observer.update(doc);
            }
        }
        
        // Nghiệp vụ v1.0 2.5: Gửi thông báo cho quản lý nếu Thượng khẩn (Priority >= 2)
        // Điều chỉnh lại điều kiện theo đúng ý giảng viên (Thượng khẩn mới báo quản lý)
        if (doc.priority >= 2) {
            System.out.println("  [MANAGER-ALERT] -> Gửi báo cáo khẩn đến Trưởng phòng (" + doc.officerEmail + ")");
        }
    }

    private boolean shouldNotify(List<String> prefs, String observerType) {
        if (prefs == null || prefs.isEmpty()) {
            return false; // Phải đăng ký mới gửi (Đúng tinh thần "Đăng ký nhận thông báo")
        }
        for (String pref : prefs) {
            if (observerType.contains(pref)) return true;
        }
        return false;
    }
}
