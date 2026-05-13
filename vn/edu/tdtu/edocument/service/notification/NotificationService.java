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
        // Yêu cầu 4: Đăng ký nhận thông báo theo nhu cầu
        for (NotificationObserver observer : observers) {
            String type = observer.getClass().getSimpleName().toUpperCase();
            if (shouldNotify(doc, type)) {
                observer.update(doc);
            }
        }
        
        // Nghiệp vụ v1.0 2.5: Gửi thông báo cho quản lý nếu Khẩn trở lên (Priority >= 1) 
        // Lưu ý: Đề bài ghi Priority >= 2 (Thượng khẩn) hoặc Khẩn (Priority 1)
        if (doc.priority >= 1) {
            System.out.println("  [MANAGER-ALERT] -> Gửi báo cáo khẩn đến Trưởng phòng (" + doc.officerEmail + ")");
        }
    }

    private boolean shouldNotify(Document doc, String observerType) {
        if (doc.notificationPreferences == null || doc.notificationPreferences.isEmpty()) {
            return true; // Mặc định gửi tất cả nếu không cấu hình (Backward compatibility)
        }
        for (String pref : doc.notificationPreferences) {
            if (observerType.contains(pref)) return true;
        }
        return false;
    }
}
