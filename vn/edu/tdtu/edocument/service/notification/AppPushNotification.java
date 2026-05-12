package vn.edu.tdtu.edocument.service.notification;

import vn.edu.tdtu.edocument.model.Document;

public class AppPushNotification implements NotificationObserver {
    @Override
    public void update(Document doc) {
        System.out.println("  [NOTIFY] -> APP PUSH gửi thông báo đẩy cho ứng dụng của " + doc.applicantName + ": Trạng thái " + doc.status);
    }
}
