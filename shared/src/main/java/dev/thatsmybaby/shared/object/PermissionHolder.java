package dev.thatsmybaby.shared.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor @Getter
public abstract class PermissionHolder {

    @Setter protected Map<String, PermissionCache> permissionCacheMap;

    public PermissionCache getCachedPermission(@NonNull String permissionName) {
        return this.permissionCacheMap.get(permissionName.toLowerCase());
    }

    public boolean hasPermissionCache(@NonNull String permissionName) {
        return this.permissionCacheMap.containsKey(permissionName.toLowerCase());
    }

    public void invalidate() {

    }
}