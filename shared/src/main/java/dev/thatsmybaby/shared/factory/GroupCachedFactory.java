package dev.thatsmybaby.shared.factory;

import dev.thatsmybaby.shared.object.GroupCache;
import dev.thatsmybaby.shared.object.PermissionCache;
import dev.thatsmybaby.shared.provider.AbstractResultSet;
import dev.thatsmybaby.shared.provider.MysqlProvider;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public final class GroupCachedFactory {

    @Getter private final static GroupCachedFactory instance = new GroupCachedFactory();

    private final Map<String, GroupCache> groupCacheMap = new HashMap<>();

    public void init() {
        AbstractResultSet rs = MysqlProvider.getInstance().fetch("SELECT * FROM abstract_groups");

        if (rs == null) {
            // TODO: Log a error

            return;
        }

        while (rs.next()) {
            this.setCachedGroup(GroupCache.fromResult(rs, this.fetchPermissionCache(rs.fetchString("name"))));
        }
    }

    public GroupCache getCachedGroup(String name) {
        return this.groupCacheMap.get(name.toLowerCase());
    }

    public void setCachedGroup(GroupCache group) {
        this.groupCacheMap.put(group.getName().toLowerCase(), group);
    }

    public GroupCache fetchGroupCache(String name) {
        AbstractResultSet rs = MysqlProvider.getInstance().fetch("SELECT * FROM abstract_groups WHERE name = ?", name);

        if (rs == null) return null;

        return GroupCache.fromResult(rs, this.fetchPermissionCache(name));
    }

    public GroupCache storeGroup(String name) {
        MysqlProvider.getInstance().store("INSERT INTO abstract_groups");

        return new GroupCache(name, 0, "", "", new HashMap<>());
    }

    public void storeGroup(GroupCache groupCache) {
        // TODO: Update group cache
    }

    public Map<String, PermissionCache> fetchPermissionCache(String name) {
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