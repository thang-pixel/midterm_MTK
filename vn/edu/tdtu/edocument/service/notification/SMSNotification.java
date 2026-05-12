package vn.edu.tdtu.edocument.service.notification;

import vn.edu.tdtu.edocument.model.Document;

public class SMSNotification implements NotificationObserver {
    @Override
    public void update(Document doc) {
        System.out.println("  [NOTIFY] -> SMS gửi đến " + doc.applicantPhone + ": Hồ sơ [" + doc.id + "] chuyển sang trạng thái " + doc.status);
    }
}
