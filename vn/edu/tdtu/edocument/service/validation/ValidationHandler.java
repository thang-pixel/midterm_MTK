package vn.edu.tdtu.edocument.service.validation;

import vn.edu.tdtu.edocument.model.Document;

public abstract class ValidationHandler {
    protected ValidationHandler next;

    public void setNext(ValidationHandler next) {
        this.next = next;
    }

    public abstract boolean validate(Document doc);

    protected boolean checkNext(Document doc) {
        if (next == null) {
            return true;
        }
        return next.validate(doc);
    }
}
