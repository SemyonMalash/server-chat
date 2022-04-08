package ru.itsjava.dao;

import lombok.AllArgsConstructor;
import ru.itsjava.domain.Message;
import ru.itsjava.utils.Props;

import java.sql.*;
import java.util.ArrayList;

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

    public String getHistory(ArrayList arrayList) {
        try (Connection connection = DriverManager.getConnection(
                props.getValue("db.url"),
                props.getValue("db.login"),
                props.getValue("db.password"))
        ) {
            PreparedStatement statement = connection.prepareStatement("SELECT `from`, `text` FROM (SELECT * FROM messages ORDER BY id DESC LIMIT 10) messages ORDER BY id;");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                arrayList.add(resultSet.getString(1) + ":" + resultSet.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arrayList.toString();
    }
}
