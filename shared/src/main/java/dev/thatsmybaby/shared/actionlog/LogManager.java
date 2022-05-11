package dev.thatsmybaby.shared.actionlog;

import dev.thatsmybaby.shared.AbstractPlugin;
import dev.thatsmybaby.shared.provider.MysqlProvider;
import dev.thatsmybaby.shared.sender.AbstractSender;
import lombok.*;

public final class LogManager {

    @Getter private final static LogManager instance = new LogManager();

    public void broadcast(@NonNull LoggedAction entry, @NonNull AbstractSender sender) {
        if (AbstractPlugin.getInstance().storeLog()) {
            MysqlProvider.getInstance().storeAsync("LOGS_INSERT",
                    entry.getTimestamp(),
                    entry.getSourceXuid(),
                    entry.getSourceName(),
                    entry.getType().toString(),
                    entry.getTargetXuid(),
                    entry.getTargetName(),
                    entry.getAction()
            );
        }

        if (AbstractPlugin.getInstance().logRemote()) {
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
}