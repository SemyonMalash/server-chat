package ru.itsjava.dao;

import lombok.AllArgsConstructor;
import ru.itsjava.domain.User;
import ru.itsjava.utils.Props;

import java.sql.*;

@AllArgsConstructor
public class UserDaoImpl implements UserDao {
    private final Props props;

    @Override
    public User findByNameAndPassword(String name, String password) {
        try (Connection connection = DriverManager.getConnection(
                props.getValue("db.url"),
                props.getValue("db.login"),
                props.getValue("db.password"))
        ) {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select count(*) cnt from new_schema.users where name = ? and password = ?;");

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            int userCount = resultSet.getInt("cnt");

            if (userCount == 1) {
                return new User(name, password);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        throw new UserNotFoundException("User not found");
    }

    @Override
    public User addUser(String name, String password) {
        try (Connection connection = DriverManager.getConnection(
                props.getValue("db.url"),
                props.getValue("db.login"),
                props.getValue("db.password"));
        ) {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("INSERT INTO new_schema.users(name, password) values (?, ?);");

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new User(name, password);
    }

    @Override
    public void removeUser(String name) {
        try (Connection connection = DriverManager.getConnection(
                props.getValue("db.url"),
                props.getValue("db.login"),
                props.getValue("db.password"));
        ) {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("DELETE FROM new_schema.users where name = ?");

            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
