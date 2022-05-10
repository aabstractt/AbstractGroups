package dev.thatsmybaby.shared.object;

import dev.thatsmybaby.shared.provider.AbstractResultSet;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
public final class GroupCache extends PermissionHolder {

    private final String name;
    @Setter private int priority;

    public GroupCache(String name, int priority, MetaCache metaCache, Map<String, PermissionCache> permissionMetadata) {
        super(metaCache, permissionMetadata);

        this.name = name;
        this.priority = priority;
    }

    public void invalidate() {
        super.invalidate();
    }

    public static GroupCache fromResult(AbstractResultSet rs, MetaCache metaCache, Map<String, PermissionCache> permissionsFetched) {
        return new GroupCache(
                rs.fetchString("name"),
                rs.fetchInt("priority"),
                metaCache,
                permissionsFetched
        );
    }
}