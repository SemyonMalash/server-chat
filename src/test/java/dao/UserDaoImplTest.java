package dao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.itsjava.dao.UserDaoImpl;
import ru.itsjava.dao.UserNotFoundException;
import ru.itsjava.domain.User;
import ru.itsjava.utils.Props;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDaoImplTest {

    @Test
    public void addUser() {
        UserDaoImpl userDao = new UserDaoImpl(new Props());

        assertEquals(userDao.addUser("UT", "PT"), userDao.findByNameAndPassword("UT", "PT"));
    }

    @Test
    public void removeUser() {
        UserDaoImpl userDao = new UserDaoImpl(new Props());
        userDao.addUser("UT2", "PT2");

        try {
            userDao.removeUser("UT2");
        } catch (UserNotFoundException e) {
            assertEquals("User not found", userDao.findByNameAndPassword("UT2", "PT2"));
        }
    }

    @Test
    public void findByNameAndPassword() {
        UserDaoImpl userDao = new UserDaoImpl(new Props());
        User user = new User("UT3", "PT3");
        userDao.addUser("UT3", "PT3");

        assertEquals(user, userDao.findByNameAndPassword("UT3", "PT3"));
    }

    @AfterAll
    public static void clear() {
        UserDaoImpl userDao = new UserDaoImpl(new Props());
        userDao.removeUser("UT");
        userDao.removeUser("UT2");
        userDao.removeUser("UT3");
    }
}
