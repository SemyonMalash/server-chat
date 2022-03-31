package ru.itsjava;

import ru.itsjava.services.ServerServiceImpl;

public class Application {

    public static void main(String[] args) {
        ServerServiceImpl serverService = new ServerServiceImpl();
        serverService.start();

//        ClientRunnable clientRunnable = new ClientRunnable(new Socket(), new ServerServiceImpl(), new UserDaoImpl(new Props()));
//        String regMessage = "!registr!UT:PT";
//
//        System.out.println(clientRunnable.registration(regMessage));
    }
}
