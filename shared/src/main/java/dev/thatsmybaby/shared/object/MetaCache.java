package dev.thatsmybaby.shared.object;

import dev.thatsmybaby.shared.factory.GroupCachedFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor @Getter
public final class MetaCache {

    private String primaryGroup;
    private String prefix;
    private String suffix;

    private Set<String> groups;
    private Set<String> prefixes;
    private Set<String> suffixes;

    public Set<GroupCache> getCachedGroups() {
        return this.groups.stream()
                .map(name -> GroupCachedFactory.getInstance().getCachedGroup(name))
                .collect(Collectors.toSet());
    }

    public static MetaCache build(Set<String> groupNames, Set<String> prefixes, Set<String> suffixes) {
        GroupCache groupCache = recalculatePrimaryGroup(groupNames);

        if (groupCache == null) {
            return null;
        }

        return new MetaCache(groupCache.getName(), groupCache.getMetaCache().getPrefix(), groupCache.getMetaCache().getSuffix(), groupNames, prefixes, suffixes);
    }

    public static GroupCache recalculatePrimaryGroup(Set<String> groupNames) {
        return groupNames.stream()
                .map(name -> GroupCachedFactory.getInstance().getCachedGroup(name))
                .filter(Objects::nonNull)
                .min(Comparator.comparing(GroupCache::getPriority))
                .orElse(null);
    }

    public static MetaCache empty() {
        return new MetaCache(
                "default",
                null,
                null,
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>()
        );
    }
}