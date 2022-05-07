package dev.thatsmybaby.shared.factory;

import dev.thatsmybaby.shared.object.GroupCache;
import dev.thatsmybaby.shared.object.PermissionCache;
import dev.thatsmybaby.shared.provider.AbstractResultSet;
import dev.thatsmybaby.shared.provider.MysqlProvider;
import lombok.Getter;
import lombok.NonNull;
import org.apache.logging.log4j.Level;

import java.util.HashMap;
import java.util.Map;

public final class GroupCachedFactory {

    @Getter private final static GroupCachedFactory instance = new GroupCachedFactory();

    private final Map<String, GroupCache> groupCacheMap = new HashMap<>();

    public void init() {
        AbstractResultSet rs = MysqlProvider.getInstance().fetch("SELECT * FROM abstract_groups");

        if (rs == null) {
            MysqlProvider.getInstance().getLogger().log(Level.FATAL, "Error trying fetch abstract_groups and result received null");

            return;
        }

        while (rs.next()) {
            this.setCachedGroup(GroupCache.fromResult(rs, this.fetchPermissionCache(rs.fetchString("name"))));
        }
    }

    public GroupCache getCachedGroup(@NonNull String name) {
        return this.groupCacheMap.get(name.toLowerCase());
    }

    public void setCachedGroup(@NonNull GroupCache group) {
        this.groupCacheMap.put(group.getName().toLowerCase(), group);
    }

    public GroupCache fetchGroupCache(@NonNull String name) {
        AbstractResultSet rs = MysqlProvider.getInstance().fetch("SELECT * FROM abstract_groups WHERE name = ?", name);

        if (rs == null || !rs.next()) return null;

        return GroupCache.fromResult(rs, this.fetchPermissionCache(name));
    }

    public GroupCache storeGroup(@NonNull String name) {
        MysqlProvider.getInstance().store("INSERT INTO abstract_groups");

        return new GroupCache(name, 0, "", "", new HashMap<>());
    }

    public void storeGroup(@NonNull GroupCache groupCache) {
        // TODO: Update group cache
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