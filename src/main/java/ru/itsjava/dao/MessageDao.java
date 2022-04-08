package ru.itsjava.dao;

import ru.itsjava.domain.Message;

import java.util.ArrayList;

public interface MessageDao {
    Message addMessage(String from, String text);
    String getHistory(ArrayList arrayList);
}
