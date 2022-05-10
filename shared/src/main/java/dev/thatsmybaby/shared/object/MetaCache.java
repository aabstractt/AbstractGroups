package dev.thatsmybaby.shared.object;

import dev.thatsmybaby.shared.factory.GroupCachedFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor @Getter
public final class MetaCache {

    @Setter private String prefix;
    @Setter private String suffix;

    private Set<String> prefixes;
    private Set<String> suffixes;

    public static MetaCache empty() {
        return new MetaCache(
                null,
                null,
                new HashSet<>(),
                new HashSet<>()
        );
    }
}