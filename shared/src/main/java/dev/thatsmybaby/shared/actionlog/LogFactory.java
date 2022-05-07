package dev.thatsmybaby.shared.actionlog;

import dev.thatsmybaby.shared.AbstractPlugin;
import dev.thatsmybaby.shared.provider.MysqlProvider;
import dev.thatsmybaby.shared.sender.AbstractSender;
import lombok.*;

public final class LogFactory {

    @Getter private final static LogFactory instance = new LogFactory();

    public void broadcast(@NonNull LoggedAction entry, @NonNull AbstractSender sender) {
        if (AbstractPlugin.getInstance().storeNotify()) {
            MysqlProvider.getInstance().storeAsync(Queries.INSERT_LOG.getResult(),
                    entry.getTimestamp(),
                    entry.getSourceXuid(),
                    entry.getSourceName(),
                    entry.getType().toString(),
                    entry.getTargetXuid(),
                    entry.getTargetName()
            );
        }

        if (AbstractPlugin.getInstance().notifyRemote()) {
            // TODO: Send the redis packet of log action
            //RedisProvider.getInstance().redisMessage();
        }

        this.broadcast(entry);
    }

    public void broadcast(@NonNull LoggedAction entry) {
        AbstractPlugin.getInstance().getOnlineSenders()
                .filter(sender -> sender.hasPermission("log.notify"))
                .forEach(sender -> sender.sendMessage("LOG_NOTIFY", entry.getSourceName(), entry.getTargetName(), entry.getAction()));
    }

    @RequiredArgsConstructor (access = AccessLevel.PRIVATE)
    private enum Queries {
        INSERT_LOG("INSERT INTO <table>_logs (timestamp, source_xuid, source_name, type, target_xuid, target_name, action)");

        @Getter private final String result;
    }
}