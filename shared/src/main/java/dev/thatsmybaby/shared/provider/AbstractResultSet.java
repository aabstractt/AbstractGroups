package dev.thatsmybaby.shared.provider;

import lombok.RequiredArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
public final class AbstractResultSet {

    private final PreparedStatement preparedStatement;
    private final ResultSet resultSet;

    public boolean next() {
        try {
            return this.resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String fetchString(String name) {
        try {
            return this.resultSet.getString(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int fetchInt(String name) {
        try {
            return this.resultSet.getInt(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public boolean fetchBoolean(String name) {
        try {
            return this.resultSet.getBoolean(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void close() throws SQLException {
        this.resultSet.close();

        this.preparedStatement.close();
    }
}