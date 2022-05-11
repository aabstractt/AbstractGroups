package dev.thatsmybaby.shared;

import dev.thatsmybaby.shared.sender.AbstractSender;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Callable;
import java.util.stream.Stream;

public class AbstractPlugin {

    @Getter private final static AbstractPlugin instance = new AbstractPlugin();

    @Setter private Callable<Boolean> futurePrimaryThread;
    @Setter private Callable<Stream<AbstractSender>> futureOnlineSenders;

    @Setter private boolean storeLog = false;
    @Setter private boolean logRemote = false;

    public boolean isPrimaryThread() {
        try {
            return this.futurePrimaryThread.call();
        } catch (Exception e) {
            return false;
        }
    }

    public Stream<AbstractSender> getOnlineSenders() {
        try {
            return this.futureOnlineSenders.call();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean storeLog() {
        return this.storeLog;
    }

    public boolean logRemote() {
        return this.logRemote;
    }
}