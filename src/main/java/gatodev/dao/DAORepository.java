package gatodev.dao;

import java.util.List;

public interface DAORepository<T, ID> {
    T save(T t);
    T update(T t);
    void delete(ID id);
    List<T> findAll();
    T findById(ID id);
}
