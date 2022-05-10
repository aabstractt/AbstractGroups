package dev.thatsmybaby.shared.object;

import dev.thatsmybaby.shared.factory.GroupCachedFactory;
import lombok.Getter;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public final class User extends PermissionHolder {

    private String primaryGroup = null;
    private String prefix = null;
    private String suffix = null;

    private Set<String> groups;

    public User(MetaCache metaCache, Map<String, PermissionCache> permissionCacheMap) {
        super(metaCache, permissionCacheMap);
    }

    public void recalculate() {
        GroupCache primaryGroup = recalculatePrimaryGroup(this.groups);

        if (primaryGroup == null) {
            throw new RuntimeException("Primary group was returned null");
        }

        this.primaryGroup = primaryGroup.getName();

        this.metaCache.setPrefix(primaryGroup.getMetaCache().getPrefix());
        this.metaCache.setSuffix(primaryGroup.getMetaCache().getSuffix());
    }

    public Set<GroupCache> getCachedGroups() {
        return this.groups.stream()
                .map(name -> GroupCachedFactory.getInstance().getCachedGroup(name))
                .collect(Collectors.toSet());
    }

    public static GroupCache recalculatePrimaryGroup(Set<String> groupNames) {
        return groupNames.stream()
                .map(name -> GroupCachedFactory.getInstance().getCachedGroup(name))
                .filter(Objects::nonNull)
                .min(Comparator.comparing(GroupCache::getPriority))
                .orElse(null);
    }
}