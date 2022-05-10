package dev.thatsmybaby.shared.provider;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.thatsmybaby.shared.TaskUtils;
import dev.thatsmybaby.shared.VersionInfo;
import lombok.Getter;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public final class MysqlProvider {

    @Getter private final static MysqlProvider instance = new MysqlProvider();

    private HikariDataSource dataSource = null;
    private HikariConfig hikariConfig = null;

    private final Map<String, String> statements = new HashMap<>();

    public void init(File file) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();

            return;
        }

        HikariConfig config = new HikariConfig(file.toString());

        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(50);
        config.setConnectionTimeout(10000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setValidationTimeout(120000);

        this.dataSource = new HikariDataSource(this.hikariConfig = config);
    }

    public AbstractResultSet fetch(String sql, Object... args) {
        if (this.disconnected()) return this.reconnect() ? this.fetch(sql, args) : null;

        if (!this.statements.containsKey(sql)) {
            throw new RuntimeException("Statement " + sql + " not found");
        }

        try (Connection connection = this.dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(this.statements.get(sql))) {
                this.set(preparedStatement, args);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return new AbstractResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void store(String sql, Object... args) {
        if (this.disconnected()) {
            if (this.reconnect()) {
                this.store(sql, args);
            }

            return;
        }

        if (!this.statements.containsKey(sql)) {
            throw new RuntimeException("Statement " + sql + " not found");
        }

        try (Connection connection = this.dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(this.statements.get(sql))) {
                this.set(preparedStatement, args);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void storeAsync(String sql, Object... args) {
        TaskUtils.runAsync(() -> this.store(sql, args));
    }

    private boolean disconnected() {
        return this.dataSource == null || this.dataSource.isClosed() || !this.dataSource.isRunning();
    }

    private boolean reconnect() {
        this.close();

        if (this.hikariConfig == null) {
            VersionInfo.getLogger().log(Level.FATAL, "Can't reconnect because Hikari config is null...");

            return false;
        }

        try {
            this.dataSource = new HikariDataSource(this.hikariConfig);
        } catch (Exception e) {
            VersionInfo.getLogger().log(Level.FATAL, "Can't reconnect because, reason: {}", e.getMessage());

            return false;
        }

        return true;
    }

    public void close() {
        if (this.dataSource != null) this.dataSource.close();
    }

    private void set(PreparedStatement preparedStatement, Object... args) throws SQLException {
        for (int i = 1; i <= args.length; i++) {
            Object result = args[i - 1];

            if (result instanceof String) preparedStatement.setString(i, (String) result);
            if (result instanceof Integer) preparedStatement.setInt(i, (Integer) result);
            if (result instanceof Boolean) preparedStatement.setBoolean(i, (Boolean) result);
            if (result instanceof Float) preparedStatement.setFloat(i, (Float) result);
        }
    }
}