package services;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.itsjava.dao.UserDao;
import ru.itsjava.dao.UserDaoImpl;
import ru.itsjava.domain.User;
import ru.itsjava.services.ClientRunnable;
import ru.itsjava.services.ServerServiceImpl;
import ru.itsjava.utils.Props;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientRunnableTest {

    @Test
    public void registration() {
        UserDao userDao = new UserDaoImpl(new Props());
        ClientRunnable clientRunnable = new ClientRunnable(new Socket(), new ServerServiceImpl(), userDao);
        String registrationMessge = "!registr!UT:PT";
        User user = new User("UT", "PT");

        clientRunnable.registration(registrationMessge);
        assertEquals(user, userDao.findByNameAndPassword("UT", "PT"));
    }

    @AfterAll
    public static void clear() {
        UserDaoImpl userDao = new UserDaoImpl(new Props());
        userDao.removeUser("UT");
    }
}
