package dev.thatsmybaby.shared.object;

import com.google.common.base.Splitter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor @Getter
public abstract class PermissionHolder {

    protected MetaCache metaCache;
    @Setter protected Map<String, PermissionCache> permissionCacheMap;

    public PermissionCache getCachedPermission(@NonNull String permissionName) {
        return this.permissionCacheMap.get(permissionName.toLowerCase());
    }

    public boolean hasPermissionCache(@NonNull String permissionName) {
        return this.permissionCacheMap.containsKey(permissionName.toLowerCase());
    }

    public boolean hasPermission(String name) {
        PermissionCache permissionCache = this.permissionCacheMap.getOrDefault(name, this.permissionCacheMap.get("*"));

        if (permissionCache != null) {
            return permissionCache.isValue();
        }

        StringBuilder parsed = new StringBuilder();

        for (String split : Splitter.on('.').splitToList(name)) {
            permissionCache = this.permissionCacheMap.get(parsed.toString());

            if (permissionCache != null) {
                return permissionCache.isValue();
            }

            permissionCache = this.permissionCacheMap.get(parsed + ".*");

            if (permissionCache != null) {
                return permissionCache.isValue();
            }

            parsed.append(split);
        }

        return false;
    }

    public void invalidate() {
        this.permissionCacheMap.clear();
    }
}