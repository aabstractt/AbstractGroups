package dev.thatsmybaby.shared.provider;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

@RequiredArgsConstructor
public final class AbstractResultSet {

    private final ListIterator<Map<String, Object>> iterator;

    private Map<String, Object> current = new HashMap<>();

    public boolean next() {
        boolean next = this.iterator.hasNext();

        this.invalidate();

        if (next) this.current = this.iterator.next();

        return next;
    }

    public @NonNull String fetchString(String field) {
        return (String) this.current.getOrDefault(field, "");
    }

    public int fetchInt(String field) {
        return (int) this.current.getOrDefault(field, -1);
    }

    public boolean fetchBoolean(String field) {
        return (boolean) this.current.getOrDefault(field, false);
    }

    public void invalidate() {
        if (this.current != null) this.current.clear();
    }

    public static @NonNull AbstractResultSet fetch(@NonNull ResultSet rs) throws SQLException {
        List<Map<String, Object>> list = new LinkedList<>();

        while (rs.next()) {
            ResultSetMetaData metaData = rs.getMetaData();

            Map<String, Object> map = new HashMap<>();

            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                map.put(metaData.getColumnName(i), rs.getObject(i));
            }

            list.add(map);
        }

        return new AbstractResultSet(list.listIterator());
    }
}