package dev.thatsmybaby.shared.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor @Getter
public final class MetaCache {

    @Setter private String prefix;
    @Setter private String suffix;

    private Set<String> prefixes;
    private Set<String> suffixes;

    public void invalidate() {
        this.prefixes.clear();

        this.suffixes.clear();
    }

    public void invalidatePrefix(String prefix) {
        this.prefixes.clear();

        this.prefix = prefix;
    }

    public void invalidateSuffix(String suffix) {
        this.suffixes.clear();

        this.suffix = suffix;
    }

    public void recalculate() {
        // TODO: Recalculate prefixes, suffixes etc and order it as createdAt date to get the first prefix and suffix
    }

    public void addPrefix(String prefix) {
        this.prefixes.add(prefix);
    }

    public boolean findPrefix(String prefix) {
        return this.prefixes.contains(prefix) || Objects.equals(this.prefix, prefix);
    }

    public boolean findSuffix(String suffix) {
        return this.suffixes.contains(suffix) || Objects.equals(this.suffix, suffix);
    }

    public static MetaCache empty() {
        return new MetaCache(
                null,
                null,
                new HashSet<>(),
                new HashSet<>()
        );
    }
}