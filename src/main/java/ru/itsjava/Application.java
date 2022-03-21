package ru.itsjava;

import ru.itsjava.services.ServerServiceImpl;

public class Application {

    public static void main(String[] args) {
        ServerServiceImpl serverService = new ServerServiceImpl();
        serverService.start();
    }
}
