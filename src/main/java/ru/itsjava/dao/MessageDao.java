package ru.itsjava.dao;

import ru.itsjava.domain.Message;

public interface MessageDao {
    Message addMessage(String from, String text);
}
