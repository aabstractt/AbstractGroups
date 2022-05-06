package dev.thatsmybaby.shared.sender;

public abstract class AbstractSender {

    public abstract boolean hasPermission(String permission);

    public abstract void sendMessage(String k, String... args);
}