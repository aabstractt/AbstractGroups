package dev.thatsmybaby.shared.actionlog;

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

    public static @NonNull  Builder build() {
        return new Builder();
    }

    public final static class Builder {

        private long timestamp = 0L;

        private String sourceXuid;
        private String sourceName;

        private String targetXuid;
        private String targetName;

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

        public @NonNull Builder target(String targetXuid, String targetName) {
            this.targetXuid = targetXuid;

            this.targetName = targetName;

            return this;
        }

        public @NonNull Builder target(AbstractSender sender) {
            return this.source(sender.getXuid(), sender.getName());
        }

        public @NonNull LoggedAction build() {
            return new LoggedAction(this.timestamp, this.sourceXuid, this.sourceName, this.targetXuid, this.targetName);
        }
    }
}