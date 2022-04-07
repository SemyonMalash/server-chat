package ru.itsjava.dao;

import lombok.AllArgsConstructor;
import ru.itsjava.domain.Message;
import ru.itsjava.utils.Props;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@AllArgsConstructor
public class MessageDaoImpl implements MessageDao {
    private final Props props;

    @Override
    public Message addMessage(String from, String text) {
        try (Connection connection = DriverManager.getConnection(
                props.getValue("db.url"),
                props.getValue("db.login"),
                props.getValue("db.password"))
        ) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO new_schema.messages(`from`, text) values (?, ?);");

            preparedStatement.setString(1, from);
            preparedStatement.setString(2, text);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Message(from, text);
    }
}
