package dev.thatsmybaby.shared.object;

import dev.thatsmybaby.shared.provider.AbstractResultSet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor @Getter
public final class PermissionCache {

    private final @NonNull String name;
    @Setter private @NonNull String serverName;
    @Setter private boolean value;

    public static PermissionCache fromResult(AbstractResultSet rs) {
        return new PermissionCache(
                rs.fetchStringNonNull("name"),
                rs.fetchStringNonNull("server"),
                rs.fetchBoolean("value")
        );
    }
}