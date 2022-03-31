package services;

import org.junit.jupiter.api.Test;
import ru.itsjava.dao.UserDao;
import ru.itsjava.dao.UserDaoImpl;
import ru.itsjava.services.ClientRunnable;
import ru.itsjava.services.ServerServiceImpl;
import ru.itsjava.utils.Props;

import java.net.Socket;

public class ClientRunnableTest {

    @Test
    public void registration() {
        UserDao userDao = new UserDaoImpl(new Props());
        ClientRunnable clientRunnable = new ClientRunnable(new Socket(), new ServerServiceImpl(), userDao);
        String registrationMessge = "!registr!UT:PT";

    }
}
