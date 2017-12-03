package githubapi.data.access;

import githubapi.data.models.User;

import java.util.List;

public class UserDao extends DataAccessObject<User>{
    @Override
    List<User> getAll() {
        return null;
    }

    @Override
    boolean insert(User entity) {
        return false;
    }

    @Override
    boolean update(User entity) {
        return false;
    }

    @Override
    boolean delete(User entity) {
        return false;
    }
}
