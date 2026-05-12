package vn.edu.tdtu.edocument.service.persistence;

import vn.edu.tdtu.edocument.model.Document;

public interface DocumentRepository {
    void save(Document doc);
}
