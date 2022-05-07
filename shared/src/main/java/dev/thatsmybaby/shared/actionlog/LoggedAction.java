package dev.thatsmybaby.shared.actionlog;

import dev.thatsmybaby.shared.object.GroupCache;
import dev.thatsmybaby.shared.object.PermissionHolder;
import dev.thatsmybaby.shared.sender.AbstractSender;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE) @Getter
public final class LoggedAction {

    private final long timestamp;

    private final String sourceXuid;
    private final String sourceName;

    private final String targetXuid;
    private final String targetName;

    private final Type type;
    private final String action;

    public static @NonNull  Builder build() {
        return new Builder();
    }

    public void submit(AbstractSender sender) {
        LogFactory.getInstance().broadcast(this, sender);
    }

    public final static class Builder {

        private long timestamp = 0L;

        private String sourceXuid;
        private String sourceName;

        private String targetXuid = null;
        private String targetName;

        private Type type;
        private String action;

        private Builder() {
            // Not allow make instance
        }

        public @NonNull Builder timestamp(Instant instant) {
            this.timestamp = instant.getEpochSecond();

            return this;
        }

        public @NonNull Builder source(String sourceXuid, String sourceName) {
            this.sourceXuid = sourceXuid;

            this.sourceName = sourceName;

            return this;
        }

        public @NonNull Builder source(AbstractSender sender) {
            return this.source(sender.getXuid(), sender.getName());
        }

        public @NonNull Builder target(String targetXuid, @NonNull String targetName) {
            this.targetXuid = targetXuid;

            this.targetName = targetName;

            return this;
        }

        public @NonNull Builder target(AbstractSender target) {
            return this.source(target.getXuid(), target.getName());
        }

        public @NonNull Builder target(PermissionHolder target) {
            if (target instanceof GroupCache) {
                return this.target(null, ((GroupCache) target).getName()).type(Type.GROUP);
            }

            return this.target(null, "");
        }

        public Builder type(Type type) {
            this.type = type;

            return this;
        }

        public @NonNull Builder action(Object args) {
            this.action = "";

            return this;
        }

        public @NonNull LoggedAction build() {
            return new LoggedAction(this.timestamp, this.sourceXuid, this.sourceName, this.targetXuid, this.targetName, this.type, this.action);
        }
    }

    public enum Type {
        USER, GROUP;

        public String toString() {
            return ((Character) (this.equals(Type.GROUP) ? 'G' : 'U')).toString();
        }
    }
}