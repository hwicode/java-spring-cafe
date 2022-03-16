package com.kakao.cafe.repository;

import com.kakao.cafe.domain.User;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

public class JdbcUserRepository implements UserRepository {

    private final DataSource dataSource;

    public JdbcUserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private String serializeUser(User user) throws IOException {
        byte[] serializedMember;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(user);
                serializedMember = baos.toByteArray();
            }
        }
        return Base64.getEncoder().encodeToString(serializedMember);
    }

    private User deserializeUser(String base64User) throws IOException, ClassNotFoundException {
        byte[] serializedMember = Base64.getDecoder().decode(base64User);
        try (ByteArrayInputStream bais = new ByteArrayInputStream(serializedMember)) {
            try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                Object objectUser = ois.readObject();
                return (User) objectUser;
            }
        }
    }

    @Override
    public User save(User user) {
        String sql = "insert into users(id, name, userObject) values(?, ?, ?)";

        Connection connnection = null;
        PreparedStatement preparedStatement = null;

        try {
            connnection = getConnection();
            preparedStatement = connnection.prepareStatement(sql);

            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, serializeUser(user));

            preparedStatement.executeUpdate();

            return user;

        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(connnection, preparedStatement, null);
        }
    }

    @Override
    public Optional<User> findById(String id) {
        String sql = "select * from users where id = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                //user 문자열을 가져온뒤 객체로 디코딩해줌
                User user = deserializeUser(resultSet.getString("userObject"));
                return Optional.of(user);
            }
            return Optional.empty();

        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "select * from users";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);

            resultSet = preparedStatement.executeQuery();

            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                //user 문자열을 가져온뒤 객체로 디코딩해줌
                User user = deserializeUser(resultSet.getString("userObject"));
                users.add(user);
            }
            return users;

        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
    }

    @Override
    public Optional<User> findByName(String name) {
        String sql = "select * from users where name = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                //user 문자열을 가져온뒤 객체로 디코딩해줌
                User user = deserializeUser(resultSet.getString("userObject"));
                return Optional.of(user);
            }
            return Optional.empty();

        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
    }

    @Override
    public User update(User savedUser, User newUser) {
        delete(savedUser);
        return save(newUser);
    }

    @Override
    public void delete(User user) {
        String sql = "delete from users where id = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getUserId());
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(connection, preparedStatement, null);
        }
    }

    @Override
    public void deleteAll() {
        String sql = "delete from users";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(connection, preparedStatement, null);
        }
    }

    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }

    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                close(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void close(Connection conn) throws SQLException {
        DataSourceUtils.releaseConnection(conn, dataSource);
    }
}
