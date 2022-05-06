package dev.thatsmybaby.shared.command;

import dev.thatsmybaby.shared.sender.AbstractSender;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor @Getter
public abstract class AbstractArgument<T> {

    private final String name;
    private final String description;
    private final String permission;
    private final String usage;

    public abstract void execute(AbstractSender sender, String commandLabel, String argumentLabel, T target, String[] args);

    public boolean authorized(AbstractSender sender) {
        return this.permission == null || sender.hasPermission(this.permission);
    }
}