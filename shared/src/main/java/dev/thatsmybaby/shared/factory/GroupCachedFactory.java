package dev.thatsmybaby.shared.factory;

import dev.thatsmybaby.shared.VersionInfo;
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
            String name = rs.fetchString("name");

            if (name == null) continue;

            this.setCachedGroup(GroupCache.fromResult(rs, this.fetchMetaCache(rs.fetchInt("rowId")), this.fetchPermissionCache(name)));
        }
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

        return GroupCache.fromResult(rs, this.fetchMetaCache(rs.fetchInt("rowId")), this.fetchPermissionCache(name));
    }

    public GroupCache storeGroup(@NonNull String name) {
        MysqlProvider.getInstance().storeAsync("GROUP_INSERT", name);

        return new GroupCache(
                name,
                0,
                new MetaCache("default", null, null, new HashSet<>(), new HashSet<>(), new HashSet<>()),
                new HashMap<>()
        );
    }

    public void storeGroup(@NonNull GroupCache groupCache) {
        // TODO: Update group cache
    }

    public MetaCache fetchMetaCache(int targetId) {
        AbstractResultSet rs = MysqlProvider.getInstance().fetch("NODE_META_SELECT_ALL_BY_TARGET_ID", targetId);

        if (rs == null || !rs.next()) {
            return MetaCache.empty();
        }

        Set<String> prefixes = new HashSet<>();
        Set<String> suffixes = new HashSet<>();

        while (rs.next()) {
            String context = rs.fetchString("context");
            String value = rs.fetchString("value");

            if (context == null || value == null) continue;

            if (context.equalsIgnoreCase("prefix")) {
                prefixes.add(value);
            } else {
                suffixes.add(value);
            }
        }

        return new MetaCache(null, null, null, new HashSet<>(), prefixes, suffixes);
    }

    public Map<String, PermissionCache> fetchPermissionCache(@NonNull String name) {
        AbstractResultSet rs = MysqlProvider.getInstance().fetch("SELECT * FROM abstract_permissions WHERE object_name = ?", name);

        if (rs == null) return null;

        Map<String, PermissionCache> map = new HashMap<>();

        while (rs.next()) {
            String permissionName = rs.fetchString("name");

            if (permissionName == null) continue;

            map.put(permissionName.toLowerCase(), PermissionCache.fromResult(rs));
        }

        return map;
    }
}