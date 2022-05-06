package dev.thatsmybaby.shared.object;

import dev.thatsmybaby.shared.provider.AbstractResultSet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor @Getter
public final class PermissionCache {

    private final String name;
    @Setter private String serverName;
    @Setter private boolean value;

    public static PermissionCache fromResult(AbstractResultSet rs) {
        return new PermissionCache(
                rs.fetchString("name"),
                rs.fetchString("server"),
                rs.fetchBoolean("value")
        );
    }
}