package vn.edu.tdtu.edocument.service.notification;

import vn.edu.tdtu.edocument.model.Document;

public interface NotificationObserver {
    void update(Document doc);
}
