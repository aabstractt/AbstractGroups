package dev.thatsmybaby.shared.actionlog;

import dev.thatsmybaby.shared.provider.MysqlProvider;
import dev.thatsmybaby.shared.sender.AbstractSender;
import lombok.Getter;

public final class LogFactory {

    @Getter private final static LogFactory instance = new LogFactory();

    public void broadcast(LoggedAction entry, AbstractSender sender) {
        // TODO: Do this async
        MysqlProvider.getInstance().store("INSERT INTO abstractgroups_logs () VALUES (?, ?)", sender);

        // TODO: Send redis pubsub
    }
}