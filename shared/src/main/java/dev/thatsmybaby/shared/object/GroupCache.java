package dev.thatsmybaby.shared.object;

import dev.thatsmybaby.shared.provider.AbstractResultSet;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
public final class GroupCache extends PermissionHolder {

    private final String name;
    @Setter private int priority;

    @Setter private String prefix;
    @Setter private String suffix;

    public GroupCache(String name, int priority, String prefix, String suffix, Map<String, PermissionCache> permissionMetadata) {
        super(permissionMetadata);

        this.name = name;
        this.priority = priority;

        this.prefix = prefix;
        this.suffix = suffix;
    }

    public void invalidate() {
        super.invalidate();
    }

    public static GroupCache fromResult(AbstractResultSet rs, Map<String, PermissionCache> permissionsFetched) {
        return new GroupCache(
                rs.fetchString("name"),
                rs.fetchInt("priority"),
                rs.fetchString("prefix"),
                rs.fetchString("suffix"),
                permissionsFetched
        );
    }
}