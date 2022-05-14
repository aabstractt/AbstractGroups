package dev.thatsmybaby.shared.factory;

import dev.thatsmybaby.shared.VersionInfo;
import dev.thatsmybaby.shared.actionlog.LoggedAction;
import dev.thatsmybaby.shared.object.GroupCache;
import dev.thatsmybaby.shared.object.MetaCache;
import dev.thatsmybaby.shared.object.PermissionCache;
import dev.thatsmybaby.shared.provider.AbstractResultSet;
import dev.thatsmybaby.shared.provider.MysqlProvider;
import lombok.Getter;
import lombok.NonNull;
import org.apache.logging.log4j.Level;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class GroupCachedFactory {

    @Getter private final static GroupCachedFactory instance = new GroupCachedFactory();

    private final Map<String, GroupCache> groupCacheMap = new HashMap<>();

    public void init() {
        AbstractResultSet rs = MysqlProvider.getInstance().fetch("GROUP_SELECT_ALL");

        if (rs == null) {
            VersionInfo.getLogger().log(Level.FATAL, "Error trying fetch all groups and result received null");

            return;
        }

        while (rs.next()) {
            this.setCachedGroup(GroupCache.fromResult(
                    rs,
                    this.fetchMetaCache(rs.fetchInt("rowId")),
                    this.fetchPermissionCache(rs.fetchInt("rowId"))
            ));
        }

        rs.invalidate();
    }

    public GroupCache getCachedGroup(@NonNull String name) {
        return this.groupCacheMap.get(name.toLowerCase());
    }

    public void setCachedGroup(@NonNull GroupCache group) {
        this.groupCacheMap.put(group.getName().toLowerCase(), group);
    }

    public GroupCache fetchGroupCache(@NonNull String name) {
        AbstractResultSet rs = MysqlProvider.getInstance().fetch("GROUP_SELECT", name);

        if (rs == null || !rs.next()) return null;

        // Maybe this is a good solution?
        try {
            return GroupCache.fromResult(
                    rs,
                    this.fetchMetaCache(rs.fetchInt("rowId")),
                    this.fetchPermissionCache(rs.fetchInt("rowId"))
            );
        } finally {
            rs.invalidate();
        }
    }

    public GroupCache storeGroup(@NonNull String name) {
        Object generatedKey = MysqlProvider.getInstance().storeAndFetch("GROUP_INSERT", name);

        if (!(generatedKey instanceof Integer)) {
            return null;
        }

        return new GroupCache(
                (Integer) generatedKey,
                name,
                0,
                MetaCache.empty(),
                new HashMap<>()
        );
    }

    public void storeMeta(int targetId, LoggedAction.Type type, String context, String value) {
        MysqlProvider.getInstance().storeAsync("NODE_META_INSERT",
                targetId,
                type.toString(),
                context,
                value
        );
    }

    public void removeMeta(int targetId, LoggedAction.Type type, String context, String value) {
        MysqlProvider.getInstance().store("NODE_META_DELETE", targetId, type.toString(), context, value);
    }

    public @NonNull MetaCache fetchMetaCache(int targetId) {
        AbstractResultSet rs = MysqlProvider.getInstance().fetch("NODE_META_SELECT_ALL_BY_TARGET_ID", targetId);

        if (rs == null || !rs.next()) {
            return MetaCache.empty();
        }

        Set<String> prefixes = new HashSet<>();
        Set<String> suffixes = new HashSet<>();

        while (rs.next()) {
            String context = rs.fetchString("context");
            String value = rs.fetchString("value");

            if (context.equalsIgnoreCase("prefix")) {
                prefixes.add(value);
            } else {
                suffixes.add(value);
            }
        }

        rs.invalidate();

        return new MetaCache(
                prefixes.stream().findFirst().orElse(null),
                suffixes.stream().findFirst().orElse(null),
                prefixes,
                suffixes
        );
    }

    public Map<String, PermissionCache> fetchPermissionCache(int groupId) {
        AbstractResultSet rs = MysqlProvider.getInstance().fetch("GROUP_PERMISSIONS_SELECT_ALL", groupId);

        if (rs == null) return null;

        Map<String, PermissionCache> map = new HashMap<>();

        while (rs.next()) {
            map.put(rs.fetchString("name").toLowerCase(), PermissionCache.fromResult(rs));
        }

        rs.invalidate();

        return map;
    }
}