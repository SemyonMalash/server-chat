package ru.itsjava.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.itsjava.dao.UserDao;
import ru.itsjava.dao.UserNotFoundException;
import ru.itsjava.domain.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@RequiredArgsConstructor
public class ClientRunnable implements Runnable, Observer {
    private final Socket socket;
    private final ServerService serverService;
    private User user;
    private final UserDao userDao;

    @SneakyThrows
    @Override
    public void run() {
        System.out.println("Client is connected");
        BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String messageFromClient;
        String input;

        while ((input = bufferedReader.readLine()) != null) {
            if (authorization(input)) {
                serverService.addObserver(this);
                while ((messageFromClient = bufferedReader.readLine()) != null) {
                    System.out.println(user.getName() + ":" + messageFromClient);
                    serverService.notifyObserversExceptMe(user.getName() + ":" + messageFromClient, this);
                }
            } else if (registration(input)) {
                serverService.addObserver(this);
                while ((messageFromClient = bufferedReader.readLine()) != null) {
                    System.out.println(user.getName() + ":" + messageFromClient);
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
        //!autho!login:password
        if (authorizationMessage.startsWith("!autho!")) {
            String login = authorizationMessage.substring(7).split(":")[0];
            String password = authorizationMessage.substring(7).split(":")[1];

            user = userDao.findByNameAndPassword(login, password);
            return true;
        }
        return false;
    }

    @SneakyThrows
    @Override
    public void notifyMe(String message) {
        PrintWriter clientWriter = new PrintWriter(socket.getOutputStream());
        clientWriter.println(message);
        clientWriter.flush();
    }
}
