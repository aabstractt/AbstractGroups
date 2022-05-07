package dev.thatsmybaby.shared;

import dev.thatsmybaby.shared.sender.AbstractSender;

import java.util.stream.Stream;

public interface AbstractPlugin {

    static AbstractPlugin getInstance() {
        throw new RuntimeException("Plugin not initialized");
    }

    default boolean isPrimaryThread() {
        throw new RuntimeException("Plugin not initialized");
    }

    default Stream<AbstractSender> getOnlineSenders() {
        throw new RuntimeException("Plugin not initialized");
    }

    default boolean storeLog() {
        throw new RuntimeException("Plugin not initialized");
    }

    default boolean logRemote() {
        throw new RuntimeException("Plugin not initialized");
    }
}