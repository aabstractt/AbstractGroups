package dev.thatsmybaby.shared.actionlog;

import dev.thatsmybaby.shared.provider.MysqlProvider;
import dev.thatsmybaby.shared.sender.AbstractSender;
import lombok.*;

public final class LogFactory {

    @Getter private final static LogFactory instance = new LogFactory();

    @Setter private boolean storeAllowed = false;
    @Setter private boolean remote = false;

    public void broadcast(@NonNull LoggedAction entry, @NonNull AbstractSender sender) {
        if (this.storeAllowed) {
            MysqlProvider.getInstance().storeAsync(Queries.INSERT_LOG.getResult(),
                    entry.getTimestamp(),
                    entry.getSourceXuid(),
                    entry.getSourceName(),
                    LoggedAction.typeAsCharacter(entry.getType()).toString(),
                    entry.getTargetXuid(),
                    entry.getTargetName()
            );
        }

        if (remote) {
            // TODO: Send the redis packet of log action
            //RedisProvider.getInstance().redisMessage();
        }

        this.broadcast(entry);
    }

    public void broadcast(@NonNull LoggedAction entry) {
        // TODO: get all players and send the message
    }

    @RequiredArgsConstructor (access = AccessLevel.PRIVATE)
    private enum Queries {
        INSERT_LOG("INSERT INTO <table>_logs (timestamp, source_xuid, source_name, type, target_xuid, target_name, action)");

        @Getter private final String result;
    }
}