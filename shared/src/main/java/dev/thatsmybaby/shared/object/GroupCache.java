package dev.thatsmybaby.shared.object;

import dev.thatsmybaby.shared.provider.AbstractResultSet;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Map;

@Getter
public final class GroupCache extends PermissionHolder {

    private final int rowId;
    private final @NonNull String name;
    @Setter private int priority;

    public GroupCache(int rowId, @NonNull String name, int priority, @NonNull MetaCache metaCache, Map<String, PermissionCache> permissionMetadata) {
        super(metaCache, permissionMetadata);

        this.rowId = rowId;

        this.name = name;
        this.priority = priority;
    }

    public void invalidate() {
        super.invalidate();
    }

    public static GroupCache fromResult(AbstractResultSet rs, MetaCache metaCache, Map<String, PermissionCache> permissionsFetched) {
        return new GroupCache(
                rs.fetchInt("rowId"),
                rs.fetchString("name"),
                rs.fetchInt("priority"),
                metaCache,
                permissionsFetched
        );
    }
}