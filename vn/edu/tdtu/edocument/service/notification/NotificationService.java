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
        for (NotificationObserver observer : observers) {
            observer.update(doc);
        }
    }
}
