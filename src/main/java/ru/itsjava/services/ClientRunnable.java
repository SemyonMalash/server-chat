package ru.itsjava.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.itsjava.dao.MessageDao;
import ru.itsjava.dao.UserDao;
import ru.itsjava.dao.UserNotFoundException;
import ru.itsjava.domain.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

@RequiredArgsConstructor
public class ClientRunnable implements Runnable, Observer {
    private final Socket socket;
    private final ServerService serverService;
    private User user;
    private final UserDao userDao;
    private final MessageDao messageDao;
    private static final Logger logger = LogManager.getLogger(ClientRunnable.class);

    @SneakyThrows
    @Override
    public void run() {
        logger.info("Client is connected");
        BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ArrayList history = new ArrayList();
        String messageFromClient;
        String input;

        while ((input = bufferedReader.readLine()) != null) {
            if (authorization(input)) {
                serverService.addObserver(this);
                while ((messageFromClient = bufferedReader.readLine()) != null) {
                    System.out.println(user.getName() + ":" + messageFromClient);
                    serverService.writeIntoFile(user.getName() + ":" + messageFromClient);
                    messageDao.addMessage(user.getName(), messageFromClient);
                    if (messageFromClient.equals("history")) {
                        serverService.showHistory(messageDao.getHistory(history), this);
                        continue;
                    }
                    serverService.notifyObserversExceptMe(user.getName() + ":" + messageFromClient, this);
                }
            } else if (registration(input)) {
                serverService.addObserver(this);
                while ((messageFromClient = bufferedReader.readLine()) != null) {
                    System.out.println(user.getName() + ":" + messageFromClient);
                    serverService.writeIntoFile(user.getName() + ":" + messageFromClient);
                    messageDao.addMessage(user.getName(), messageFromClient);
                    if (messageFromClient.equals("history")) {
                        serverService.showHistory(messageDao.getHistory(history), this);
                        continue;
                    }
                    serverService.notifyObserversExceptMe(user.getName() + ":" + messageFromClient, this);
                }
            } else if (reauthorization(input)) {
                while ((messageFromClient = bufferedReader.readLine()) != null) {
                    System.out.println(user.getName() + ":" + messageFromClient);
                    serverService.writeIntoFile(user.getName() + ":" + messageFromClient);
                    messageDao.addMessage(user.getName(), messageFromClient);
                    if (messageFromClient.equals("history")) {
                        serverService.showHistory(messageDao.getHistory(history), this);
                        continue;
                    }
                    serverService.notifyObserversExceptMe(user.getName() + ":" + messageFromClient, this);
                }
            }
        }
    }

    public boolean registration(String registrationMessage) {
        if (registrationMessage.startsWith("!registr!")) {
            String login = registrationMessage.substring(9).split(":")[0];
            String password = registrationMessage.substring(9).split(":")[1];

            try {
                user = userDao.findByNameAndPassword(login, password);
            } catch (UserNotFoundException e) {
                user = userDao.addUser(login, password);
                return true;
            }
        }
        return false;
    }

    @SneakyThrows
    private boolean authorization(String authorizationMessage) {
        if (authorizationMessage.startsWith("!autho!")) {
            String login = authorizationMessage.substring(7).split(":")[0];
            String password = authorizationMessage.substring(7).split(":")[1];

            user = userDao.findByNameAndPassword(login, password);
            return true;
        }
        return false;
    }

    private boolean reauthorization(String reauthorizationMessage) {
        if (reauthorizationMessage.startsWith("!reautho!")) {
            String login = reauthorizationMessage.substring(9).split(":")[0];
            String password = reauthorizationMessage.substring(9).split(":")[1];

            user = userDao.findByNameAndPassword(login, password);
            return true;
        }
        return false;
    }

    @SneakyThrows
    @Override
    public void notifyMe(String message) {
        PrintWriter clientWriter = new PrintWriter(socket.getOutputStream(), true);
        clientWriter.println(message);
    }
}
