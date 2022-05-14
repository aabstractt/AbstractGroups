package dev.thatsmybaby.shared.object;

import dev.thatsmybaby.shared.provider.AbstractResultSet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
public final class PermissionCache {

    @Getter private final @NonNull String name;
    @Getter @Setter private @NonNull String serverName;
    @Setter private boolean value;

    public boolean getValue() {
        return this.value;
    }

    public static PermissionCache fromResult(AbstractResultSet rs) {
        return new PermissionCache(
                rs.fetchString("name"),
                rs.fetchString("server"),
                rs.fetchBoolean("value")
        );
    }
}