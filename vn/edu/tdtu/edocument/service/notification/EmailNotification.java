package vn.edu.tdtu.edocument.service.notification;

import vn.edu.tdtu.edocument.model.Document;

public class EmailNotification implements NotificationObserver {
    @Override
    public void update(Document doc) {
        System.out.println("  [NOTIFY] -> EMAIL gửi đến " + doc.applicantEmail + ": Hồ sơ [" + doc.id + "] chuyển sang trạng thái " + doc.status);
    }
}
