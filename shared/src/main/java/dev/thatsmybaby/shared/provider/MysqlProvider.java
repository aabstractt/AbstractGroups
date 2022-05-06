package dev.thatsmybaby.shared.provider;

import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class MysqlProvider {

    @Getter private final static MysqlProvider instance = new MysqlProvider();

    private Connection connection;

    public AbstractResultSet fetch(String sql, Object... args) {
        try (Connection connection = this.connection) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            this.set(preparedStatement, args);

            return new AbstractResultSet(preparedStatement, preparedStatement.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void store(String sql, Object... args) {
        try (Connection connection = this.connection) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            this.set(preparedStatement, args);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void set(PreparedStatement preparedStatement, Object... args) throws SQLException {
        for (int i = 0; i < args.length; i++) {
            Object result = args[i];

            if (result instanceof String) preparedStatement.setString(i+1, (String) result);
            if (result instanceof Integer) preparedStatement.setInt(i+1, (Integer) result);
            if (result instanceof Boolean) preparedStatement.setBoolean(i+1, (Boolean) result);
        }
    }
}