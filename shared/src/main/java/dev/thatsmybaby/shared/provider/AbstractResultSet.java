package dev.thatsmybaby.shared.provider;

import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
public final class AbstractResultSet {

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

    public String fetchStringNonNull(String field) {
        String result = this.fetchString(field);

        if (result == null) {
            throw new RuntimeException("Tried fetch " + field + " but received a null value");
        }

        return result;
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
}