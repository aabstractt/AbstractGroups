package dev.thatsmybaby.shared;

import dev.thatsmybaby.shared.sender.AbstractSender;

import java.util.stream.Stream;

public interface AbstractPlugin {

    static AbstractPlugin getInstance() {
        throw new RuntimeException("Plugin not initialized");
    }

    boolean isPrimaryThread();

    Stream<AbstractSender> getOnlineSenders();
}