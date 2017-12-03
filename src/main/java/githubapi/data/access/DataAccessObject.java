package githubapi.data.access;

import java.util.List;

public abstract class DataAccessObject<T> {
    abstract List<T> getAll();
    abstract boolean insert(T entity);
    abstract boolean update(T entity);
    abstract boolean delete(T entity);
}
